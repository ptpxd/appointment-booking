package com.appointmentbooking.application.recommendation;

import java.time.LocalDate;

public record AppointmentPreference(LocalDate preferredDate, TimeOfDay preferredTimeOfDay) {
    public enum TimeOfDay { MORNING, AFTERNOON, EVENING }
}