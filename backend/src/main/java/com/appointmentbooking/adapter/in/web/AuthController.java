package com.appointmentbooking.adapter.in.web;

import com.appointmentbooking.adapter.in.web.dto.ProviderDtos;
import com.appointmentbooking.application.provider.AuthenticateProviderUseCase;
import com.appointmentbooking.application.provider.RegisterProviderUseCase;
import com.appointmentbooking.config.ProviderPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegisterProviderUseCase register;
    private final AuthenticateProviderUseCase authenticate;
    public AuthController(RegisterProviderUseCase register, AuthenticateProviderUseCase authenticate) { this.register = register; this.authenticate = authenticate; }
    @PostMapping("/register")
    public ResponseEntity<ProviderDtos.ProviderResponse> register(@Valid @RequestBody ProviderDtos.RegisterRequest request) {
        var provider = register.register(request.email(), request.password(), request.displayName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProviderDtos.ProviderResponse(provider.getId(), provider.getEmail(), provider.getDisplayName()));
    }
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody ProviderDtos.LoginRequest request, HttpServletRequest servletRequest) {
        var provider = authenticate.authenticate(request.email(), request.password());
        var principal = ProviderPrincipal.from(provider);
        var authentication = UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());
        var context = new SecurityContextImpl(authentication);
        SecurityContextHolder.setContext(context);
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}