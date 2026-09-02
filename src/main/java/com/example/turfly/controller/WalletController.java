package com.example.turfly.controller;

import com.example.turfly.dto.TopUpRequest;
import com.example.turfly.dto.WalletResponse;
import com.example.turfly.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/topup")
    public ResponseEntity<WalletResponse> topUp(@Valid @RequestBody TopUpRequest request,
                                                Authentication authentication) {
        return ResponseEntity.ok(walletService.topUp(request, authentication.getName()));
    }

    @GetMapping("/balance")
    public ResponseEntity<WalletResponse> getBalance(Authentication authentication) {
        return ResponseEntity.ok(walletService.getBalance(authentication.getName()));
    }
}