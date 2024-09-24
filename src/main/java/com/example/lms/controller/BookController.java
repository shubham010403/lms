package com.example.lms.controller;

import com.example.lms.dto.BookReqDto;
import com.example.lms.dto.BookResDto;
import com.example.lms.entities.BookEntity;
import com.example.lms.service.IBookService;
import com.example.lms.service.IBorrowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
//@Tag(name = "Book Controller", description = "Book management APIs")
public class BookController {

    @Autowired
    private IBookService bookService;

    // Add a new book
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BookResDto> addBook(@RequestBody BookReqDto book) {
        BookResDto savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    // Retrieve a list of all books
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<BookResDto>> getAllBooks() {
        List<BookResDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Retrieve details of a specific book by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BookResDto> getBookById(@PathVariable Long id) {
        BookResDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // Update book information
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BookResDto> updateBook(@PathVariable Long id, @RequestBody BookReqDto updatedBook) {
        BookResDto book = bookService.updateBook(id, updatedBook);
        return ResponseEntity.ok(book);
    }

    // Delete a book
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        String response =  bookService.deleteBook(id);
        return ResponseEntity.ok(response);
    }

}
