package app.volvo.exception;

public class APIKeyDisabledException extends RuntimeException {

    public APIKeyDisabledException(String message) {
        super(message);
    }

}