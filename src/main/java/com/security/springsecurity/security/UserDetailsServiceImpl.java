package com.security.springsecurity.security;


import com.security.springsecurity.daoauth.UserRepository;
import com.security.springsecurity.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailsServiceImpl===loadUserByUsername");
        System.out.println("UserDetailsServiceImpl===username=="+username);
        Optional<AuthUser> userOptional=repository.findByEmail(username);
        System.out.println("UserDetailsServiceImpl===repository.findByEmail");
        AuthUser userfromdb=userOptional.get();
        System.out.println("userfromdb.getEmail()"+userfromdb.getEmail());
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
