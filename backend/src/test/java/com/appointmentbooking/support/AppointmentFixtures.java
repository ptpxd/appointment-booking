package com.appointmentbooking.support;

import com.appointmentbooking.domain.provider.Provider;
import com.appointmentbooking.domain.slot.BookableSlot;
import java.time.Instant;

public final class AppointmentFixtures {
    private AppointmentFixtures() { }
    public static Provider provider() { return new Provider("provider@example.com", "hash", "Example Provider"); }
    public static BookableSlot futureSlot(Provider provider) { return new BookableSlot(provider, Instant.now().plusSeconds(3600), Instant.now().plusSeconds(5400)); }
}