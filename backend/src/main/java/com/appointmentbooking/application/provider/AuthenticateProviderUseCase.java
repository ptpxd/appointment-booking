package com.appointmentbooking.application.provider;

import com.appointmentbooking.adapter.out.persistence.ProviderJpaRepository;
import com.appointmentbooking.application.ApiException;
import com.appointmentbooking.domain.provider.Provider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateProviderUseCase {
    private final ProviderJpaRepository providers; private final PasswordEncoder passwords;
    public AuthenticateProviderUseCase(ProviderJpaRepository providers, PasswordEncoder passwords) { this.providers = providers; this.passwords = passwords; }
    public Provider authenticate(String email, String password) {
        Provider provider = providers.findByEmailIgnoreCase(email.trim()).orElseThrow(this::invalidCredentials);
        if (!passwords.matches(password, provider.getPasswordHash())) throw invalidCredentials();
        return provider;
    }
    private ApiException invalidCredentials() { return new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid email or password."); }
}