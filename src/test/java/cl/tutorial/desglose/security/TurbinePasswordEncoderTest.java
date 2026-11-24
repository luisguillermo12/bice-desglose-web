package cl.tutorial.desglose.security;

import org.junit.Assert;
import org.junit.Test;

public class TurbinePasswordEncoderTest {

    private static final String plainPassword      = "clave2002";
    private static final String plainPasswordAdmin = "admin";
    private static final String encodedPassword    = "YaupOf3xs0Olj05Q0UKD459G";

    @Test
    public void encrypt() throws Exception {
        Assert.assertNotNull("La contraseña de entrada no puede ser NULL", plainPassword);
        final String encodedAsString = TurbinePasswordEncoder.encode(plainPassword);
        Assert.assertNotNull("La contraseña codificada no puede ser NULL", encodedAsString);
        Assert.assertEquals("Las contraseñas encriptadas no son iguales.", encodedPassword, encodedAsString);
    }

    @Test
    public void encryptAdmin() throws Exception {
        Assert.assertNotNull("La contraseña de entrada no puede ser NULL", plainPasswordAdmin);
        final String encodedAsString = TurbinePasswordEncoder.encode(plainPasswordAdmin);
        Assert.assertNotNull("La contraseña codificada no puede ser NULL", encodedAsString);
        System.out.println(encodedAsString);
    }
}
