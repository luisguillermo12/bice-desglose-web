import { Moment } from 'moment';

export interface IProjectionHistoricDataResponseDTO {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codPlantilla?: number;
    fecPeriodo?: Moment;
}

export class ProjectionHistoricDataResponseDTO implements IProjectionHistoricDataResponseDTO {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public codPlantilla?: number,
        public fecPeriodo?: Moment
    ) {
    }
}
