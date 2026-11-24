package cl.tutorial.desglose.test;

import cl.tutorial.desglose.service.dto.ConceptoDTO;
import cl.tutorial.desglose.service.util.Utils;
import org.apache.fulcrum.crypto.CryptoAlgorithm;
import org.apache.fulcrum.crypto.provider.JavaCrypt;
import org.apache.fulcrum.crypto.provider.OldJavaCrypt;
import org.apache.fulcrum.crypto.provider.UnixCrypt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static void main(String[] args) throws Exception {
        String glsGenericacorta = "0.49999999";
        BigDecimal eee = new BigDecimal(glsGenericacorta);
        System.out.println(eee.toString());

        String pass = "clave2002";
        String adminPass = "admin";

        CryptoAlgorithm ca = new JavaCrypt();
        CryptoAlgorithm ca2 = new OldJavaCrypt();
        CryptoAlgorithm ca3 = new UnixCrypt();

        System.out.println("DEFAULT_CIPHER -> SHA, JavaCrypt, adminPass -> " + ca2.encrypt(adminPass));

        System.out.println("DEFAULT_CIPHER -> SHA");
        System.out.println("JavaCrypt -> " + ca.encrypt(pass));
        System.out.println("OldJavaCrypt -> " + ca2.encrypt(pass));
        System.out.println("UnixCrypt -> " + ca3.encrypt(pass));

        ca.setCipher("MD5");
        ca2.setCipher("MD5");
        ca3.setCipher("MD5");

        System.out.println("DEFAULT_CIPHER -> MD5");
        System.out.println("JavaCrypt -> " + ca.encrypt(pass));
        System.out.println("OldJavaCrypt -> " + ca2.encrypt(pass));
        System.out.println("UnixCrypt -> " + ca3.encrypt(pass));


        ConceptoDTO c = new ConceptoDTO();
        ConceptoDTO c1 = new ConceptoDTO();
        c.setCodTipoConcep("ddfsfd");
        List<List<ConceptoDTO>> matrix = new ArrayList<>();
        final ArrayList<ConceptoDTO> conceptoDTOS = new ArrayList<>();
        final ArrayList<ConceptoDTO> conceptoDTOS1 = new ArrayList<>();
        conceptoDTOS.add(c);
        c.setGlsConcepto("BKA");
        c1.setGlsConcepto("BKA1");
        conceptoDTOS.add(c);
        conceptoDTOS1.add(c1);
        matrix.add(conceptoDTOS);
        matrix.add(conceptoDTOS1);
        System.out.println(matrix);

        System.out.println("Utils.convertToNumberingScheme(15) -> " + Utils.convertToNumberingScheme(15));
        System.out.println("Utils.convertToNumberingScheme(16) -> " + Utils.convertToNumberingScheme(16));
        System.out.println("Utils.convertToNumberingScheme(17) -> " + Utils.convertToNumberingScheme(17));

        String[] myStrings = { "", ".", "66", "EST_RESULT.6.2", "EST_RESULT.6.3", "EST_RESULT.6.4", "EST_RESULT.6.6", "EST_RESULT.6.8", "6.6", "6.6.", "6.6.11", "6.", ".9" };
        //        for (int i = 0; i < myStrings.length; i++) {
        //            System.out.println("Utils.isNumberAndDot(" + myStrings[i] + ") -> " + Utils.isNumberAndDot(myStrings[i]));
        //        }
        //
        //        System.out.println("Utils.convertToNumberingScheme(1) -> " + Utils.convertToNumberingScheme(1));
        //        System.out.println("Utils.convertToNumberingScheme(5) -> " + Utils.convertToNumberingScheme(5));

        //        String[] inputs = { "Turtle123456_fly.me", "birdy_12345678_prd.tr" };
        for (String str : myStrings) {
            System.out.println("original -> " + str + " fue transformado en -> " + str.replaceAll("[^A-Z_].*", ""));
        }

        for (String str : myStrings) {
            System.out.println("original -> " + str + " fue extraida la segunda parte -> " + str.substring(str.indexOf(".") + 1));
        }

        //        Test test = new TestImpl();
        //        System.out.println("test.getNameValue() = " + test.getNameValue());
        //        System.out.println("test.nombre = " + test.nombre);
        //        System.out.println("Test.nombre = " + Test.nombre);
        //
        //        BigDecimal value = new BigDecimal("1");
        //        Boolean isEmpresa = (value != null) ? BooleanUtils.toBoolean(value.intValue()) : false;
        //        System.out.println("isEmpresa = " + isEmpresa);
        //
        //        value = new BigDecimal("0");
        //        isEmpresa = (value != null) ? BooleanUtils.toBoolean(value.intValue()) : false;
        //        System.out.println("isEmpresa = " + isEmpresa);
        //
        //        Integer val = Integer.valueOf("1");
        //        System.out.println("val = " + val);
        //        String aa = null;
        //        Integer valNull = aa != null ? Integer.valueOf(aa) : null;
        //        System.out.println("valNull = " + valNull);
    }

}
