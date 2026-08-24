package com.appointmentbooking.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ProviderOwnershipIntegrationTest extends PostgresIntegrationTest {
    @Test @Disabled("Requires two persisted provider fixtures") void providerCannotAccessAnotherProvidersSlot() { }
}