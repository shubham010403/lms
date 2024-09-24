package com.example.lms.service;

import com.example.lms.dto.BookResDto;
import com.example.lms.dto.BorrowDto;

import java.util.List;

public interface IBorrowService {
    public BookResDto borrowBook(Long id);
    public BookResDto returnBook(Long id);
    public List<BorrowDto> getAllBorrowedBooks();
}
