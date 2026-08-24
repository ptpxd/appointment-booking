package com.appointmentbooking.application.confirmation;

import com.appointmentbooking.domain.confirmation.ConfirmationToken;
import com.appointmentbooking.domain.reservation.Reservation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {
    private final SecureRandom random = new SecureRandom();
    public IssuedToken issue(Reservation reservation) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return new IssuedToken(raw, new ConfirmationToken(reservation, hash(raw), reservation.getExpiresAt()));
    }
    public String hash(String rawToken) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(rawToken.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
    public record IssuedToken(String rawToken, ConfirmationToken token) { }
}