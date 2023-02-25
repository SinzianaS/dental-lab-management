package com.app.dentallabmanagement.service;

import com.app.dentallabmanagement.entity.User;
import com.app.dentallabmanagement.repository.UserRepository;
import com.app.dentallabmanagement.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOpt = userRepository.findByUsername(username);   //querying the database using the repository

        return userOpt.orElseThrow(()-> new UsernameNotFoundException("Invalid credentials."));
    }
}
