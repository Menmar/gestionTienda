package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.AbstractIT;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.ticket.LineaLlaveRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PdfControllerIT extends AbstractIT {

    @Test
    void descargarPdf_devuelveApplicationPdf() throws Exception {
        var clienteBody = mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new ClienteRequest("PDF", "Test", "699000003", null, null, null))))
                .andReturn().getResponse().getContentAsString();
        var clienteId = objectMapper.readTree(clienteBody).get("id").asLong();

        var llaveBody = mockMvc.perform(get("/catalogo/llaves")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andReturn().getResponse().getContentAsString();
        var tipoLlaveId = objectMapper.readTree(llaveBody).get(0).get("id").asLong();

        var ticketBody = mockMvc.perform(post("/tickets")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new TicketRequest(TipoTicket.LLAVE, clienteId, null, null,
                                "Urgente", null, null, null, null, null,
                                List.of(new LineaLlaveRequest(tipoLlaveId, (short) 1, null, null))))))
                .andReturn().getResponse().getContentAsString();
        var ticketId = objectMapper.readTree(ticketBody).get("id").asLong();

        var pdfBytes = mockMvc.perform(get("/tickets/" + ticketId + "/pdf")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("resguardo-" + ticketId + ".pdf")))
                .andReturn().getResponse().getContentAsByteArray();

        // PDF mágico: empieza con "%PDF"
        assertThat(new String(pdfBytes, 0, 4)).isEqualTo("%PDF");
    }

    @Test
    void descargarPdf_ticketInexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/tickets/999999/pdf")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isNotFound());
    }
}
