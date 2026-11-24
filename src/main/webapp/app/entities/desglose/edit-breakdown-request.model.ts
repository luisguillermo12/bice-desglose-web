import { IDesglose } from 'app/shared/model/desglose.model';

export interface IBreakdownEditRequestDTO {
    desglose?: IDesglose;
    req2Firma?: boolean;
    isNewApprobationProcess?: boolean;
}

export class BreakdownEditRequestDTO implements IBreakdownEditRequestDTO {
    constructor(
        public desglose?: IDesglose,
        public req2Firma?: boolean,
        public isNewApprobationProcess?: boolean,
    ) {
    }
}
