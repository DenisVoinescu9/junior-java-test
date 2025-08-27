package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final CarRepository carRepository;

    public ClaimService(ClaimRepository claimRepository, CarRepository carRepository) {
        this.claimRepository = claimRepository;
        this.carRepository = carRepository;
    }

    public Claim createClaim(Long carId, Claim claim) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        if (claim.getClaimDate() == null || claim.getDescription() == null || claim.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Claim fields are required");
        }
        claim.setCar(car);
        return claimRepository.save(claim);
    }

    public List<Claim> getCarHistory(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        return claimRepository.findByCarOrderByClaimDateAsc(car);
    }
}
