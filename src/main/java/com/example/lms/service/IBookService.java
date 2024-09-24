package com.example.lms.service;

import com.example.lms.dto.BookReqDto;
import com.example.lms.dto.BookResDto;
import com.example.lms.entities.BookEntity;

import java.util.List;

public interface IBookService {
    public BookResDto addBook(BookReqDto book);
    public List<BookResDto> getAllBooks();
    public BookResDto getBookById(Long id);
    public BookResDto updateBook(Long id, BookReqDto updatedBook);
    public String deleteBook(Long id);

}
