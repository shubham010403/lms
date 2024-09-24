package com.example.lms.service;

import com.example.lms.dto.AuthReqDto;
import com.example.lms.dto.UserReqDto;

public interface IAuthService {
    String addUser(UserReqDto user);
    String authenticateAndGetToken(AuthReqDto authRequestDto);
}