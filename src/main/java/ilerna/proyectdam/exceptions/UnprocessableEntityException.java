package ilerna.proyectdam.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion personalizada para el Status Http 422 de las Entidades de Persistencia.
 * Esta excepcion es lanzada cuando la validacion de los datos de Hibernate falla
 *
 * @author Jose F. Bejarano
 * @HttpStatus 422
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends Exception {
    public UnprocessableEntityException(String mensaje) {
        super(mensaje);
    }

}
