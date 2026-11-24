package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;

public class ValPeriodoDesgImprPK extends PeriodoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    public static ValPeriodoDesgImprPK newInstance(PeriodoPK periodoPK) {
        ValPeriodoDesgImprPK pk = new ValPeriodoDesgImprPK();
        pk.setCodCliente(periodoPK.codCliente);
        pk.setCodTipoDes(periodoPK.codTipoDes);
        pk.setCodSubtipoDes(periodoPK.codSubtipoDes);
        pk.setCodMoneda(periodoPK.codMoneda);
        pk.setCodPlantilla(periodoPK.codPlantilla);
        pk.setFecPeriodo(periodoPK.fecPeriodo);
        return pk;
    }
}
