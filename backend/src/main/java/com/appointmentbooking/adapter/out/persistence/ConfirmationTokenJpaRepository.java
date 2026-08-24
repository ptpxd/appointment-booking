package com.appointmentbooking.adapter.out.persistence;

import com.appointmentbooking.domain.confirmation.ConfirmationToken;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfirmationTokenJpaRepository extends JpaRepository<ConfirmationToken, UUID> {
    Optional<ConfirmationToken> findByTokenHash(String tokenHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select token from ConfirmationToken token join fetch token.reservation reservation join fetch reservation.slot where token.tokenHash = :hash")
    Optional<ConfirmationToken> findByTokenHashForUpdate(@Param("hash") String hash);
}