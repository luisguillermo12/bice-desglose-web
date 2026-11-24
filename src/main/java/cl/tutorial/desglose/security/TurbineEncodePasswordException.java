package cl.tutorial.desglose.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Antonio Marrero Palomino
 *
 * This exception is thrown in case of an error encoding the password whit org.apache.turbine.cryptoAlgorithm.encrypt() method.
 */
public class TurbineEncodePasswordException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public TurbineEncodePasswordException(String message) {
        super(message);
    }

    public TurbineEncodePasswordException(String message, Throwable t) {
        super(message, t);
    }
}
