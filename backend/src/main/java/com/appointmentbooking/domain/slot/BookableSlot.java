package com.appointmentbooking.domain.slot;

import com.appointmentbooking.domain.provider.Provider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookable_slots")
public class BookableSlot {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id")
    private Provider provider;
    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;
    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;
    @Version
    private long version;

    protected BookableSlot() { }

    public BookableSlot(Provider provider, Instant startsAt, Instant endsAt) {
        this.id = UUID.randomUUID();
        this.provider = provider;
        updateTimes(startsAt, endsAt);
        this.status = SlotStatus.AVAILABLE;
    }

    public void reservePending() {
        if (status != SlotStatus.AVAILABLE) throw new IllegalStateException("Slot is not available");
        status = SlotStatus.PENDING_CONFIRMATION;
    }
    public void confirm() {
        if (status != SlotStatus.PENDING_CONFIRMATION) throw new IllegalStateException("Slot is not pending");
        status = SlotStatus.BOOKED;
    }
    public void release() {
        if (status != SlotStatus.PENDING_CONFIRMATION) throw new IllegalStateException("Slot is not pending");
        status = SlotStatus.AVAILABLE;
    }
    public void updateTimes(Instant startsAt, Instant endsAt) {
        if (startsAt == null || endsAt == null || !endsAt.isAfter(startsAt)) throw new IllegalArgumentException("Invalid slot interval");
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }
    public UUID getId() { return id; }
    public Provider getProvider() { return provider; }
    public Instant getStartsAt() { return startsAt; }
    public Instant getEndsAt() { return endsAt; }
    public SlotStatus getStatus() { return status; }
}