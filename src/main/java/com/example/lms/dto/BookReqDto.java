package com.example.lms.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookReqDto {
    private String title;
    private String author;
    private Integer publishedYear;
    private int availableCopies;
}
