package cl.tutorial.desglose.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Antonio Marrero Palomino
 *
 * Custom implementation for spring's PasswordEncoder interface.
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    public static final Charset USED_CHARSET = StandardCharsets.UTF_8;

    @Override
    public String encode(CharSequence rawPassword) {
        // String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12));
        // return hashed;
        String encodedAsString;
        try {
            encodedAsString = TurbinePasswordEncoder.encode(rawPassword.toString());
        } catch (Exception e) {
            throw new TurbineEncodePasswordException("TurbinePasswordEncoder -> Error codificando la contraseña.", e);
        }
        return encodedAsString;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        final String newEncodedPassword = this.encode(rawPassword);
        return encodedPassword.equals(newEncodedPassword);
    }
}
