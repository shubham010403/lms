package com.example.lms.controller;

import com.example.lms.dto.BookResDto;
import com.example.lms.dto.BorrowDto;
import com.example.lms.service.IBorrowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrow-books")
//@Tag(name = "Borrowing Controller", description = "Borrow management APIs")
public class BorrowController {

    @Autowired
    private IBorrowService borrowService;

    // Borrow a book (decrease available copies)
    @GetMapping("/{id}/borrow")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BookResDto> borrowBook(@PathVariable Long id) {
        BookResDto borrowedBook = borrowService.borrowBook(id);
        return ResponseEntity.ok(borrowedBook);
    }

    // Return a book (increase available copies)
    @GetMapping("/{id}/return")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BookResDto> returnBook(@PathVariable Long id) {
        BookResDto returnedBook = borrowService.returnBook(id);
        return ResponseEntity.ok(returnedBook);
    }

    @GetMapping("/borrowed")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<BorrowDto>> getBorrowedBooks(){
        List<BorrowDto> borrowDtos = borrowService.getAllBorrowedBooks();
        return ResponseEntity.ok(borrowDtos);
    }

}
