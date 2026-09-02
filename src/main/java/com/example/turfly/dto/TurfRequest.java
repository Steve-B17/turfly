package com.example.turfly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TurfRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String sportType;

    @NotNull
    @Positive
    private Double price;
}