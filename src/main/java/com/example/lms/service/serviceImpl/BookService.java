package com.example.lms.service.serviceImpl;

import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.service.IBookService;
import com.example.lms.entities.BookEntity;
import com.example.lms.repository.BookRepository;
import com.example.lms.dto.BookResDto;
import com.example.lms.dto.BookReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {
    @Autowired
    private BookRepository bookRepository;

    // Add a new book
    @Override
    public BookResDto addBook(BookReqDto bookReqDto) {
        BookEntity book = convertToEntity(bookReqDto);
        BookEntity savedBook = bookRepository.save(book);
        return convertToResDto(savedBook);
    }

    // Retrieve a list of all books
    @Override
    public List<BookResDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::convertToResDto)
                .collect(Collectors.toList());
    }

    // Retrieve details of a specific book by ID
    @Override
    public BookResDto getBookById(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToResDto(book);
    }

    // Update book information
    @Override
    public BookResDto updateBook(Long id, BookReqDto updatedBookReqDto) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        book.setTitle(updatedBookReqDto.getTitle());
        book.setAuthor(updatedBookReqDto.getAuthor());
        book.setPublishedYear(updatedBookReqDto.getPublishedYear());
        book.setAvailableCopies(updatedBookReqDto.getAvailableCopies());

        BookEntity updatedBook = bookRepository.save(book);
        return convertToResDto(updatedBook);
    }

    // Delete a book
    @Override
    public String deleteBook(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
        return "Book deleted succesfully";
    }


    // Convert BookReqDto to BookEntity
    private BookEntity convertToEntity(BookReqDto bookReqDto) {
        BookEntity book = new BookEntity();
        book.setTitle(bookReqDto.getTitle());
        book.setAuthor(bookReqDto.getAuthor());
        book.setPublishedYear(bookReqDto.getPublishedYear());
        book.setAvailableCopies(bookReqDto.getAvailableCopies());
        return book;
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
}
