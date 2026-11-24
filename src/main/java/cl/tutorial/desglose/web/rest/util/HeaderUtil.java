package cl.tutorial.desglose.web.rest.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LogManager.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME        = "desgloseApp";
    private static final String APPLICATION_NAME_CUSTOM = "breakdown";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityClonedAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".cloned", param);
    }

    public static HttpHeaders createEntityCreationAlertCustom(String entityName, String param) {
        return createAlert(APPLICATION_NAME_CUSTOM + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public static HttpHeaders createEntityCustomDeleteAlert(String entityName) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deletedList", null);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", "error." + errorKey);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }

    /**
     * Crea un string de la manera {"valor1", "valor2", "valorN"}
     *
     * @param values
     *
     * @return
     */
    public static String createHeaderEntityId(String... values) {
        String result = "{";
        for (int i = 0; i < values.length; i++) {
            result += values[i];
            if ((i + 1) < values.length) {
                result += ", ";
            }
        }
        result += "}";
        return result;
    }
}
