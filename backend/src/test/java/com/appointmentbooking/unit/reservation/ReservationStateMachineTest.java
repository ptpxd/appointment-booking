package com.appointmentbooking.unit.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.appointmentbooking.domain.reservation.Reservation;
import com.appointmentbooking.domain.reservation.ReservationStatus;
import com.appointmentbooking.domain.slot.SlotStatus;
import com.appointmentbooking.support.AppointmentFixtures;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ReservationStateMachineTest {
    @Test void pendingReservationCanBeConfirmedOnce() {
        var slot = AppointmentFixtures.futureSlot(AppointmentFixtures.provider()); slot.reservePending();
        var reservation = new Reservation(slot, "guest@example.com", Instant.now().plusSeconds(900));
        reservation.confirm(Instant.now()); slot.confirm();
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals(SlotStatus.BOOKED, slot.getStatus());
    }
}