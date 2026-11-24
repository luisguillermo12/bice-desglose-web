package cl.tutorial.desglose.security;

import org.apache.fulcrum.crypto.CryptoAlgorithm;
import org.apache.fulcrum.crypto.provider.OldJavaCrypt;

/**
 * @author Antonio Marrero Palomino
 *
 * Clase para encriptar la contraseña y garantizar la compatibilidad con las librerías utilizadas por el banco BICE. (org.apache.turbine 2.1)
 */
public class TurbinePasswordEncoder {

    public static final String encode(String password) throws Exception {
        if (password == null) {
            return null;
        }
        // Algoritmo para encriptar que se utiliza en org.apache.turbine 2.1
        CryptoAlgorithm cryptoAlgorithm = new OldJavaCrypt();
        return cryptoAlgorithm.encrypt(password);
    }
}
