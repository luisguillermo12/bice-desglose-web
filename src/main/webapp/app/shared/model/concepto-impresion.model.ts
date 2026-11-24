export interface IConceptoImpresion {
    codCliente?: string;
    codPlantilla?: number;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    tipoConcep?: string;
    codTipoConcep?: string;
    corConcepto?: string;
    prc?: number;
    prc1?: number;
    codConcepto?:string;
}

export class ConceptoImpresion implements IConceptoImpresion {
    constructor(
        public codCliente?: string,
        public codPlantilla?: number,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public tipoConcep?: string,
        public codTipoConcep?: string,
        public corConcepto?: string,
        public prc?: number,
        public prc1?: number,
        public codConcepto?: string
    ) {
    }
}
