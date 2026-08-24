package com.appointmentbooking.contract;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.appointmentbooking.adapter.in.web.ApiExceptionHandler;
import com.appointmentbooking.adapter.in.web.AuthController;
import com.appointmentbooking.application.provider.AuthenticateProviderUseCase;
import com.appointmentbooking.application.provider.RegisterProviderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({AuthController.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class ProviderDashboardControllerContractTest {
    @Autowired MockMvc mvc;
    @MockBean RegisterProviderUseCase register;
    @MockBean AuthenticateProviderUseCase authenticate;
    @Test void invalidRegistrationIsRejected() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
    }
}