package com.example.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="borrow_record")
public class BorrowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private UserEntity userEntity;

    @OneToOne
    @JoinColumn(name="book_id",nullable = false)
    private BookEntity bookEntity;
}
