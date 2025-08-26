package com.example.carins.web.dto;

import java.time.LocalDate;

public record InsurancePolicyDto(
        Long id,
        Long carId,
        String vin,
        String make,
        String model,
        Integer yearOfManufacture,
        Long ownerId,
        String ownerName,
        String ownerEmail,
        String provider,
        LocalDate startDate,
        LocalDate endDate
) {}
