package com.example.turfly.repository;

import com.example.turfly.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByTurfOwnerId(Long ownerId);
}