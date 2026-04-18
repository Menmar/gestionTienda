package com.menmar.gestionTienda.exception;

public sealed class AppException extends RuntimeException
        permits RecursoNoEncontradoException, NegocioException, ConflictoException {

    protected AppException(String message) {
        super(message);
    }
}
