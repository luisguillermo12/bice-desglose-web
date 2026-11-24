import { Moment } from 'moment';

export interface IValPeriodoProyeccionDesglose {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codPlantilla?: number;
    fecPeriodo?: Moment;
    codTipoConcepPry?: string;
    corConceptoPry?: number;
    valConceptoPry?: number;
    glsFrmlConcepPry?: string;
    codConcepto?: string;
    codPlantillaPry?: number;
    fecPeriodoPry?: Moment;
}

export class ValPeriodoProyeccionDesglose implements IValPeriodoProyeccionDesglose {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public codPlantilla?: number,
        public fecPeriodo?: Moment,
        public codTipoConcepPry?: string,
        public corConceptoPry?: number,
        public valConceptoPry?: number,
        public lsFrmlConcepPry?: string,
        public codConcepto?: string,
        public codPlantillaPry?: number,
        public fecPeriodoPry?: Moment
    ) {
    }
}
