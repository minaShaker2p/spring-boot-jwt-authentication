package com.mina.springbootjwtauthentication.service;

import com.mina.springbootjwtauthentication.model.ERole;
import com.mina.springbootjwtauthentication.model.Role;
import com.mina.springbootjwtauthentication.model.User;
import com.mina.springbootjwtauthentication.payload.request.SignupRequest;
import com.mina.springbootjwtauthentication.payload.response.Response;
import com.mina.springbootjwtauthentication.repository.RoleRepository;
import com.mina.springbootjwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Response createNewUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new Response(HttpStatus.BAD_REQUEST.value(), "Error username is already taken");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new Response(HttpStatus.BAD_REQUEST.value(), "Error: Email is already in use");
        }
        // create new user account
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return new Response(HttpStatus.OK.value(),"User registered successfully!");
    }
}
