package com.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDto {
    @NotBlank(message = "username cannot be blank")
    private String name;
    private String email;
    private String password;
    private String role;
}
