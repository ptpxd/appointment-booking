package com.appointmentbooking.domain.reservation;

import com.appointmentbooking.domain.slot.BookableSlot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id")
    private BookableSlot slot;
    @Column(name = "guest_email", nullable = false)
    private String guestEmail;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "confirmed_at")
    private Instant confirmedAt;
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    protected Reservation() { }

    public Reservation(BookableSlot slot, String guestEmail, Instant expiresAt) {
        this.id = UUID.randomUUID();
        this.slot = slot;
        this.guestEmail = guestEmail;
        this.status = ReservationStatus.PENDING_CONFIRMATION;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
    }
    public void confirm(Instant now) {
        if (status != ReservationStatus.PENDING_CONFIRMATION) throw new IllegalStateException("Reservation cannot be confirmed");
        status = ReservationStatus.CONFIRMED;
        confirmedAt = now;
    }
    public void expire() {
        if (status != ReservationStatus.PENDING_CONFIRMATION) throw new IllegalStateException("Reservation cannot be expired");
        status = ReservationStatus.EXPIRED;
    }
    public UUID getId() { return id; }
    public BookableSlot getSlot() { return slot; }
    public String getGuestEmail() { return guestEmail; }
    public ReservationStatus getStatus() { return status; }
    public Instant getExpiresAt() { return expiresAt; }
}