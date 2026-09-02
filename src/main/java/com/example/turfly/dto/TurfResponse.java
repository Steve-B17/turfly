package com.example.turfly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TurfResponse {
    private Long id;
    private String name;
    private String address;
    private String sportType;
    private Double price;
    private String ownerName;
}