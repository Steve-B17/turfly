package com.example.turfly.service;

import com.example.turfly.dto.TopUpRequest;
import com.example.turfly.dto.WalletResponse;
import com.example.turfly.entity.User;
import com.example.turfly.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final UserRepository userRepository;

    public WalletService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public WalletResponse topUp(TopUpRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setWalletBalance(user.getWalletBalance() + request.getAmount());

        return new WalletResponse(user.getWalletBalance());
    }

    @Transactional(readOnly = true)
    public WalletResponse getBalance(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new WalletResponse(user.getWalletBalance());
    }
}