import { Moment } from 'moment';

export interface IPeriodoDesgConsol {
    id?: number;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codPlantilla?: number;
    corEmpresa?: number;
    fecPeriodo?: Moment;
    valPrcConsol?: number;
}

export class PeriodoDesgConsol implements IPeriodoDesgConsol {
    constructor(
        public id?: number,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public corEmpresa?: number,
        public fecPeriodo?: Moment,
        public valPrcConsol?: number,
        public codPlantilla?: number
    ) {
    }
}
