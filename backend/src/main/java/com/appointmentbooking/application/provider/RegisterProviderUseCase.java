package com.appointmentbooking.application.provider;

import com.appointmentbooking.adapter.out.persistence.ProviderJpaRepository;
import com.appointmentbooking.application.ApiException;
import com.appointmentbooking.domain.provider.Provider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterProviderUseCase {
    private final ProviderJpaRepository providers; private final PasswordEncoder passwords;
    public RegisterProviderUseCase(ProviderJpaRepository providers, PasswordEncoder passwords) { this.providers = providers; this.passwords = passwords; }
    @Transactional
    public Provider register(String email, String password, String displayName) {
        if (providers.existsByEmailIgnoreCase(email)) throw new ApiException(HttpStatus.CONFLICT, "EMAIL_ALREADY_REGISTERED", "This email address is already registered.");
        return providers.save(new Provider(email.trim().toLowerCase(), passwords.encode(password), displayName.trim()));
    }
}