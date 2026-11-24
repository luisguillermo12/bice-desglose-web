import { Moment } from 'moment';

export interface IPeriodDeleteRequestDTO {
    tcodCliente?: string;
    tipoDesglose?: number;
    tipoPlantillaDesglose?: string;
    subTipoDesglose?: number;
    moneda?: string;
    fecPeriodo?: Moment;
    codPlantilla?: number;
}

export class PeriodDeleteRequestDTO implements IPeriodDeleteRequestDTO {
    constructor(
        public codCliente?: string,
        public tipoDesglose?: number,
        public tipoPlantillaDesglose?: string,
        public subTipoDesglose?: number,
        public moneda?: string,
        public fecPeriodo?: Moment,
        public codPlantilla?: number
    ) {
    }
}
