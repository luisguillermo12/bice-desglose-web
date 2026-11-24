package cl.tutorial.desglose.service.util;

import cl.tutorial.desglose.config.Constants;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class UtilsTest {

    private final static String rut1          = "4808058281";
    private final static String formattedRut1 = "480.805.828-1";
    private final static String rut2          = "78903690K";
    private final static String formattedRut2 = "78.903.690-K";
    private final static String rut3          = "407655616K";
    private final static String formattedRut3 = "407.655.616-K";
    private final static String rut4          = "2000057420";
    private final static String formattedRut4 = "200.005.742-0";
    private final static String rut5          = "26034359-9";
    private final static String formattedRut5 = "26.034.359-9";
    private final static String originalError = "ORA-01438: value larger than specified precision allowed for this column";
    private final static String textToCapitalize = "PAbLO maRrerO";
    private final static String textCapitalized = "Pablo Marrero";

    @Test
    public void formatRut() {
        final String actual1 = Utils.formatRut(rut1);
        assertEquals(formattedRut1, actual1);
        final String actual2 = Utils.formatRut(rut2);
        assertEquals(formattedRut2, actual2);
        final String actual3 = Utils.formatRut(rut3);
        assertEquals(formattedRut3, actual3);
        final String actual4 = Utils.formatRut(rut4);
        assertEquals(formattedRut4, actual4);
        final String actual5 = Utils.formatRut(rut5);
        assertEquals(formattedRut5, actual5);
        System.out.println("actual1 -> " + actual1);
        System.out.println("actual2 -> " + actual2);
        System.out.println("actual3 -> " + actual3);
        System.out.println("actual4 -> " + actual4);
        System.out.println("actual5 -> " + actual5);

    }

    @Test
    public void bigDecimalMath() {
        System.out.println(new BigDecimal(1).divide(new BigDecimal(3), 3, RoundingMode.HALF_UP));

    }

    @Test
    public void stringFormat() {
        String str = String.format("%s is not a %s", "Amar Singh", "Singh Amar"); // String value
        String expected = "Amar Singh is not a Singh Amar";
        System.out.println(str);
        assertEquals(expected, str);
    }

    @Test
    public void stringUtilsMatchIgnoreCase() {
        System.out.println("Constants.ORACLE_KNOWN_ERROR_MESSAGE_1 ->" + Constants.ORACLE_KNOWN_ERROR_MESSAGE_1);
        System.out.println("originalError -> " + originalError);
        assertFalse(StringUtils.containsIgnoreCase(Constants.ORACLE_KNOWN_ERROR_MESSAGE_1, originalError));
        assertTrue(StringUtils.containsIgnoreCase(originalError, Constants.ORACLE_KNOWN_ERROR_MESSAGE_1));

    }

    @Test
    public void convertStringToJavaDateTime() {
        System.out.println("input ->" + "2019-04-10T11:49:33");
        LocalDateTime test = LocalDateTime.parse("2019-04-10T11:49:33");
        assertNotNull(test);
    }

    @Test
    public void wordUtilsCapitalizeFully() {
        System.out.println("textToCapitalize ->" + textToCapitalize);
        System.out.println("textCapitalized ->" + textCapitalized);
        assertEquals(textCapitalized, WordUtils.capitalizeFully(textToCapitalize));
    }
}
