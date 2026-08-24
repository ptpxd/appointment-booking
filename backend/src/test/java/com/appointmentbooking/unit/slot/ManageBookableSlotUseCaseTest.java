package com.appointmentbooking.unit.slot;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.appointmentbooking.support.AppointmentFixtures;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ManageBookableSlotUseCaseTest {
    @Test void slotRejectsAnInvalidInterval() {
        assertThrows(IllegalArgumentException.class, () -> new com.appointmentbooking.domain.slot.BookableSlot(AppointmentFixtures.provider(), Instant.now().plusSeconds(60), Instant.now()));
    }
}