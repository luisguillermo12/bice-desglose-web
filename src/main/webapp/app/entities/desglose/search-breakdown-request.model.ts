export interface IBreakdownSearchRequestDTO {
    codCliente?: string;
    tipoDesglose?: number;
    tipoPlantillaDesglose?: string;
    subTipoDesglose?: number;
    moneda?: string;
}

export class BreakdownSearchRequestDTO implements IBreakdownSearchRequestDTO {
    constructor(
        public codCliente?: string,
        public tipoDesglose?: number,
        public tipoPlantillaDesglose?: string,
        public subTipoDesglose?: number,
        public moneda?: string
    ) {
    }
}
