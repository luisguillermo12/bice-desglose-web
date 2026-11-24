import { IConcepto } from 'app/shared/model/concepto.model';
import { IBreakdownValidationError } from 'app/entities/desglose/breakdown-validation-error.model';

export  interface IBreakdownFilterConceptDto {
    conceptoDTOList?: IConcepto[];
    validationErrorDTOS?: IBreakdownValidationError[];
}
export class BreakdownFilterConceptDto {
    constructor(public conceptoDTOList?:IConcepto,public validationErrorDTOS?: IBreakdownValidationError[]){}
}
