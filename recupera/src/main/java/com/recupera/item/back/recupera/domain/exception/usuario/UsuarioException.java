package com.recupera.item.back.recupera.domain.exception.usuario;

public class UsuarioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsuarioException(String message) {
        super(message);
    }

    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsuarioException(Throwable cause) {
        super(cause);
    }
 
}
