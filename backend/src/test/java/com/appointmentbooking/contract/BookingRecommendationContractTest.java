package com.appointmentbooking.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.appointmentbooking.application.ApiException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class BookingRecommendationContractTest {
    @Test void unavailableSlotContractUsesConflictStatusAndCode() {
        var exception = new ApiException(HttpStatus.CONFLICT, "SLOT_UNAVAILABLE", "Unavailable", Map.of("alternatives", java.util.List.of()));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus()); assertEquals("SLOT_UNAVAILABLE", exception.getCode());
    }
}