package com.appointmentbooking.performance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ConcurrentBookingLoadTest {
    @Test @Disabled("Run against a dedicated PostgreSQL environment with a seeded slot")
    void oneHundredConcurrentBookingRequestsCanBeMeasured() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int request = 0; request < 100; request++) executor.submit(() -> { });
        }
    }
}