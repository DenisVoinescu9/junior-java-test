package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.service.CarService;
import com.example.carins.service.ClaimService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.ClaimDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;
    private final ClaimService claimService;

    public CarController(CarService service, ClaimService claimService) {
        this.carService = service;
        this.claimService = claimService;
    }

    @GetMapping()
    public List<CarDto> getCars() {
        return carService.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        // TODO: validate date format and handle errors consistently
        LocalDate d = LocalDate.parse(date);
        boolean valid = carService.isInsuranceValid(carId, d);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    @PostMapping("/{carId}/claims")
    public ResponseEntity<ClaimDto> registerClaim(
            @PathVariable Long carId,
            @RequestBody ClaimDto dto) {
        Claim claim = new Claim();
        claim.setClaimDate(dto.claimDate());
        claim.setDescription(dto.description());
        claim.setAmount(dto.amount());
        Claim saved = claimService.createClaim(carId, claim);
        ClaimDto responseDto = new ClaimDto(saved.getId(), saved.getClaimDate(), saved.getDescription(), saved.getAmount());
        URI location = URI.create(String.format("/api/cars/%d/claims/%d", carId, saved.getId()));
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{carId}/history")
    public List<ClaimDto> getCarHistory(@PathVariable Long carId) {
        return claimService.getCarHistory(carId)
                .stream()
                .map(c -> new ClaimDto(c.getId(), c.getClaimDate(), c.getDescription(), c.getAmount()))
                .collect(Collectors.toList());
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
