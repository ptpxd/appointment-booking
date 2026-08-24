package com.appointmentbooking.application.recommendation;

import com.appointmentbooking.adapter.out.persistence.BookableSlotJpaRepository;
import com.appointmentbooking.domain.slot.BookableSlot;
import com.appointmentbooking.domain.slot.SlotStatus;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AppointmentRecommendationService {
    private final BookableSlotJpaRepository slots;
    public AppointmentRecommendationService(BookableSlotJpaRepository slots) { this.slots = slots; }

    public List<BookableSlot> recommend(UUID providerId, AppointmentPreference preference) {
        return slots.findAvailableForRecommendation(providerId, SlotStatus.AVAILABLE, Instant.now(), PageRequest.of(0, 50)).stream()
                .sorted(Comparator.comparingInt((BookableSlot slot) -> score(slot, preference)).thenComparing(BookableSlot::getStartsAt))
                .limit(3)
                .toList();
    }
    private int score(BookableSlot slot, AppointmentPreference preference) {
        if (preference == null) return 2;
        var start = slot.getStartsAt().atZone(ZoneOffset.UTC);
        int score = preference.preferredDate() != null && preference.preferredDate().equals(start.toLocalDate()) ? 0 : 2;
        if (preference.preferredTimeOfDay() != null && timeOfDay(start.getHour()) != preference.preferredTimeOfDay()) score++;
        return score;
    }
    private AppointmentPreference.TimeOfDay timeOfDay(int hour) {
        if (hour < 12) return AppointmentPreference.TimeOfDay.MORNING;
        if (hour < 18) return AppointmentPreference.TimeOfDay.AFTERNOON;
        return AppointmentPreference.TimeOfDay.EVENING;
    }
}