package com.example.lms.service.serviceImpl;

import com.example.lms.entities.UserEntity;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByName(username);
        UserEntity user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return user;
    }
}