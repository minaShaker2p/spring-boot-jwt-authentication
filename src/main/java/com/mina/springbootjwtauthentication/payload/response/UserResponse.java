package com.mina.springbootjwtauthentication.payload.response;

import com.mina.springbootjwtauthentication.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private Set<Role> roles = new HashSet<>();

    public UserResponse(
            Long id,
            String username,
            String email,
            Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
