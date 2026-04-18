package com.menmar.gestionTienda.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Respuesta paginada genérica")
public record PageResponse<T>(
        @Schema(description = "Elementos de la página actual") List<T> content,
        @Schema(description = "Número de página actual (0-based)", example = "0") int page,
        @Schema(description = "Tamaño de página solicitado", example = "20") int size,
        @Schema(description = "Total de elementos en todas las páginas", example = "42") long totalElements,
        @Schema(description = "Total de páginas", example = "3") int totalPages,
        @Schema(description = "Indica si es la última página", example = "false") boolean last
) {
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}