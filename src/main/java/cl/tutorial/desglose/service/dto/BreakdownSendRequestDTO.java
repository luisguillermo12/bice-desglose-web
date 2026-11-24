package cl.tutorial.desglose.service.dto;

import java.io.Serializable;

public class BreakdownSendRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public DesgloseDTO          desglose;
    public DesgloseAuditoriaDTO desgloseAuditoria;
    public DesgloseRevisionDTO  desgloseRevision;
    public DesgloseMensajeDTO   desgloseMensaje;
    public ExtraDataDTO         extraDataDesglose;
}
