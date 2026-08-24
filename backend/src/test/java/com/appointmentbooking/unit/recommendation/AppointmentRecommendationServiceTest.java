package com.appointmentbooking.unit.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.appointmentbooking.application.recommendation.AppointmentPreference.TimeOfDay;
import org.junit.jupiter.api.Test;

class AppointmentRecommendationServiceTest {
    @Test void timeOfDayEnumProvidesTheSupportedPreferenceValues() {
        assertEquals(3, TimeOfDay.values().length);
    }
}