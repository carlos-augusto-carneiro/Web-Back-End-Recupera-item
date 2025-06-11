package com.recupera.item.back.recupera.domain.exception.usuario;

public class SenhaFracaException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public SenhaFracaException(String message) {
        super(message);
    }

    public SenhaFracaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SenhaFracaException(Throwable cause) {
        super(cause);
    }

}
