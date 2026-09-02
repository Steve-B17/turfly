package com.example.turfly.service;

import com.example.turfly.dto.BookingRequest;
import com.example.turfly.dto.BookingResponse;
import com.example.turfly.entity.*;
import com.example.turfly.repository.BookingRepository;
import com.example.turfly.repository.TurfRepository;
import com.example.turfly.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TurfRepository turfRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, TurfRepository turfRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.turfRepository = turfRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Turf turf = turfRepository.findById(request.getTurfId())
                .orElseThrow(() -> new IllegalArgumentException("Turf not found"));

        double hours = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0;
        if (hours <= 0) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        double amount = hours * turf.getPrice();

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTurf(turf);
        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus(BookingStatus.PENDING);
        booking.setAmount(amount);

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return bookingRepository.findByCustomerId(customer.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsForMyTurfs(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        return bookingRepository.findByTurfOwnerId(owner.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BookingResponse approveBooking(Long bookingId, String ownerEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getTurf().getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessDeniedException("Not your turf");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not pending");
        }

        User customer = booking.getCustomer();
        if (customer.getWalletBalance() < booking.getAmount()) {
            throw new IllegalStateException("Customer has insufficient wallet balance");
        }

        customer.setWalletBalance(customer.getWalletBalance() - booking.getAmount());
        booking.setStatus(BookingStatus.APPROVED);

        return toResponse(booking);
    }

    @Transactional
    public BookingResponse rejectBooking(Long bookingId, String ownerEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getTurf().getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessDeniedException("Not your turf");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not pending");
        }

        booking.setStatus(BookingStatus.REJECTED);
        return toResponse(booking);
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getTurf().getName(),
                booking.getCustomer().getName(),
                booking.getDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getAmount()
        );
    }
}