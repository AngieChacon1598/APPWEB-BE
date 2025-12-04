package pe.edu.vallegrande.restLosPinos.exceptions;

public class GlobalException extends RuntimeException {

    private final int codeError;

    public GlobalException(String message, int errorCode) {
        super(message);
        this.codeError = errorCode;
    }

    public int getCodeError() {
        return codeError;
    }
}
