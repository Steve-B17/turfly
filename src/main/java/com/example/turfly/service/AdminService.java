package com.example.turfly.service;

import com.example.turfly.dto.BookingResponse;
import com.example.turfly.dto.TurfResponse;
import com.example.turfly.dto.UserResponse;
import com.example.turfly.entity.Booking;
import com.example.turfly.entity.Turf;
import com.example.turfly.entity.User;
import com.example.turfly.repository.BookingRepository;
import com.example.turfly.repository.TurfRepository;
import com.example.turfly.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final TurfRepository turfRepository;
    private final BookingRepository bookingRepository;

    public AdminService(UserRepository userRepository, TurfRepository turfRepository,
                        BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.turfRepository = turfRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getWalletBalance()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TurfResponse> getAllTurfs() {
        return turfRepository.findAll().stream()
                .map(t -> new TurfResponse(t.getId(), t.getName(), t.getAddress(), t.getSportType(),
                        t.getPrice(), t.getOwner().getName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> new BookingResponse(b.getId(), b.getTurf().getName(), b.getCustomer().getName(),
                        b.getDate(), b.getStartTime(), b.getEndTime(), b.getStatus(), b.getAmount()))
                .toList();
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
}