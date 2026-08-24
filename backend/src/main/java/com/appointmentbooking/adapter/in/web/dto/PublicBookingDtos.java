package com.appointmentbooking.adapter.in.web.dto;

import com.appointmentbooking.application.recommendation.AppointmentPreference.TimeOfDay;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class PublicBookingDtos {
    private PublicBookingDtos() { }
    public record CreateBookingRequest(@NotNull UUID slotId, @NotBlank @Email String guestEmail, @Valid Preference preference) { }
    public record Preference(LocalDate preferredDate, TimeOfDay preferredTimeOfDay) { }
    public record ConfirmBookingRequest(@NotBlank String token) { }
    public record ProviderSummary(UUID id, String displayName) { }
    public record SlotSummary(UUID id, Instant startsAt, Instant endsAt) { }
    public record PublicSlotsResponse(ProviderSummary provider, List<SlotSummary> slots) { }
    public record PendingBookingResponse(UUID reservationId, Instant expiresAt, String message) { }
    public record ConfirmedBookingResponse(UUID reservationId, String status, String providerName, SlotSummary slot) { }
}