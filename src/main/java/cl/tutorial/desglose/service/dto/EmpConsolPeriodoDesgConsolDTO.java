package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the empresa consolidada and PeriodoDesgConsol entities fields.
 */
public class EmpConsolPeriodoDesgConsolDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String     codCliente;
    public Integer    codTipoDes;
    public Integer    codSubtipoDes;
    public String     codMoneda;
    public Integer    codPlantilla;
    public Integer    corEmpresa;
    public String     nomEmpresa;
    public LocalDate  fecPeriodo;
    public BigDecimal valPrcConsol;
    public Integer    valPrcConsolInt;
}
