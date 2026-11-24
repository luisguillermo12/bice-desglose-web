import { Moment } from 'moment';

export interface IPeriodoDesgImpresion {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codMonedaImp?: string;
    fecPeriodo?: Moment;
    valDeflacPrd?: number;
    valTipoCambio?: number;
    corImpreso?: number;
    codPlantilla?: number;
    id?: number;
}

export class PeriodoDesgImpresion implements IPeriodoDesgImpresion {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public codMonedaImp?: string,
        public fecPeriodo?: Moment,
        public valDeflacPrd?: number,
        public valTipoCambio?: number,
        public corImpreso?: number,
        public codPlantilla?: number,
        public id?: number
    ) {
    }
}
