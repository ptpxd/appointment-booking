package com.appointmentbooking.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ReservationExpiryIntegrationTest extends PostgresIntegrationTest {
    @Test @Disabled("Requires a persisted expired reservation fixture") void expiryReleasesItsSlot() { }
}