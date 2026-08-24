package com.appointmentbooking.unit.provider;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.appointmentbooking.domain.provider.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class ProviderRegistrationServiceTest {
    @Test void passwordsAreStoredAsOneWayHashes() {
        var encoder = new BCryptPasswordEncoder();
        var provider = new Provider("provider@example.com", encoder.encode("secure-password"), "Provider");
        assertTrue(encoder.matches("secure-password", provider.getPasswordHash()));
    }
}