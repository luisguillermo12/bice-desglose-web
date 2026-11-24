package cl.tutorial.desglose.service.util;

import cl.tutorial.desglose.config.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

import static cl.tutorial.desglose.config.Constants.DATABASE_ORACLE;
import static cl.tutorial.desglose.config.Constants.DATABASE_SQL;

public class Utils {

    public static final Integer[] indexesOf(String searchableString, String keyword) {
        List<Integer> list = new ArrayList<Integer>();
        int index = searchableString.indexOf(keyword);
        while (index >= 0) {
            list.add(index);
            index = searchableString.indexOf(keyword, index + keyword.length());
        }
        return list.toArray(new Integer[list.size()]);
    }

    //    public static final Integer[] indexesOfList(List<ConceptoDTO> dtoList, String codConcepto) {
    //        List<Integer> list = new ArrayList<Integer>();
    //
    //        int index = searchableString.indexOf(keyword);
    //        while (index >= 0) {
    //            System.out.println("Index : " + index);
    //            list.add(index);
    //            index = searchableString.indexOf(keyword, index + keyword.length());
    //        }
    //        return (Integer[]) list.toArray();
    //    }

    /**
     * Convierte un número en letra. 1 = A, 2 = B, ... 26 = Z
     *
     * @param number
     */
    public static String convertToNumberingScheme(int number) {
        String result = "";
        while (number > 0) {
            number--; // 1 => a, not 0 => a
            int remainder = number % 26;
            char digit = (char) (remainder + 97);
            result = digit + result;
            number = (number - remainder) / 26;
        }

        return result.toUpperCase();
    }

    /**
     * Chequea que un string solo contenga números y puntos.
     *
     * @param string
     *
     * @return
     */
    public static boolean isNumberAndDot(String string) {
        return string.matches("^([\\d\\.]+)");
    }

    /**
     * Deja solo letras y guión bajo en una cadena de caracteres.
     *
     * @param string
     *
     * @return
     */
    public static boolean letOnlyLettersAndUnderScoar(String string) {
        return string.matches("^([\\d\\.]+)");
    }

    /**
     * Utilidad para far formato a un rut con puntos y guión.
     *
     * @param rut
     *
     * @return
     */
    public static String formatRut(String rut) {
        int cont = 0;
        String format;
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        format = "-" + rut.substring(rut.length() - 1);
        for (int i = rut.length() - 2; i >= 0; i--) {
            format = rut.substring(i, i + 1) + format;
            cont++;
            if (cont == 3 && i != 0) {
                format = "." + format;
                cont = 0;
            }
        }
        return format;
    }

    public static boolean isAllNullsOrZeros(Iterable<String> array) {
        return StreamSupport.stream(array.spliterator(), true)
            .allMatch(o -> o == null || Constants.ZERO_AS_STRING.equals(o) || Constants.VALUE_AS_STRING.equals(o) || Constants.ERROR_AS_STRING.equals(o) || o.trim().isEmpty());
    }
    public static HashMap<String, Object> getHibernateConfiguracion(String database){
        HashMap<String, Object> properties = new HashMap<>();
        switch (database){
            case DATABASE_ORACLE:
                properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
                break;
            case DATABASE_SQL:
                // properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
                break;
        }
        properties.put("hibernate.cache.use_second_level_cache","false");
        properties.put("hibernate.cache.use_query_cache","false");
        properties.put("hibernate.show_sql","true");
        properties.put("hibernate.connection.autocommit","false");
        properties.put("hibernate.proc.param_null_passing","true");
        properties.put("hibernate.id.new_generator_mappings","true");
        properties.put("hibernate.generate_statistics","true");
        return properties;
    }
}
