package com.mina.springbootjwtauthentication.controller;

import com.mina.springbootjwtauthentication.payload.response.MessageResponse;
import com.mina.springbootjwtauthentication.payload.response.UserResponse;
import com.mina.springbootjwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("User Not Found!!"), HttpStatus.NOT_FOUND);
        } else {
            var userResponse = new UserResponse(user.get().getId(), user.get().getUsername(), user.get().getEmail(), user.get().getRoles());
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> searchByUsername(@PathVariable("username") String username) {

        var user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("User Not Found!!"), HttpStatus.NOT_FOUND);
        } else {
            var userResponse = new UserResponse(user.get().getId(), user.get().getUsername(), user.get().getEmail(), user.get().getRoles());
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
    }
}
