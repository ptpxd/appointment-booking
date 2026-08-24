package com.appointmentbooking.application.reservation;

import com.appointmentbooking.adapter.out.persistence.ConfirmationTokenJpaRepository;
import com.appointmentbooking.application.ApiException;
import com.appointmentbooking.application.confirmation.ConfirmationTokenService;
import com.appointmentbooking.domain.reservation.Reservation;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmReservationUseCase {
    private final ConfirmationTokenJpaRepository tokens;
    private final ConfirmationTokenService tokenService;
    public ConfirmReservationUseCase(ConfirmationTokenJpaRepository tokens, ConfirmationTokenService tokenService) { this.tokens = tokens; this.tokenService = tokenService; }
    @Transactional
    public Reservation confirm(String rawToken) {
        var token = tokens.findByTokenHashForUpdate(tokenService.hash(rawToken))
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "CONFIRMATION_INVALID", "The confirmation token is invalid."));
        if (token.getUsedAt() != null) throw new ApiException(HttpStatus.BAD_REQUEST, "CONFIRMATION_ALREADY_USED", "The confirmation token has already been used.");
        if (!token.getExpiresAt().isAfter(Instant.now())) throw new ApiException(HttpStatus.BAD_REQUEST, "CONFIRMATION_EXPIRED", "The confirmation token has expired.");
        Reservation reservation = token.getReservation();
        if (reservation.getStatus() != com.appointmentbooking.domain.reservation.ReservationStatus.PENDING_CONFIRMATION)
            throw new ApiException(HttpStatus.BAD_REQUEST, "CONFIRMATION_INVALID", "The reservation cannot be confirmed.");
        reservation.confirm(Instant.now());
        reservation.getSlot().confirm();
        token.markUsed(Instant.now());
        return reservation;
    }
}