package com.appointmentbooking.adapter.in.web;

import com.appointmentbooking.adapter.in.web.dto.PublicBookingDtos;
import com.appointmentbooking.adapter.out.persistence.BookableSlotJpaRepository;
import com.appointmentbooking.adapter.out.persistence.ProviderJpaRepository;
import com.appointmentbooking.application.ApiException;
import com.appointmentbooking.application.recommendation.AppointmentPreference;
import com.appointmentbooking.application.reservation.ConfirmReservationUseCase;
import com.appointmentbooking.application.reservation.CreatePendingReservationUseCase;
import com.appointmentbooking.domain.slot.SlotStatus;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicBookingController {
    private final ProviderJpaRepository providers; private final BookableSlotJpaRepository slots;
    private final CreatePendingReservationUseCase create; private final ConfirmReservationUseCase confirm;
    public PublicBookingController(ProviderJpaRepository providers, BookableSlotJpaRepository slots, CreatePendingReservationUseCase create, ConfirmReservationUseCase confirm) { this.providers = providers; this.slots = slots; this.create = create; this.confirm = confirm; }
    @GetMapping("/providers/{providerId}/slots")
    public PublicBookingDtos.PublicSlotsResponse slots(@PathVariable UUID providerId, @RequestParam LocalDate date) {
        var provider = providers.findById(providerId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "PROVIDER_NOT_FOUND", "Provider not found."));
        var start = date.atStartOfDay(ZoneOffset.UTC).toInstant(); var end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        var result = slots.findByProviderIdAndStatusAndStartsAtGreaterThanEqualAndStartsAtLessThanOrderByStartsAt(providerId, SlotStatus.AVAILABLE, start, end).stream().map(slot -> new PublicBookingDtos.SlotSummary(slot.getId(), slot.getStartsAt(), slot.getEndsAt())).toList();
        return new PublicBookingDtos.PublicSlotsResponse(new PublicBookingDtos.ProviderSummary(provider.getId(), provider.getDisplayName()), result);
    }
    @PostMapping("/bookings")
    public ResponseEntity<PublicBookingDtos.PendingBookingResponse> create(@Valid @RequestBody PublicBookingDtos.CreateBookingRequest request) {
        var preference = request.preference() == null ? null : new AppointmentPreference(request.preference().preferredDate(), request.preference().preferredTimeOfDay());
        var result = create.create(request.slotId(), request.guestEmail(), preference);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new PublicBookingDtos.PendingBookingResponse(result.reservationId(), result.expiresAt(), "A confirmation email has been sent."));
    }
    @PostMapping("/bookings/confirm")
    public PublicBookingDtos.ConfirmedBookingResponse confirm(@Valid @RequestBody PublicBookingDtos.ConfirmBookingRequest request) {
        var reservation = confirm.confirm(request.token()); var slot = reservation.getSlot();
        return new PublicBookingDtos.ConfirmedBookingResponse(reservation.getId(), reservation.getStatus().name(), slot.getProvider().getDisplayName(), new PublicBookingDtos.SlotSummary(slot.getId(), slot.getStartsAt(), slot.getEndsAt()));
    }
}