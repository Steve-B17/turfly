package com.example.turfly.controller;

import com.example.turfly.dto.TurfRequest;
import com.example.turfly.dto.TurfResponse;
import com.example.turfly.service.TurfService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turfs")
public class TurfController {

    private final TurfService turfService;

    public TurfController(TurfService turfService) {
        this.turfService = turfService;
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<TurfResponse> createTurf(@Valid @RequestBody TurfRequest request,
                                                   Authentication authentication) {
        TurfResponse response = turfService.createTurf(request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TurfResponse>> getAllTurfs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(turfService.getAllTurfsPaged(pageable));
    }

    @GetMapping("/my-turfs")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<TurfResponse>> getMyTurfs(Authentication authentication) {
        return ResponseEntity.ok(turfService.getMyTurfs(authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteTurf(@PathVariable Long id, Authentication authentication) {
        turfService.deleteTurf(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}