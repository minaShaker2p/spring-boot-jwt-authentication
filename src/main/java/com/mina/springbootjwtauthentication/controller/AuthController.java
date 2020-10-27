package com.mina.springbootjwtauthentication.controller;

import com.mina.springbootjwtauthentication.payload.request.LoginRequest;
import com.mina.springbootjwtauthentication.payload.request.SignupRequest;
import com.mina.springbootjwtauthentication.payload.response.LoginResponse;
import com.mina.springbootjwtauthentication.payload.response.MessageResponse;
import com.mina.springbootjwtauthentication.security.UserDetailsImpl;
import com.mina.springbootjwtauthentication.security.jwt.JwtUtils;
import com.mina.springbootjwtauthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        var userResponse = userService.createNewUser(signupRequest);
        switch (userResponse.getCode()) {
            case 400: {
                return ResponseEntity.badRequest().body(new MessageResponse(userResponse.getMessage()));
            }
            case 200: {
                return ResponseEntity.ok(new MessageResponse(userResponse.getMessage()));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse(userResponse.getMessage()));
    }


}
