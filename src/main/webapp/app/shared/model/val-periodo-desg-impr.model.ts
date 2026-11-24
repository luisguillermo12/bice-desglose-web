import { Moment } from 'moment';

export interface IValPeriodoDesgImpr {
    id?: number;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codMonedaImp?: string;
    fecPeriodo?: Moment;
    codTipoConcep?: string;
    corConcepto?: number;
    corValor?: number;
    valConcepto?: number;
    corImpreso?: number;
    cod_tipo_plantilla_des?: number;
}

export class ValPeriodoDesgImpr implements IValPeriodoDesgImpr {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public codMonedaImp?: string,
        public fecPeriodo?: Moment,
        public codTipoConcep?: string,
        public corConcepto?: number,
        public corValor?: number,
        public valConcepto?: number,
        public corImpreso?: number,
        public cod_tipo_plantilla_des?: number,
        public id?: number
    ) {}
}
