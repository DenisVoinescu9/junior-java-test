package com.example.carins.config;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PolicyExpiryLogger {

    private final InsurancePolicyRepository repository;
    private final Logger logger = LoggerFactory.getLogger(PolicyExpiryLogger.class);
    private final Set<Long> loggedPolicyIds = new HashSet<>();

    public PolicyExpiryLogger(InsurancePolicyRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 10000)
    public void logTodayExpiredPolicies() {
        LocalDate today = LocalDate.now();
        List<InsurancePolicy> policies = repository.findAll();

        for (InsurancePolicy policy : policies) {
            if (policy.getEndDate() == null) continue;

            if (policy.getEndDate().isEqual(today) && !loggedPolicyIds.contains(policy.getId())) {
                logger.info(
                        "Policy " + policy.getId() +
                                " for car " + policy.getCar().getId() +
                                " expired on " + policy.getEndDate()
                );
                loggedPolicyIds.add(policy.getId());
            }
        }
    }
}
