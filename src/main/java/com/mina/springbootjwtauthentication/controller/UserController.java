package com.mina.springbootjwtauthentication.controller;

import com.mina.springbootjwtauthentication.payload.response.UserResponse;
import com.mina.springbootjwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.StreamSupport;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {

        var users = userRepository.findAll().spliterator();

        return ResponseEntity.ok(StreamSupport.stream(users, false).map(user -> {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
        }));
    }

}
