package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.AbstractIT;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.ticket.LineaLlaveRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FotoControllerIT extends AbstractIT {

    private Long ticketId;

    @BeforeEach
    void prepararTicket() throws Exception {
        var clienteBody = mockMvc.perform(post("/clientes")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new ClienteRequest("Foto", "Test", "699000002", null, null, null))))
                .andReturn().getResponse().getContentAsString();
        var clienteId = objectMapper.readTree(clienteBody).get("id").asLong();

        var llaveBody = mockMvc.perform(get("/catalogo/llaves")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andReturn().getResponse().getContentAsString();
        var tipoLlaveId = objectMapper.readTree(llaveBody).get(0).get("id").asLong();

        var ticketBody = mockMvc.perform(post("/tickets")
                        .header("Authorization", bearer(tokenEmpleado))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new TicketRequest(TipoTicket.LLAVE, clienteId, null, null, null,
                                null, null, null, null, null, List.of(new LineaLlaveRequest(tipoLlaveId, (short) 1, null, null))))))
                .andReturn().getResponse().getContentAsString();
        ticketId = objectMapper.readTree(ticketBody).get("id").asLong();
    }

    @Test
    void subirFoto_devuelve201ConMetadatos() throws Exception {
        var fichero = new MockMultipartFile("fichero", "zapato.jpg",
                MediaType.IMAGE_JPEG_VALUE, "fake-image-bytes".getBytes());

        mockMvc.perform(multipart("/tickets/" + ticketId + "/fotos")
                        .file(fichero)
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.ticketId").value(ticketId))
                .andExpect(jsonPath("$.nombreFichero").value(endsWith(".jpg")));
    }

    @Test
    void listarFotos_devuelveListaConFotoSubida() throws Exception {
        var fichero = new MockMultipartFile("fichero", "zapato2.png",
                MediaType.IMAGE_PNG_VALUE, "fake-png-bytes".getBytes());

        mockMvc.perform(multipart("/tickets/" + ticketId + "/fotos")
                        .file(fichero)
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tickets/" + ticketId + "/fotos")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void eliminarFoto_devuelve204YNoAparece() throws Exception {
        var fichero = new MockMultipartFile("fichero", "borrar.jpg",
                MediaType.IMAGE_JPEG_VALUE, "delete-me".getBytes());

        var subirBody = mockMvc.perform(multipart("/tickets/" + ticketId + "/fotos")
                        .file(fichero)
                        .header("Authorization", bearer(tokenEmpleado)))
                .andReturn().getResponse().getContentAsString();
        var fotoId = objectMapper.readTree(subirBody).get("id").asLong();

        mockMvc.perform(delete("/tickets/" + ticketId + "/fotos/" + fotoId)
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tickets/" + ticketId + "/fotos")
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + fotoId + ")]", hasSize(0)));
    }

    @Test
    void descargarFoto_devuelveContenido() throws Exception {
        var contenido = "fake-image-content".getBytes();
        var fichero = new MockMultipartFile("fichero", "descarga.jpg",
                MediaType.IMAGE_JPEG_VALUE, contenido);

        var subirBody = mockMvc.perform(multipart("/tickets/" + ticketId + "/fotos")
                        .file(fichero)
                        .header("Authorization", bearer(tokenEmpleado)))
                .andReturn().getResponse().getContentAsString();
        var fotoId = objectMapper.readTree(subirBody).get("id").asLong();

        mockMvc.perform(get("/tickets/" + ticketId + "/fotos/" + fotoId)
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(contenido));
    }

    @Test
    void subirFoto_aTicketInexistente_devuelve404() throws Exception {
        var fichero = new MockMultipartFile("fichero", "x.jpg",
                MediaType.IMAGE_JPEG_VALUE, "data".getBytes());

        mockMvc.perform(multipart("/tickets/999999/fotos")
                        .file(fichero)
                        .header("Authorization", bearer(tokenEmpleado)))
                .andExpect(status().isNotFound());
    }
}
