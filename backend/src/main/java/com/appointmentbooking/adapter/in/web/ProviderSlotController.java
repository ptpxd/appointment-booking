package com.appointmentbooking.adapter.in.web;

import com.appointmentbooking.adapter.in.web.dto.ProviderDtos;
import com.appointmentbooking.application.slot.ManageBookableSlotUseCase;
import com.appointmentbooking.config.ProviderPrincipal;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider/slots")
public class ProviderSlotController {
    private final ManageBookableSlotUseCase slots;
    public ProviderSlotController(ManageBookableSlotUseCase slots) { this.slots = slots; }
    @GetMapping
    public Page<ProviderDtos.SlotResponse> list(@AuthenticationPrincipal ProviderPrincipal provider, @RequestParam Instant from, @RequestParam Instant to,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return slots.list(provider.id(), from, to, page(page, size)).map(this::response);
    }
    @PostMapping
    public ResponseEntity<ProviderDtos.SlotResponse> create(@AuthenticationPrincipal ProviderPrincipal provider, @Valid @RequestBody ProviderDtos.SlotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(response(slots.create(provider.id(), request.startsAt(), request.endsAt())));
    }
    @PatchMapping("/{slotId}")
    public ProviderDtos.SlotResponse update(@AuthenticationPrincipal ProviderPrincipal provider, @PathVariable UUID slotId, @Valid @RequestBody ProviderDtos.SlotRequest request) {
        return response(slots.update(provider.id(), slotId, request.startsAt(), request.endsAt()));
    }
    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal ProviderPrincipal provider, @PathVariable UUID slotId) {
        slots.delete(provider.id(), slotId); return ResponseEntity.noContent().build();
    }
    private Pageable page(int page, int size) { return PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size))); }
    private ProviderDtos.SlotResponse response(com.appointmentbooking.domain.slot.BookableSlot slot) { return new ProviderDtos.SlotResponse(slot.getId(), slot.getStartsAt(), slot.getEndsAt(), slot.getStatus()); }
}