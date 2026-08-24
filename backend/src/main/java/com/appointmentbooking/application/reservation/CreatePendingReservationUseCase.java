package com.appointmentbooking.application.reservation;

import com.appointmentbooking.adapter.out.persistence.BookableSlotJpaRepository;
import com.appointmentbooking.adapter.out.persistence.ConfirmationTokenJpaRepository;
import com.appointmentbooking.adapter.out.persistence.ReservationJpaRepository;
import com.appointmentbooking.application.ApiException;
import com.appointmentbooking.application.confirmation.ConfirmationTokenService;
import com.appointmentbooking.application.recommendation.AppointmentPreference;
import com.appointmentbooking.application.recommendation.AppointmentRecommendationService;
import com.appointmentbooking.domain.reservation.Reservation;
import com.appointmentbooking.domain.slot.BookableSlot;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePendingReservationUseCase {
    private static final Duration RESERVATION_TTL = Duration.ofMinutes(15);
    private final BookableSlotJpaRepository slots;
    private final ReservationJpaRepository reservations;
    private final ConfirmationTokenJpaRepository tokens;
    private final ConfirmationTokenService tokenService;
    private final AppointmentRecommendationService recommendations;
    private final ApplicationEventPublisher events;
    public CreatePendingReservationUseCase(BookableSlotJpaRepository slots, ReservationJpaRepository reservations,
            ConfirmationTokenJpaRepository tokens, ConfirmationTokenService tokenService,
            AppointmentRecommendationService recommendations, ApplicationEventPublisher events) {
        this.slots = slots; this.reservations = reservations; this.tokens = tokens; this.tokenService = tokenService;
        this.recommendations = recommendations; this.events = events;
    }
    @Transactional
    public PendingReservation create(UUID slotId, String guestEmail, AppointmentPreference preference) {
        BookableSlot slot = slots.findByIdForUpdate(slotId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SLOT_NOT_FOUND", "The requested slot does not exist."));
        if (slot.getStatus() != com.appointmentbooking.domain.slot.SlotStatus.AVAILABLE) {
            List<SlotAlternative> alternatives = recommendations.recommend(slot.getProvider().getId(), preference).stream()
                    .map(candidate -> new SlotAlternative(candidate.getId(), candidate.getStartsAt(), candidate.getEndsAt())).toList();
            throw new ApiException(HttpStatus.CONFLICT, "SLOT_UNAVAILABLE", "The requested slot is no longer available.", Map.of("alternatives", alternatives));
        }
        slot.reservePending();
        Reservation reservation = reservations.save(new Reservation(slot, guestEmail, Instant.now().plus(RESERVATION_TTL)));
        var issued = tokenService.issue(reservation);
        tokens.save(issued.token());
        events.publishEvent(new ConfirmationRequestedEvent(reservation.getId(), guestEmail, issued.rawToken()));
        return new PendingReservation(reservation.getId(), reservation.getExpiresAt());
    }
    public record PendingReservation(UUID reservationId, Instant expiresAt) { }
    public record SlotAlternative(UUID id, Instant startsAt, Instant endsAt) { }
}