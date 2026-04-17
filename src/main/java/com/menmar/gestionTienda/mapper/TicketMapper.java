package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.foto.FotoResponse;
import com.menmar.gestionTienda.model.ticket.LineaResponse;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.file.Path;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "clienteId",    source = "cliente.id")
    @Mapping(target = "clienteNombre",   source = "cliente.nombre")
    @Mapping(target = "clienteTelefono", source = "cliente.telefono")
    @Mapping(target = "empleadoId",   source = "empleado.id")
    @Mapping(target = "empleadoNombre",
            expression = "java(ticket.getEmpleado().getNombre() + ' ' + ticket.getEmpleado().getApellidos())")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "tipoNombre", source = "tipoReparacion.nombre")
    @Mapping(target = "subtotal",
            expression = "java(linea.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(linea.getCantidad())))")
    LineaResponse toResponse(TicketCalzadoLinea linea);

    @Mapping(target = "tipoNombre", source = "tipoCostura.nombre")
    @Mapping(target = "subtotal",
            expression = "java(linea.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(linea.getCantidad())))")
    LineaResponse toResponse(TicketCosturaLinea linea);

    @Mapping(target = "tipoNombre", source = "tipoLlave.nombre")
    @Mapping(target = "subtotal",
            expression = "java(linea.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(linea.getCantidad())))")
    @Mapping(target = "descripcion", ignore = true)
    LineaResponse toResponse(TicketLlaveLinea linea);

    @Mapping(target = "ticketId",      source = "ticket.id")
    @Mapping(target = "nombreFichero",
            expression = "java(java.nio.file.Path.of(foto.getRutaLocal()).getFileName().toString())")
    FotoResponse toResponse(Foto foto);
}
