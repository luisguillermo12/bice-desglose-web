package cl.tutorial.desglose.service.exception;

/**
 * Clase de excepción para indicar que una entidad que se intenta buscar por su ID no existe.
 *
 * @author Antonio Marrero Palomino
 */
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
