package com.appointmentbooking.config;

import com.appointmentbooking.application.reservation.ExpirePendingReservationsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationSchedulingConfig {
    private final ExpirePendingReservationsUseCase expiry;
    public ReservationSchedulingConfig(ExpirePendingReservationsUseCase expiry) { this.expiry = expiry; }
    @Scheduled(fixedDelayString = "${appointment.reservation-expiry-interval-ms:60000}")
    public void expireReservations() { expiry.expireDueReservations(); }
}