package com.example.turfly.service;

import com.example.turfly.dto.BookingRequest;
import com.example.turfly.entity.*;
import com.example.turfly.repository.BookingRepository;
import com.example.turfly.repository.TurfRepository;
import com.example.turfly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TurfRepository turfRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    private User customer;
    private User owner;
    private Turf turf;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setEmail("joel@test.com");
        owner.setName("Joel");
        owner.setRole(Role.OWNER);

        customer = new User();
        customer.setId(2L);
        customer.setEmail("steve@test.com");
        customer.setName("Steve");
        customer.setRole(Role.CUSTOMER);
        customer.setWalletBalance(5000.0);

        turf = new Turf();
        turf.setId(1L);
        turf.setName("Green Field Arena");
        turf.setPrice(800.0);
        turf.setOwner(owner);
    }

    @Test
    void createBooking_calculatesCorrectAmount_andSucceeds() {
        BookingRequest request = new BookingRequest();
        request.setTurfId(1L);
        request.setDate(LocalDate.of(2026, 9, 20));
        request.setStartTime(LocalTime.of(18, 0));
        request.setEndTime(LocalTime.of(19, 0));

        when(userRepository.findByEmail("steve@test.com")).thenReturn(Optional.of(customer));
        when(turfRepository.findById(1L)).thenReturn(Optional.of(turf));
        when(bookingRepository.existsOverlappingBooking(any(), any(), any(), any())).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = bookingService.createBooking(request, "steve@test.com");

        assertEquals(800.0, response.getAmount());
        assertEquals(BookingStatus.PENDING, response.getStatus());
    }

    @Test
    void createBooking_overlappingSlot_throwsIllegalStateException() {
        BookingRequest request = new BookingRequest();
        request.setTurfId(1L);
        request.setDate(LocalDate.of(2026, 9, 20));
        request.setStartTime(LocalTime.of(18, 30));
        request.setEndTime(LocalTime.of(19, 30));

        when(userRepository.findByEmail("steve@test.com")).thenReturn(Optional.of(customer));
        when(turfRepository.findById(1L)).thenReturn(Optional.of(turf));
        when(bookingRepository.existsOverlappingBooking(any(), any(), any(), any())).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> bookingService.createBooking(request, "steve@test.com"));
    }

    @Test
    void approveBooking_insufficientWalletBalance_throwsIllegalStateException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTurf(turf);
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.PENDING);
        booking.setAmount(10000.0); // more than customer's 5000 balance

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(IllegalStateException.class,
                () -> bookingService.approveBooking(1L, "joel@test.com"));
    }

    @Test
    void approveBooking_alreadyApproved_throwsIllegalStateException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTurf(turf);
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.APPROVED); // already approved
        booking.setAmount(800.0);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(IllegalStateException.class,
                () -> bookingService.approveBooking(1L, "joel@test.com"));
    }

    @Test
    void rejectBooking_byNonOwner_throwsAccessDeniedException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setTurf(turf); // owned by joel@test.com
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.PENDING);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.rejectBooking(1L, "someoneelse@test.com"));
    }
}