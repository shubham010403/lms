package com.example.lms.service.serviceImpl;
import com.example.lms.dto.BookResDto;
import com.example.lms.dto.BorrowDto;
import com.example.lms.dto.UserResDto;
import com.example.lms.entities.BookEntity;
import com.example.lms.entities.BorrowEntity;
import com.example.lms.entities.UserEntity;
import com.example.lms.exception.CustomServiceException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.IBorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class BorrowService implements IBorrowService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    // Borrow a book (decrease available copies)
    @Override
    public BookResDto borrowBook(Long id) {
        // Retrieve the book by ID
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // Check if there are available copies to borrow
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies to borrow.");
        }

        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Optional<UserEntity> userOptional = userRepository.findByName(name);

        // Proceed if the user exists
        if (userOptional.isPresent()) {
            // Create a new borrow record
            BorrowEntity record = new BorrowEntity();
            record.setUserEntity(userOptional.get());
            record.setBookEntity(book);
            borrowRepository.save(record); // Save the borrow record

            // Decrease the available copies and save the book entity
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            BookEntity updatedBook = bookRepository.save(book); // Save the updated book once

            // Return the updated book as a DTO
            return convertToResDto(updatedBook);
        } else {
            throw new CustomServiceException("Username not found");
        }
    }

    @Override
    public BookResDto returnBook(Long bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        log.info("Retrieved bookEntity entity: {}", bookEntity);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new CustomServiceException("Username not found: " + username));
        BorrowEntity borrowRecord = borrowRepository.findByUserEntityIdAndBookEntityId(user.getId(), bookEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found for user: " + username + " and bookEntity: " + bookId));

        borrowRepository.delete(borrowRecord);
        bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() + 1);
        BookEntity updatedBook = bookRepository.save(bookEntity);
        return convertToResDto(updatedBook);
    }

    @Override
    public List<BorrowDto> getAllBorrowedBooks() {

        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        UserEntity user = userRepository.findByName(name)
                .orElseThrow(() -> new CustomServiceException("Username not found: " + name));

        List<BorrowEntity> borrowedBooks = borrowRepository.findAll();
            return borrowedBooks.stream().map(borrowEntity -> {
                BorrowDto borrowDto = new BorrowDto();
                UserResDto userDto = convertToUserResDto(borrowEntity.getUserEntity());
                BookResDto bookDto = convertToResDto(borrowEntity.getBookEntity());
                borrowDto.setUser(userDto);
                borrowDto.setBook(bookDto);
                return borrowDto;
            }).collect(Collectors.toList());
    }

    // Convert BookEntity to BookResDto
    private BookResDto convertToResDto(BookEntity book) {
        return new BookResDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishedYear(),
                book.getAvailableCopies()
        );
    }
    private UserResDto convertToUserResDto(UserEntity userEntity) {
        return new UserResDto(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getRole());
    }

}
