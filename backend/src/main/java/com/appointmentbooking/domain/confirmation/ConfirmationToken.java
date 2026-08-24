package com.appointmentbooking.domain.confirmation;

import com.appointmentbooking.domain.reservation.Reservation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken {
    @Id
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", unique = true)
    private Reservation reservation;
    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
    @Column(name = "used_at")
    private Instant usedAt;

    protected ConfirmationToken() { }
    public ConfirmationToken(Reservation reservation, String tokenHash, Instant expiresAt) {
        this.id = UUID.randomUUID();
        this.reservation = reservation;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }
    public void markUsed(Instant now) { this.usedAt = now; }
    public UUID getId() { return id; }
    public Reservation getReservation() { return reservation; }
    public String getTokenHash() { return tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getUsedAt() { return usedAt; }
}