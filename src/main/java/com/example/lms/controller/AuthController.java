package com.example.lms.controller;

import com.example.lms.dto.AuthReqDto;
import com.example.lms.dto.UserReqDto;
import com.example.lms.service.IAuthService;
import com.example.lms.service.IBookService;
import com.example.lms.service.serviceImpl.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
//@Tag(name = "User Controller", description = "Authorization management APIs")
public class AuthController {
    @Autowired
    private IBookService carService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserReqDto user){
        String responseModel;
        responseModel = authService.addUser(user);
        return responseModel;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthReqDto authRequestDto) {
        String token = authService.authenticateAndGetToken(authRequestDto);
        return ResponseEntity.ok(token);
    }

}
