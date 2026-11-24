export interface IConceptoImpresionRequestDTO {
    codCliente?: string;
    tipoDesglose?: number;
    subTipoDesglose?: number;
    moneda?: string;
}

export class ConceptoImpresionRequestDTO implements IConceptoImpresionRequestDTO {
    constructor(
        public  codCliente?: string,
        public  tipoDesglose?: number,
        public subTipoDesglose?: number,
        public  moneda?: string
    ) {
    }
}
