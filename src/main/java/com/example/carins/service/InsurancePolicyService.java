package com.example.carins.service;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class InsurancePolicyService {

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final CarRepository carRepository;

    public InsurancePolicyService(InsurancePolicyRepository insurancePolicyRepository, CarRepository carRepository) {
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.carRepository = carRepository;
    }

    public List<InsurancePolicy> listInsurancePolicies() {
        return insurancePolicyRepository.findAll();
    }

    public InsurancePolicy createInsurancePolicy(InsurancePolicy insurancePolicy) {
        validateInsurancePolicy(insurancePolicy.getEndDate());
        return insurancePolicyRepository.save(insurancePolicy);
    }
    private void validateInsurancePolicy(LocalDate date) {
        if (date == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date is required and cannot be null");
        }
    }

    public InsurancePolicy updateInsurancePolicyFromDto(Long id, InsurancePolicyDto insurancePolicy) {
        validateInsurancePolicy(insurancePolicy.endDate());
        InsurancePolicy existingPolicy = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance policy not found"));

        if (insurancePolicy.carId() != null) {
            var existingCar = carRepository.findById(insurancePolicy.carId())
                    .orElseThrow(() -> new RuntimeException("Car not found"));
            existingPolicy.setCar(existingCar);
        }

        existingPolicy.setProvider(insurancePolicy.provider());
        existingPolicy.setStartDate(insurancePolicy.startDate());
        existingPolicy.setEndDate(insurancePolicy.endDate());

        return insurancePolicyRepository.save(existingPolicy);
    }
}
