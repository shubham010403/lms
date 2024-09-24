package com.example.lms.service.serviceImpl;

import com.example.lms.dto.AuthReqDto;
import com.example.lms.dto.UserReqDto;
import com.example.lms.entities.UserEntity;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String authenticateAndGetToken(AuthReqDto authRequestDto) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
        );

        // Check if authentication was successful
        if (authentication.isAuthenticated()) {
            // Generate JWT token for the authenticated user
            String token = jwtService.generateToken(authRequestDto.getUsername());
            return token;
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @Override
    public String addUser(UserReqDto userDto){

        UserEntity user = new UserEntity();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User created";
    }
}
