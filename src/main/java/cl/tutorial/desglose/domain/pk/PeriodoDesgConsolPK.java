package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;

public class PeriodoDesgConsolPK extends PeriodoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    public static PeriodoDesgConsolPK newInstance(PeriodoPK periodoPK) {
        PeriodoDesgConsolPK pk = new PeriodoDesgConsolPK();
        pk.setCodCliente(periodoPK.codCliente);
        pk.setCodTipoDes(periodoPK.codTipoDes);
        pk.setCodSubtipoDes(periodoPK.codSubtipoDes);
        pk.setCodMoneda(periodoPK.codMoneda);
        pk.setCodPlantilla(periodoPK.codPlantilla);
        pk.setFecPeriodo(periodoPK.fecPeriodo);
        return pk;
    }
}
