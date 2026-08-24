package com.appointmentbooking.config;

import com.appointmentbooking.domain.provider.Provider;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record ProviderPrincipal(UUID id, String email, String passwordHash, String displayName) implements UserDetails {
    public static ProviderPrincipal from(Provider provider) { return new ProviderPrincipal(provider.getId(), provider.getEmail(), provider.getPasswordHash(), provider.getDisplayName()); }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("ROLE_PROVIDER")); }
    @Override public String getPassword() { return passwordHash; }
    @Override public String getUsername() { return email; }
}