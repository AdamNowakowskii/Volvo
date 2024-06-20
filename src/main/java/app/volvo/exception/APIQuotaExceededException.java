package app.volvo.exception;

public class APIQuotaExceededException extends RuntimeException {

    public APIQuotaExceededException(String message) {
        super(message);
    }

}