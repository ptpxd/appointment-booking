package com.appointmentbooking.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AppointmentRecommendationIntegrationTest extends PostgresIntegrationTest {
    @Test @Disabled("Requires persisted available slots") void recommendationReturnsAtMostThreeSlots() { }
}