package com.practice.ecommerce.security.jwt.service;

import com.practice.ecommerce.repositories.UserRepository;
import com.practice.ecommerce.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(
                        ()-> new UsernameNotFoundException("User Not Found With UserName: " + username)
                );
        return UserDetailsImpl.build(user);
    }
}
