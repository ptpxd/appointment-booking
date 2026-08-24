package com.appointmentbooking.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ConcurrentReservationIntegrationTest extends PostgresIntegrationTest {
    @Test @Disabled("Requires a persisted provider and available slot fixture") void exactlyOneConcurrentReservationWins() { }
}