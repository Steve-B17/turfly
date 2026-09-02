package com.example.turfly.repository;

import com.example.turfly.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByTurfOwnerId(Long ownerId);

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.turf.id = :turfId
        AND b.date = :date
        AND b.status IN (com.example.turfly.entity.BookingStatus.PENDING, com.example.turfly.entity.BookingStatus.APPROVED)
        AND b.startTime < :endTime
        AND b.endTime > :startTime
        """)
    boolean existsOverlappingBooking(Long turfId, LocalDate date, LocalTime startTime, LocalTime endTime);
}