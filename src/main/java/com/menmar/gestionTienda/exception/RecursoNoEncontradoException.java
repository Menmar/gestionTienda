package com.menmar.gestionTienda.exception;

public final class RecursoNoEncontradoException extends AppException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " no encontrado/a con id: " + id);
    }
}
