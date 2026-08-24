package com.appointmentbooking.adapter.in.web.dto;

import com.appointmentbooking.domain.reservation.ReservationStatus;
import com.appointmentbooking.domain.slot.SlotStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

public final class ProviderDtos {
    private ProviderDtos() { }
    public record RegisterRequest(@NotBlank @Email String email, @NotBlank @Size(min = 12, max = 128) String password, @NotBlank @Size(max = 120) String displayName) { }
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) { }
    public record ProviderResponse(UUID id, String email, String displayName) { }
    public record SlotRequest(@NotNull Instant startsAt, @NotNull Instant endsAt) { }
    public record SlotResponse(UUID id, Instant startsAt, Instant endsAt, SlotStatus status) { }
    public record ReservationResponse(UUID id, String guestEmail, ReservationStatus status, SlotResponse slot) { }
}