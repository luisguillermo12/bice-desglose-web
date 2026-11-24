import { IExtraDataDesglose } from 'app/shared/model/extra-data-desglose.model';

export interface IConceptValidationBreakdownRequestDTO {
    extraDataDTO?: IExtraDataDesglose;
    activePeriodsIndexes?: number[];
    skipNotRequiredValidations?: boolean;
}

export class ConceptValidationBreakdownRequestDTO implements IConceptValidationBreakdownRequestDTO {
    constructor(
        public extraDataDTO?: IExtraDataDesglose,
        public activePeriodsIndexes?: number[],
        public skipNotRequiredValidations?: boolean
    ) {
    }
}
