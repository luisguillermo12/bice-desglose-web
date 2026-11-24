import { Moment } from 'moment';

export interface IDesgloseChequeoInfo {
    id?: number;
    fecCheck?: Moment;
    codTipoInforme?: string;
    glsFactorInforme?: string;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    fecPeriodo?: Moment;
    numRank?: number;
    fecUserModif?: Moment;
    glsUserModif?: string;
    valDeflacBase?: number;
    valDeflacPeriodo?: number;
    valDeflacCalc?: number;
    valConceptoBd?: number;
    valConceptoOnline?: number;
    glsConceptoFormula?: string;
    valDif?: number;
    mtoActivo?: number;
    mtoPasivoPatr?: number;
    corConcepto?: number;
    corValor?: number;
    codTipoConcep?: string;
    codEstado?: string;
    glsObservacion?: string;
}

export class DesgloseChequeoInfo implements IDesgloseChequeoInfo {
    constructor(
        public id?: number,
        public fecCheck?: Moment,
        public codTipoInforme?: string,
        public glsFactorInforme?: string,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public fecPeriodo?: Moment,
        public numRank?: number,
        public fecUserModif?: Moment,
        public glsUserModif?: string,
        public valDeflacBase?: number,
        public valDeflacPeriodo?: number,
        public valDeflacCalc?: number,
        public valConceptoBd?: number,
        public valConceptoOnline?: number,
        public glsConceptoFormula?: string,
        public valDif?: number,
        public mtoActivo?: number,
        public mtoPasivoPatr?: number,
        public corConcepto?: number,
        public corValor?: number,
        public codTipoConcep?: string,
        public codEstado?: string,
        public glsObservacion?: string
    ) {}
}
