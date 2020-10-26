package com.mina.springbootjwtauthentication.service;

import com.mina.springbootjwtauthentication.model.User;
import com.mina.springbootjwtauthentication.repository.UserRepository;
import com.mina.springbootjwtauthentication.security.UserDetailsImpl;
import com.mina.springbootjwtauthentication.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found with username"));
        return UserDetailsImpl.build(user);
    }
}
