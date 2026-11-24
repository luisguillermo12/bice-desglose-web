package cl.tutorial.desglose.domain.pk;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class DesgloseMensajePK extends NewCommonPK implements Serializable {

    private static final long          serialVersionUID = 1L;

    private ZonedDateTime fecEvent;

    public DesgloseMensajePK() {
    }

    public static DesgloseMensajePK newInstance(NewCommonPK commonPK) {
        DesgloseMensajePK pk = new DesgloseMensajePK();
        pk.codCliente = commonPK.codCliente;
        pk.codTipoDes = commonPK.codTipoDes;
        pk.codSubtipoDes = commonPK.codSubtipoDes;
        pk.codMoneda = commonPK.codMoneda;
        pk.codPlantilla = commonPK.codPlantilla;
        return pk;
    }

    public ZonedDateTime getFecEvent() {
        return fecEvent;
    }

    public void setFecEvent(ZonedDateTime fecEvent) {
        this.fecEvent = fecEvent;
    }
}
