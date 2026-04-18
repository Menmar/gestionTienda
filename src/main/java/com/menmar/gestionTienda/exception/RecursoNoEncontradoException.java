package com.menmar.gestionTienda.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " no encontrado/a con id: " + id);
    }
}
