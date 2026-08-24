package com.appointmentbooking.application.slot;

import com.appointmentbooking.adapter.out.persistence.BookableSlotJpaRepository;
import com.appointmentbooking.adapter.out.persistence.ProviderJpaRepository;
import com.appointmentbooking.application.ApiException;
import com.appointmentbooking.domain.provider.Provider;
import com.appointmentbooking.domain.slot.BookableSlot;
import com.appointmentbooking.domain.slot.SlotStatus;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageBookableSlotUseCase {
    private final BookableSlotJpaRepository slots; private final ProviderJpaRepository providers;
    public ManageBookableSlotUseCase(BookableSlotJpaRepository slots, ProviderJpaRepository providers) { this.slots = slots; this.providers = providers; }
    @Transactional
    public BookableSlot create(UUID providerId, Instant startsAt, Instant endsAt) {
        validateInterval(providerId, null, startsAt, endsAt);
        Provider provider = providers.findById(providerId).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHENTICATED", "Provider session is invalid."));
        return slots.save(new BookableSlot(provider, startsAt, endsAt));
    }
    @Transactional
    public BookableSlot update(UUID providerId, UUID slotId, Instant startsAt, Instant endsAt) {
        BookableSlot slot = owned(providerId, slotId);
        if (slot.getStatus() != SlotStatus.AVAILABLE) throw new ApiException(HttpStatus.CONFLICT, "SLOT_HAS_RESERVATION", "A reserved slot cannot be changed.");
        validateInterval(providerId, slotId, startsAt, endsAt); slot.updateTimes(startsAt, endsAt); return slot;
    }
    @Transactional
    public void delete(UUID providerId, UUID slotId) {
        BookableSlot slot = owned(providerId, slotId);
        if (slot.getStatus() != SlotStatus.AVAILABLE) throw new ApiException(HttpStatus.CONFLICT, "SLOT_HAS_RESERVATION", "A reserved slot cannot be deleted.");
        slots.delete(slot);
    }
    public Page<BookableSlot> list(UUID providerId, Instant from, Instant to, Pageable pageable) { return slots.findByProviderIdAndStartsAtBetweenOrderByStartsAt(providerId, from, to, pageable); }
    private BookableSlot owned(UUID providerId, UUID slotId) {
        BookableSlot slot = slots.findById(slotId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SLOT_NOT_FOUND", "Slot not found."));
        if (!slot.getProvider().getId().equals(providerId)) throw new ApiException(HttpStatus.NOT_FOUND, "SLOT_NOT_FOUND", "Slot not found.");
        return slot;
    }
    private void validateInterval(UUID providerId, UUID excludedId, Instant startsAt, Instant endsAt) {
        if (startsAt == null || endsAt == null || !endsAt.isAfter(startsAt) || !startsAt.isAfter(Instant.now())) throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_SLOT_INTERVAL", "Slots must be future intervals with a later end time.");
        if (slots.existsOverlapping(providerId, excludedId == null ? new UUID(0, 0) : excludedId, startsAt, endsAt)) throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "SLOT_OVERLAPS", "The slot overlaps an existing slot.");
    }
}