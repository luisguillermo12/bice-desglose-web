import { Moment } from 'moment';

export interface IValPeriodoDesglose {
    id?: number;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codPlantilla?: number;
    fecPeriodo?: Moment;
    codTipoConcep?: string;
    corConcepto?: number;
    corValor?: number;
    valConcepto?: number;
    glsFrmlConcep?: string;
    glsConcepto?: string;
    valPrcConcep?: number;
    codConcepto?: string;
}

export class ValPeriodoDesglose implements IValPeriodoDesglose {
    constructor(
        public id?: number,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public fecPeriodo?: Moment,
        public codTipoConcep?: string,
        public corConcepto?: number,
        public corValor?: number,
        public valConcepto?: number,
        public glsFrmlConcep?: string,
        public glsConcepto?: string,
        public valPrcConcep?: number,
        public codConcepto?: string,
        public codPlantilla?: number
    ) {
    }
}
