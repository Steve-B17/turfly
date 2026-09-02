package com.example.turfly.dto;

import com.example.turfly.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String turfName;
    private String customerName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private Double amount;
}