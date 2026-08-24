package com.appointmentbooking.application.reservation;

import java.util.UUID;

public record ConfirmationRequestedEvent(UUID reservationId, String guestEmail, String rawToken) { }