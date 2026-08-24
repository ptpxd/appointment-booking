package com.appointmentbooking.adapter.out.persistence;

import com.appointmentbooking.domain.reservation.Reservation;
import com.appointmentbooking.domain.reservation.ReservationStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationJpaRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByStatusAndExpiresAtLessThanEqual(ReservationStatus status, Instant now);

    @Query("select reservation from Reservation reservation join reservation.slot slot where slot.provider.id = :providerId "
            + "and slot.startsAt between :from and :to order by slot.startsAt")
    Page<Reservation> findForProvider(@Param("providerId") UUID providerId, @Param("from") Instant from,
            @Param("to") Instant to, Pageable pageable);
}