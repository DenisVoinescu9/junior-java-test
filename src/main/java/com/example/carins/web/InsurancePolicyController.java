package com.example.carins.web;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InsurancePolicyController {

    private final InsurancePolicyService service;

    public InsurancePolicyController(InsurancePolicyService service) {
        this.service = service;
    }
    @GetMapping("/insurance-policies")
    public List<InsurancePolicyDto> getInsurancePolicies() {
        return service.listInsurancePolicies()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private InsurancePolicyDto toDto(InsurancePolicy policy) {
        var car = policy.getCar();
        var owner = car != null ? car.getOwner() : null;

        return new InsurancePolicyDto(
                policy.getId(),
                car != null ? car.getId() : null,
                car != null ? car.getVin() : null,
                car != null ? car.getMake() : null,
                car != null ? car.getModel() : null,
                car != null ? car.getYearOfManufacture() : null,
                owner != null ? owner.getId() : null,
                owner != null ? owner.getName() : null,
                owner != null ? owner.getEmail() : null,
                policy.getProvider(),
                policy.getStartDate(),
                policy.getEndDate()
        );
    }


    @PostMapping("/insurance-policies")
    public InsurancePolicy createInsurancePolicy(@RequestBody InsurancePolicy insurancePolicy) {
        return service.createInsurancePolicy(insurancePolicy);
    }

    @PutMapping("/insurance-policies/{id}")
    public InsurancePolicyDto updateInsurancePolicy(
            @PathVariable Long id,
            @RequestBody InsurancePolicyDto dto) {

        InsurancePolicy updated = service.updateInsurancePolicyFromDto(id, dto);
        return toDto(updated);
    }



}
