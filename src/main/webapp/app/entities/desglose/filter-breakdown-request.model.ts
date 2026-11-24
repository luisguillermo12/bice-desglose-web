import { Moment } from 'moment';

export interface IBreakdownFilterRequestDTO {
    tipoFiltro?: string;
    rut?: string;
    rutVerificationNumber?: string;
    nombre?: string;
    tipoDesglose?: number;
    tipoPlantillaDesglose?: number;
    subTipoDesglose?: number;
    subTipoDesgloseDesc?: string;
    moneda?: number;
    fecCrea1?: Moment; // from
    fecCrea2?: Moment; // to
    fecDef1?: Moment; // from
    fecDef2?: Moment; // to
    fecRevi1?: Moment; // from
    fecRevi2?: Moment; // to
    fecEnvRevi1?: Moment; // from
    fecEnvRevi2?: Moment; // to
    nomUserModification?: string;
    nomAnalista?: string;
    sector?: number;
    codEstado?: string;
    loadPeriodsDates?: boolean;
}

export class BreakdownFilterRequestDTO implements IBreakdownFilterRequestDTO {
    constructor(
        public tipoFiltro?: string,
        public rut?: string,
        public rutVerificationNumber?: string,
        public nombre?: string,
        public tipoDesglose?: number,
        public tipoPlantillaDesglose?: number,
        public subTipoDesglose?: number,
        public subTipoDesgloseDesc?: string,
        public moneda?: number,
        public fecCrea1?: Moment,
        public fecCrea2?: Moment,
        public fecDef1?: Moment,
        public fecDef2?: Moment,
        public fecRevi1?: Moment,
        public fecRevi2?: Moment,
        public fecEnvRevi1?: Moment,
        public fecEnvRevi2?: Moment,
        public nomUserModification?: string,
        public nomAnalista?: string,
        public sector?: number,
        public codEstado?: string,
        public loadPeriodsDates?: boolean
    ) {
    }
}
