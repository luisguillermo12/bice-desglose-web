import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';

export interface IBreakdownAuditor {
    auditor?: IDescripcionCodigo;
    otherAuditorName?: string;
}

export class BreakdownAuditor implements IBreakdownAuditor {
    constructor(
        public auditor?: IDescripcionCodigo,
        public otherAuditorName?: string
    ) {
    }
}
