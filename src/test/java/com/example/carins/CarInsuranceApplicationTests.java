package com.example.carins;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarInsuranceApplicationTests {

    @Autowired
    CarService service;

    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        assertFalse(service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));
    }

    @Test
    void carValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertThrows(ResponseStatusException.class, () -> service.isInsuranceValid(12344321L, LocalDate.parse("2025-02-01")));
    }

    @Test
    void dateValidityBasic() {
        assertThrows(DateTimeParseException.class, () -> service.isInsuranceValid(1L, LocalDate.parse("2025-13-01")));
        assertThrows(DateTimeParseException.class, () -> service.isInsuranceValid(2L, LocalDate.parse("2025-02-011")));
    }
}
