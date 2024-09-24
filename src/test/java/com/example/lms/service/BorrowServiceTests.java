package com.example.lms.service;

import com.example.lms.dto.BookResDto;
import com.example.lms.dto.BorrowDto;
import com.example.lms.entities.BookEntity;
import com.example.lms.entities.BorrowEntity;
import com.example.lms.entities.UserEntity;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.serviceImpl.BorrowService;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.awt.print.Book;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BorrowServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BorrowService borrowService;

    private BookEntity bookEntity;
    private UserEntity userEntity;
    private BorrowEntity borrowEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContext and Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock Authentication object to return the user name
        when(authentication.getName()).thenReturn("testUser");

        // Create mock BookEntity
        bookEntity = new BookEntity();
        bookEntity.setId(1L); // Ensure this matches the ID in your test
        bookEntity.setTitle("Test Book");
        bookEntity.setAuthor("Test Author");
        bookEntity.setPublishedYear(2020);
        bookEntity.setAvailableCopies(5); // Set initial available copies

        // Create mock UserEntity
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("testUser");
        userEntity.setEmail("test@example.com");
        userEntity.setRole("ROLE_USER");
        userEntity.setPassword("123");

        // Create mock BorrowEntity
        borrowEntity = new BorrowEntity();
        borrowEntity.setBookEntity(bookEntity);
        borrowEntity.setUserEntity(userEntity);
    }
    @Test
    void testBorrowBook_Success() {
        // Mock authenticated user
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(userEntity));

        // Mock book repository
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));

        // Mock the saving of the borrow record
        when(borrowRepository.save(Mockito.<BorrowEntity>any())).thenReturn(borrowEntity);

        // Mock the saving of the updated book (return the book passed in)
        when(bookRepository.save(Mockito.<BookEntity>any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BookResDto response = borrowService.borrowBook(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        assertEquals(4, response.getAvailableCopies()); // Copies decreased by 1

        // Verify that the bookRepository and borrowRepository save methods were called
        verify(bookRepository, times(1)).save(bookEntity);
        verify(borrowRepository, times(1)).save(Mockito.<BorrowEntity>any());
    }

    @Test
    void testBorrowBook_NoAvailableCopies() {
        // Set no available copies
        bookEntity.setAvailableCopies(0);

        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(userEntity));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> borrowService.borrowBook(1L));

        // Verify that the borrowRepository save method was never called
        verify(borrowRepository, never()).save(Mockito.<BorrowEntity>any());
    }

    @Test
    void testReturnBook_Success() {
        // Arrange
        String testUsername = "testUser";

        // Mock authenticated user
        when(authentication.getName()).thenReturn(testUsername);
        when(userRepository.findByName(testUsername)).thenReturn(Optional.of(userEntity));

        // Mock book repository
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookRepository.save(Mockito.<BookEntity>any())).thenReturn(bookEntity);

        // Mock borrow repository
        when(borrowRepository.findByUserEntityIdAndBookEntityId(userEntity.getId(), bookEntity.getId()))
                .thenReturn(Optional.of(borrowEntity));

        // Act
        BookResDto response = borrowService.returnBook(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        assertEquals(6, response.getAvailableCopies()); // Copies increased by 1

        // Verify that the borrowRepository delete method was called
        verify(borrowRepository, times(1)).delete(borrowEntity);
        verify(bookRepository, times(1)).save(bookEntity);
    }

    @Test
    void testReturnBook_BorrowRecordNotFound() {
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(userEntity));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));

        // Borrow record not found
        when(borrowRepository.findByUserEntityIdAndBookEntityId(userEntity.getId(), bookEntity.getId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> borrowService.returnBook(1L));

        // Verify that the borrowRepository delete method was never called
        verify(borrowRepository, never()).delete(Mockito.<BorrowEntity>any());
    }

    @Test
    void testGetAllBorrowedBooks_Success() {
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(userEntity));

        // Mock findAll to return a list with one borrow record
        when(borrowRepository.findAll()).thenReturn(List.of(borrowEntity));

        // Act
        List<BorrowDto> response = borrowService.getAllBorrowedBooks();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test Book", response.get(0).getBook().getTitle());
        assertEquals("testUser", response.get(0).getUser().getName());
    }
}
