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
    @Mapping(target = "establecimientoId",
            expression = "java(ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getId() : null)")
    @Mapping(target = "establecimientoNombre",
            expression = "java(ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getNombre() : null)")
    @Mapping(target = "establecimientoDireccion",
            expression = "java(ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getDireccion() : null)")
    @Mapping(target = "establecimientoTelefono",
            expression = "java(ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getTelefono() : null)")
    @Mapping(target = "precioBase",
            expression = "java(calcularPrecioBase(ticket))")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "tipoNombre", source = "tipoReparacion.nombre")
    @Mapping(target = "subtotal",
            expression = "java(calcularSubtotalCalzado(linea))")
    LineaResponse toResponse(TicketCalzadoLinea linea);

    @Mapping(target = "tipoNombre", source = "tipoCostura.nombre")
    @Mapping(target = "subtotal",
            expression = "java(calcularSubtotalCostura(linea))")
    LineaResponse toResponse(TicketCosturaLinea linea);

    @Mapping(target = "tipoNombre", source = "tipoLlave.nombre")
    @Mapping(target = "subtotal",
            expression = "java(calcularSubtotalLlave(linea))")
    @Mapping(target = "descripcion", ignore = true)
    LineaResponse toResponse(TicketLlaveLinea linea);

    @Mapping(target = "ticketId",      source = "ticket.id")
    @Mapping(target = "nombreFichero",
            expression = "java(java.nio.file.Path.of(foto.getRutaLocal()).getFileName().toString())")
    FotoResponse toResponse(Foto foto);

    default java.math.BigDecimal calcularPrecioBase(Ticket ticket) {
        var total = java.math.BigDecimal.ZERO;
        for (var l : ticket.getLineasCalzado())
            total = total.add(l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad())));
        for (var l : ticket.getLineasCostura())
            total = total.add(l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad())));
        for (var l : ticket.getLineasLlave())
            total = total.add(l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad())));
        return total;
    }

    default java.math.BigDecimal calcularSubtotalCalzado(TicketCalzadoLinea l) {
        var base = l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad()));
        if (l.getDescuento() == null || l.getDescuento().compareTo(java.math.BigDecimal.ZERO) == 0) return base;
        var factor = java.math.BigDecimal.ONE.subtract(l.getDescuento().divide(new java.math.BigDecimal("100")));
        return base.multiply(factor).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    default java.math.BigDecimal calcularSubtotalCostura(TicketCosturaLinea l) {
        var base = l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad()));
        if (l.getDescuento() == null || l.getDescuento().compareTo(java.math.BigDecimal.ZERO) == 0) return base;
        var factor = java.math.BigDecimal.ONE.subtract(l.getDescuento().divide(new java.math.BigDecimal("100")));
        return base.multiply(factor).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    default java.math.BigDecimal calcularSubtotalLlave(TicketLlaveLinea l) {
        var base = l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad()));
        if (l.getDescuento() == null || l.getDescuento().compareTo(java.math.BigDecimal.ZERO) == 0) return base;
        var factor = java.math.BigDecimal.ONE.subtract(l.getDescuento().divide(new java.math.BigDecimal("100")));
        return base.multiply(factor).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
