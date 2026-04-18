package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.estadistica.ResumenEstadisticaResponse;
import com.menmar.gestionTienda.model.estadistica.TicketPendienteRecogidaResponse;
import com.menmar.gestionTienda.model.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EstadisticaService {
    ResumenEstadisticaResponse resumen(LocalDate desde, LocalDate hasta, Long establecimientoId);
    PageResponse<TicketPendienteRecogidaResponse> pendientesRecogida(int diasDesde, Long establecimientoId, Pageable pageable);
}
