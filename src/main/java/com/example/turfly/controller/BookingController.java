package com.example.turfly.controller;

import com.example.turfly.dto.BookingRequest;
import com.example.turfly.dto.BookingResponse;
import com.example.turfly.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request,
                                                         Authentication authentication) {
        return ResponseEntity.ok(bookingService.createBooking(request, authentication.getName()));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getMyBookings(authentication.getName()));
    }

    @GetMapping("/owner-bookings")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<BookingResponse>> getOwnerBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getBookingsForMyTurfs(authentication.getName()));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BookingResponse> approveBooking(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(bookingService.approveBooking(id, authentication.getName()));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<BookingResponse> rejectBooking(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(bookingService.rejectBooking(id, authentication.getName()));
    }
}