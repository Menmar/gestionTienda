package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.AbstractIT;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerIT extends AbstractIT {

    @Test
    void crearCliente_devuelve201() throws Exception {
        var request = new ClienteRequest("Ana", "García López", "600111222", "ana@email.com", null, null);

        mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.telefono").value("600111222"));
    }

    @Test
    void crearCliente_telefonoDuplicado_devuelve400() throws Exception {
        var request = new ClienteRequest("Pedro", "Ruiz", "600222333", null, null, null);

        mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarClientes_devuelvePaginado() throws Exception {
        mockMvc.perform(get("/clientes?page=0&size=10")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").isNumber());
    }

    @Test
    void buscarPorTelefono_existente_devuelveCliente() throws Exception {
        var request = new ClienteRequest("Luis", "Martínez", "611999888", null, null, null);
        mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/clientes/telefono/611999888")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Luis"));
    }

    @Test
    void buscarPorTelefono_inexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/clientes/telefono/000000000")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarCliente_devuelve200() throws Exception {
        var crear = new ClienteRequest("Carmen", "Vega", "622333444", null, null, null);
        var idBody = mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(crear)))
                .andReturn().getResponse().getContentAsString();

        var id = objectMapper.readTree(idBody).get("id").asLong();

        var actualizar = new ClienteRequest("Carmen", "Vega Sánchez", "622333444", "carmen@email.com", null, null);
        mockMvc.perform(put("/clientes/" + id)
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(actualizar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellidos").value("Vega Sánchez"));
    }

    @Test
    void sinAutenticacion_devuelve401() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isUnauthorized());
    }
}
