package com.appointmentbooking.adapter.out.persistence;

import com.appointmentbooking.domain.provider.Provider;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderJpaRepository extends JpaRepository<Provider, UUID> {
    Optional<Provider> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}