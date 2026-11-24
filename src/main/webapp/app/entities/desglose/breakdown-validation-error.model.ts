import { Moment } from 'moment';

export interface IBreakdownValidationError {
    tableRow?: number;
    tableColum?: number;
    errorMessage?: string;
    period?: Moment;
    isRequired?: boolean;
}

export class BreakdownValidationError {
    constructor(
        public tableRow?: number,
        public tableColum?: number,
        public errorMessage?: string,
        public period?: Moment,
        public isRequired?: boolean
    ) {
    }
}
