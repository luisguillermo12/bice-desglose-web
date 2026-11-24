package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;

public class PeriodoDesgImpresionPK extends PeriodoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    public static PeriodoDesgImpresionPK newInstance(PeriodoPK periodoPK) {
        PeriodoDesgImpresionPK pk = new PeriodoDesgImpresionPK();
        pk.setCodCliente(periodoPK.codCliente);
        pk.setCodTipoDes(periodoPK.codTipoDes);
        pk.setCodSubtipoDes(periodoPK.codSubtipoDes);
        pk.setCodMoneda(periodoPK.codMoneda);
        pk.setFecPeriodo(periodoPK.fecPeriodo);
        return pk;
    }
}
