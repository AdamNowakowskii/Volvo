package app.volvo.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.ResponseEntity.status;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "error";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleInternalServerError(Exception ex) {
        if (ex instanceof APIKeyDisabledException) {
            return status(INTERNAL_SERVER_ERROR).body(Map.of(ERROR_KEY, ex.getMessage()));
        }

        return status(INTERNAL_SERVER_ERROR).body(Map.of(ERROR_KEY, "An unexpected error has occurred"));
    }

    @ExceptionHandler(APIQuotaExceededException.class)
    public ResponseEntity<Map<String, String>> handleTooManyRequests(APIQuotaExceededException ex) {
        return status(TOO_MANY_REQUESTS).body(Map.of(ERROR_KEY, ex.getMessage()));
    }

}
