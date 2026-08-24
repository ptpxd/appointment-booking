package com.appointmentbooking.adapter.in.web;

import com.appointmentbooking.adapter.in.web.dto.ProviderDtos;
import com.appointmentbooking.adapter.out.persistence.ReservationJpaRepository;
import com.appointmentbooking.config.ProviderPrincipal;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider/reservations")
public class ProviderReservationController {
    private final ReservationJpaRepository reservations;
    public ProviderReservationController(ReservationJpaRepository reservations) { this.reservations = reservations; }
    @GetMapping
    public Page<ProviderDtos.ReservationResponse> list(@AuthenticationPrincipal ProviderPrincipal provider, @RequestParam Instant from, @RequestParam Instant to,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return reservations.findForProvider(provider.id(), from, to, PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size))))
                .map(reservation -> new ProviderDtos.ReservationResponse(reservation.getId(), reservation.getGuestEmail(), reservation.getStatus(),
                        new ProviderDtos.SlotResponse(reservation.getSlot().getId(), reservation.getSlot().getStartsAt(), reservation.getSlot().getEndsAt(), reservation.getSlot().getStatus())));
    }
}