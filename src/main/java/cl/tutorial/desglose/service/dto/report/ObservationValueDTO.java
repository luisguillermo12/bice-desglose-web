package cl.tutorial.desglose.service.dto.report;

import java.io.Serializable;

/**
 * A DTO for the /download/informe_resumen_desglose_balance_A4 service.
 */
public class ObservationValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ObservationValueDTO{" + "value='" + value + '\'' + '}';
    }
}
