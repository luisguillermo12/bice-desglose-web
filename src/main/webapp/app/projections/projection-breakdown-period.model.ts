import { Moment } from 'moment';
import { IBreakdownFilterResponseDTO } from 'app/entities/desglose/filter-breakdown-response.model';

export interface IProjectionBreakdownPeriod {
    breakdownFilterResponseDTO?: IBreakdownFilterResponseDTO;
    periodDate?: Moment;
}

export class ProjectionBreakdownPeriod implements IProjectionBreakdownPeriod {
    constructor(
        public breakdownFilterResponseDTO?: IBreakdownFilterResponseDTO,
        public periodDate?: Moment
    ) {
    }
}
