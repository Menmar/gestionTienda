package com.menmar.gestionTienda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.menmar.gestionTienda.model.auth.LoginRequest;
import com.menmar.gestionTienda.model.auth.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base para tests de integración. Requiere PostgreSQL local con el perfil "test"
 * configurado en application-test.yaml (base de datos separada de desarrollo).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String tokenAdmin;
    protected String tokenEmpleado;

    @BeforeEach
    void autenticar() throws Exception {
        tokenAdmin    = obtenerToken("admin@tienda.com",    "Admin1234!");
        tokenEmpleado = obtenerToken("empleado@tienda.com", "Empleado1!");
    }

    protected String bearer(String token) {
        return "Bearer " + token;
    }

    protected String json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private String obtenerToken(String email, String password) throws Exception {
        var body = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest(email, password))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(body, LoginResponse.class).accessToken();
    }
}
