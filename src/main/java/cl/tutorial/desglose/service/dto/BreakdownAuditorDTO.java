package cl.tutorial.desglose.service.dto;

import java.io.Serializable;

/**
 * A DTO for the DescripcionCodigo entity.
 */
public class BreakdownAuditorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public DescripcionCodigoDTO auditor;
    public String               otherAuditorName;

    @Override
    public String toString() {
        return "BreakdownAuditorDTO{" + "auditor=" + auditor + ", otherAuditorName='" + otherAuditorName + '\'' + '}';
    }
}
