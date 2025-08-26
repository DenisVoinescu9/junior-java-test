package com.example.carins.config;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class InsurancePolicyEndDateFixer {

    private final InsurancePolicyRepository repository;

    public InsurancePolicyEndDateFixer(InsurancePolicyRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void fixPolicies() {
        List<InsurancePolicy> policies = repository.findAll();
        for (InsurancePolicy policy : policies) {
            if (policy.getEndDate() == null && policy.getStartDate() != null) {
                policy.setEndDate(policy.getStartDate().plusYears(1));
            }
        }
        repository.saveAll(policies);
    }
}
