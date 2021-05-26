package ilerna.proyectdam.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion personalizada con Status Http 404
 * @HttpStatus 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyNotFoundException extends RuntimeException {
    public MyNotFoundException(String message) {
        super(message);
    }
}
