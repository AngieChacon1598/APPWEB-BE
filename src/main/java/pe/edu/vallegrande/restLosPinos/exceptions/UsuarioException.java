package pe.edu.vallegrande.restLosPinos.exceptions;

public class UsuarioException extends RuntimeException {

    private final int errorCode;

    public UsuarioException(Integer idUser) {
        super("Error al encontrar el usuario: " + idUser);
        this.errorCode = 404;
    }

    private int getErrorCode() {
        return errorCode;
    }
}
