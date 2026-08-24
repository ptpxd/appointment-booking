package com.appointmentbooking.adapter.out.email;

import com.appointmentbooking.application.reservation.ConfirmationRequestedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SpringMailConfirmationSender {
    private static final Logger log = LoggerFactory.getLogger(SpringMailConfirmationSender.class);
    private final JavaMailSender mailSender;
    private final String confirmationBaseUrl;
    public SpringMailConfirmationSender(JavaMailSender mailSender, @Value("${appointment.confirmation-base-url}") String confirmationBaseUrl) { this.mailSender = mailSender; this.confirmationBaseUrl = confirmationBaseUrl; }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(ConfirmationRequestedEvent event) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
                helper.setTo(event.guestEmail()); helper.setSubject("Confirm your appointment");
                helper.setText("Confirm your appointment: " + confirmationBaseUrl + "?token=" + event.rawToken(), false);
                mailSender.send(message);
                return;
            } catch (MessagingException | RuntimeException exception) {
                if (attempt == 3) log.error("confirmation_email_failed reservationId={}", event.reservationId(), exception);
                else log.warn("confirmation_email_retry reservationId={} attempt={}", event.reservationId(), attempt, exception);
            }
        }
    }
}