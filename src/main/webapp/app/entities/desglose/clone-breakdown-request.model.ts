export interface IBreakdownCloneRequestDTO {
    codCliente?: string;
    codTipoDesOriginal?: string;
    codTipoDes?: string;
    codSubtipoDesOriginal?: string;
    codSubtipoDes?: string;
    codMonedaOriginal?: string;
    codMoneda?: string;
    codPlantillaDesgloseOriginal?: string;
}

export class BreakdownCloneRequestDTO implements IBreakdownCloneRequestDTO {
    constructor(
        public codCliente?: string,
        public codTipoDesOriginal?: string,
        public codTipoDes?: string,
        public codSubtipoDesOriginal?: string,
        public codSubtipoDes?: string,
        public codMonedaOriginal?: string,
        public codMoneda?: string,
        public codPlantillaDesgloseOriginal?: string
    ) {
    }
}
