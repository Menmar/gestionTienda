package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.AbstractIT;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.LineaLlaveRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TicketControllerIT extends AbstractIT {

    private Long clienteId;
    private Long tipoLlaveId;

    @BeforeEach
    void prepararDatos() throws Exception {
        var clienteBody = mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new ClienteRequest("Test", "Cliente", "699000001", null, null, null))))
                .andReturn().getResponse().getContentAsString();
        clienteId = objectMapper.readTree(clienteBody).get("id").asLong();

        var llaveBody = mockMvc.perform(get("/catalogo/llaves")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andReturn().getResponse().getContentAsString();
        tipoLlaveId = objectMapper.readTree(llaveBody).get(0).get("id").asLong();
    }

    @Test
    void crearTicketLlave_devuelve201() throws Exception {
        var linea = new LineaLlaveRequest(tipoLlaveId, (short) 2, null, null);
        var request = new TicketRequest(TipoTicket.LLAVE, clienteId, null, null, "Sin observaciones",
                null, null, null, null, null, List.of(linea));

        mockMvc.perform(post("/tickets")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroTicket").value(startsWith("LLA-")))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.precioTotal").isNumber())
                .andExpect(jsonPath("$.lineasLlave", hasSize(1)));
    }

    @Test
    void listarTickets_devuelvePaginado() throws Exception {
        mockMvc.perform(get("/tickets?page=0&size=5")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size").value(5));
    }

    @Test
    void listarTicketsFiltradosPorTipo_devuelveSoloEseTipo() throws Exception {
        var linea = new LineaLlaveRequest(tipoLlaveId, (short) 1, null, null);
        var request = new TicketRequest(TipoTicket.LLAVE, clienteId, null, null, null,
                null, null, null, null, null, List.of(linea));
        mockMvc.perform(post("/tickets")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tickets?tipo=LLAVE&page=0&size=20")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].tipo", everyItem(is("LLAVE"))));
    }

    @Test
    void cambiarEstado_aListoDevuelveEstadoActualizado() throws Exception {
        var linea = new LineaLlaveRequest(tipoLlaveId, (short) 1, null, null);
        var request = new TicketRequest(TipoTicket.LLAVE, clienteId, null, null, null,
                null, null, null, null, null, List.of(linea));
        var ticketBody = mockMvc.perform(post("/tickets")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(request)))
                .andReturn().getResponse().getContentAsString();
        var ticketId = objectMapper.readTree(ticketBody).get("id").asLong();

        mockMvc.perform(patch("/tickets/" + ticketId + "/estado")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CambioEstadoRequest(EstadoTicket.LISTO))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("LISTO"));
    }

    @Test
    void buscarPorNumero_inexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/tickets/numero/LLA-99999")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isNotFound());
    }
}
