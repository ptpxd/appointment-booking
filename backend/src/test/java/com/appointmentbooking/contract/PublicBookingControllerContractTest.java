package com.appointmentbooking.contract;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.appointmentbooking.adapter.in.web.ApiExceptionHandler;
import com.appointmentbooking.adapter.in.web.PublicBookingController;
import com.appointmentbooking.adapter.out.persistence.BookableSlotJpaRepository;
import com.appointmentbooking.adapter.out.persistence.ProviderJpaRepository;
import com.appointmentbooking.application.reservation.ConfirmReservationUseCase;
import com.appointmentbooking.application.reservation.CreatePendingReservationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({PublicBookingController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class PublicBookingControllerContractTest {
    @Autowired MockMvc mvc;
    @MockBean ProviderJpaRepository providers;
    @MockBean BookableSlotJpaRepository slots;
    @MockBean CreatePendingReservationUseCase create;
    @MockBean ConfirmReservationUseCase confirm;
    @Test void missingBookingFieldsReturnTheStandardValidationError() throws Exception {
        mvc.perform(post("/api/public/bookings").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}