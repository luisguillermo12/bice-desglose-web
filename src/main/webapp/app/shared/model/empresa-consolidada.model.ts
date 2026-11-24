export interface IEmpresaConsolidada {
    id?: number;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    corEmpresa?: number;
    nomEmpresa?: string;
}

export class EmpresaConsolidada implements IEmpresaConsolidada {
    constructor(
        public id?: number,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public corEmpresa?: number,
        public nomEmpresa?: string
    ) {
    }
}
