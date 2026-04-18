package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.AbstractIT;
import com.menmar.gestionTienda.model.auth.LoginRequest;
import com.menmar.gestionTienda.model.auth.LoginResponse;
import com.menmar.gestionTienda.model.auth.RefreshRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerIT extends AbstractIT {

    @Test
    void loginCorrecto_devuelveTokens() throws Exception {
        var body = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest("admin@tienda.com", "Admin1234!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        var response = objectMapper.readValue(body, LoginResponse.class);
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }

    @Test
    void loginConPasswordIncorrecta_devuelve401() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest("admin@tienda.com", "wrongpass"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginConEmailInexistente_devuelve401() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest("noexiste@tienda.com", "Admin1234!"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh_conTokenValido_devuelveNuevosTokens() throws Exception {
        var loginBody = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest("admin@tienda.com", "Admin1234!"))))
                .andReturn().getResponse().getContentAsString();

        var loginResp = objectMapper.readValue(loginBody, LoginResponse.class);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new RefreshRequest(loginResp.refreshToken()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void logout_conTokenValido_devuelve204() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", bearer(tokenAdmin)))
                .andExpect(status().isNoContent());
    }
}
