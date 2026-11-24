import { IDesglose } from 'app/shared/model/desglose.model';
import { IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';
import { IDesgloseRevision } from 'app/shared/model/desglose-revision.model';
import { IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';
import { IExtraDataDesglose } from 'app/shared/model/extra-data-desglose.model';

export interface IBreakdownSendRequest {
    desglose: IDesglose;
    desgloseAuditoria: IDesgloseAuditoria;
    desgloseRevision: IDesgloseRevision;
    desgloseMensaje: IDesgloseMensaje;
    extraDataDesglose: IExtraDataDesglose;
}

export class BreakdownSendRequest {
    constructor(public desglose: IDesglose,
                public desgloseAuditoria: IDesgloseAuditoria,
                public desgloseRevision: IDesgloseRevision,
                public desgloseMensaje: IDesgloseMensaje,
                public extraDataDesglose: IExtraDataDesglose) {
    }
}
