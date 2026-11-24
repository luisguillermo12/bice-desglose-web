import { Moment } from 'moment';

export interface IEmprConsolPeriodoDesgConsol {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codPlantilla?: number;
    corEmpresa?: number;
    nomEmpresa?: string;
    fecPeriodo?: Moment;
    valPrcConsol?: number;
    valPrcConsolInt?: number;
}

export class EmprConsolPeriodoDesgConsol implements IEmprConsolPeriodoDesgConsol {
    constructor(
        public id?: number,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public corEmpresa?: number,
        public nomEmpresa?: string,
        public fecPeriodo?: Moment,
        public valPrcConsol?: number,
        public valPrcConsolInt?: number,
        public codPlantilla?: number
    ) {
    }
}
