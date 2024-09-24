package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private Long id;
    private String name;
    private String email;
    private String role;
}
