import { Moment } from 'moment';
import { isNullOrUndefined } from 'util';

export interface IProjectionHistoricDataRequestDTO {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codPlantilla?: string;
    fecPeriodo?: Moment;
}

export class ProjectionHistoricDataRequestDTO implements IProjectionHistoricDataRequestDTO {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public codPlantilla?: string,
        public fecPeriodo?: Moment,
    ) {}
}
