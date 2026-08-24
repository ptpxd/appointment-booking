package com.appointmentbooking.application.reservation;

import com.appointmentbooking.adapter.out.persistence.BookableSlotJpaRepository;
import com.appointmentbooking.adapter.out.persistence.ReservationJpaRepository;
import com.appointmentbooking.domain.reservation.ReservationStatus;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpirePendingReservationsUseCase {
    private final ReservationJpaRepository reservations;
    private final BookableSlotJpaRepository slots;
    public ExpirePendingReservationsUseCase(ReservationJpaRepository reservations, BookableSlotJpaRepository slots) { this.reservations = reservations; this.slots = slots; }
    @Transactional
    public int expireDueReservations() {
        int[] count = {0};
        reservations.findByStatusAndExpiresAtLessThanEqual(ReservationStatus.PENDING_CONFIRMATION, Instant.now()).forEach(reservation ->
            slots.findByIdForUpdate(reservation.getSlot().getId()).ifPresent(slot -> {
                if (reservation.getStatus() == ReservationStatus.PENDING_CONFIRMATION && !reservation.getExpiresAt().isAfter(Instant.now())) {
                    reservation.expire(); slot.release(); count[0]++;
                }
            }));
        return count[0];
    }
}