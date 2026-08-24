package com.appointmentbooking.adapter.out.persistence;

import com.appointmentbooking.domain.slot.BookableSlot;
import com.appointmentbooking.domain.slot.SlotStatus;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookableSlotJpaRepository extends JpaRepository<BookableSlot, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select slot from BookableSlot slot join fetch slot.provider where slot.id = :id")
    Optional<BookableSlot> findByIdForUpdate(@Param("id") UUID id);

    List<BookableSlot> findByProviderIdAndStatusAndStartsAtGreaterThanEqualAndStartsAtLessThanOrderByStartsAt(
            UUID providerId, SlotStatus status, Instant start, Instant end);

    Page<BookableSlot> findByProviderIdAndStartsAtBetweenOrderByStartsAt(UUID providerId, Instant from, Instant to, Pageable pageable);

    @Query("select case when count(slot) > 0 then true else false end from BookableSlot slot "
            + "where slot.provider.id = :providerId and slot.id <> :excludedId "
            + "and slot.startsAt < :endsAt and slot.endsAt > :startsAt")
    boolean existsOverlapping(@Param("providerId") UUID providerId, @Param("excludedId") UUID excludedId,
            @Param("startsAt") Instant startsAt, @Param("endsAt") Instant endsAt);

    @Query("select slot from BookableSlot slot join fetch slot.provider where slot.provider.id = :providerId "
            + "and slot.status = :status and slot.startsAt >= :from order by slot.startsAt")
    List<BookableSlot> findAvailableForRecommendation(@Param("providerId") UUID providerId,
            @Param("status") SlotStatus status, @Param("from") Instant from, Pageable pageable);
}