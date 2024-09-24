package com.example.lms.service;

import com.example.lms.dto.BookReqDto;
import com.example.lms.dto.BookResDto;
import com.example.lms.entities.BookEntity;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.service.serviceImpl.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    @Autowired
    private BookService bookService;

    private BookEntity bookEntity;
    private BookReqDto bookReqDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setTitle("Test Book");
        bookEntity.setAuthor("Test Author");
        bookEntity.setPublishedYear(2020);
        bookEntity.setAvailableCopies(5);

        bookReqDto = new BookReqDto();
        bookReqDto.setTitle("New Test Book");
        bookReqDto.setAuthor("New Test Author");
        bookReqDto.setPublishedYear(2021);
        bookReqDto.setAvailableCopies(3);
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(Mockito.<BookEntity>any())).thenReturn(bookEntity);
        BookResDto response = bookService.addBook(bookReqDto);

        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        assertEquals("Test Author", response.getAuthor());
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(bookEntity));

        List<BookResDto> response = bookService.getAllBooks();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test Book", response.get(0).getTitle());
    }

    @Test
    void testGetBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));

        BookResDto response = bookService.getBookById(1L);

        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        assertEquals("Test Author", response.getAuthor());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });
    }

    @Test
    void testUpdateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);

        BookResDto response = bookService.updateBook(1L, bookReqDto);

        assertNotNull(response);
        assertEquals("New Test Book", response.getTitle());
        assertEquals("New Test Author", response.getAuthor());
        assertEquals(2021, response.getPublishedYear());
        assertEquals(3, response.getAvailableCopies());
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, bookReqDto);
        });
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        doNothing().when(bookRepository).delete(Mockito.<BookEntity>any());

        String response = bookService.deleteBook(1L);

        assertEquals("Book deleted succesfully", response);
        verify(bookRepository, times(1)).delete(bookEntity);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });
    }
}