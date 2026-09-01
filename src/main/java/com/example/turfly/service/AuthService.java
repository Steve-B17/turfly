package com.example.turfly.service;

import com.example.turfly.dto.LoginRequest;
import com.example.turfly.dto.RegisterRequest;
import com.example.turfly.entity.User;
import com.example.turfly.repository.UserRepository;
import com.example.turfly.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setWalletBalance(0.0);

        userRepository.save(user);

        return jwtUtil.generateToken(user.getEmail());
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        return jwtUtil.generateToken(request.getEmail());
    }
}