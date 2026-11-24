import { IDesglose } from 'app/shared/model/desglose.model';
import { IExtraDataDesglose } from 'app/shared/model/extra-data-desglose.model';

export interface IBreakdownSaveRequestDTO {
    desgloseDTO?: IDesglose;
    extraDataDTO?: IExtraDataDesglose;
    isIndicePeriodos?: number[];
    activePeriodsEdit?:number[];
    skipNotRequiredValidations?: boolean;
    isRapid?:boolean;
}

export class BreakdownSaveRequestDTO implements IBreakdownSaveRequestDTO {
    constructor(
        public desgloseDTO?: IDesglose,
        public extraDataDTO?: IExtraDataDesglose,
        public isIndicePeriodos?: number[],
        public activePeriodsEdit?: number[],
        public skipNotRequiredValidations?: boolean,
        public isRapid?:boolean
    ) {}
}
