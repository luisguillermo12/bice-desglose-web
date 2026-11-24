import { Moment } from 'moment';

export interface IDesgloseImpresion {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codMonedaImp?: string;
    valDeflacBase?: number;
    fecCreacion?: Moment;
    fecDeflacBase?: Moment;
    corImpreso?: number;
    codPlantillaDes?: number;
    id?: number;
}

export class DesgloseImpresion implements IDesgloseImpresion {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public codMonedaImp?: string,
        public valDeflacBase?: number,
        public fecCreacion?: Moment,
        public fecDeflacBase?: Moment,
        public corImpreso?: number,
        public codPlantillaDes?: number,
        public id?: number
    ) {}
}
