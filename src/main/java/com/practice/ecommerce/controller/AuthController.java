package com.practice.ecommerce.controller;

import com.practice.ecommerce.model.AppRole;
import com.practice.ecommerce.model.Role;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repositories.RoleRepository;
import com.practice.ecommerce.repositories.UserRepository;
import com.practice.ecommerce.security.jwt.*;
import com.practice.ecommerce.security.jwt.service.UserDetailsImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("Message", "Bad Credentials");
            map.put("Status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), roles, jwtToken);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUserName(signupRequest.getUserName())){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }if(userRepository.existsByEmail(signupRequest.getUserName())){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already taken"));
        }
        User user = new User(
                signupRequest.getUserName(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        Set<String> roles = signupRequest.getRole();
        Set<Role> enumRoles = new HashSet<>();

        if(roles == null){
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).
                    orElseThrow(
                            () -> new RuntimeException("Error: Role is Not Found")
                    );
            enumRoles.add(userRole);
        }else {
            roles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).
                                orElseThrow(
                                        () -> new RuntimeException("Error: Role is Not Found")
                                );
                        enumRoles.add(adminRole);

                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).
                                orElseThrow(
                                        () -> new RuntimeException("Error: Role is Not Found")
                                );
                        enumRoles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).
                                orElseThrow(
                                        () -> new RuntimeException("Error: Role is Not Found")
                                );
                        enumRoles.add(userRole);
                }
            });
        }
        user.setRoles(enumRoles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registered Successfully"));
    }
}
