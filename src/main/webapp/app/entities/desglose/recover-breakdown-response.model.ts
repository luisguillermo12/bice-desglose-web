import { IExtraDataDesglose } from 'app/shared/model/extra-data-desglose.model';

export interface IBreakdownRecoverResponseDTO {
    extraDataDTO?: IExtraDataDesglose;
    codCliente?: string;
    codTipoDes?: string;
    codSubtipoDes?: string;
    codMoneda?: string;
    codPlantilla?: string;
    codUnidMoneda?: string;
    glsSubtipoDes?: string;
    codEstado?: string;
    glsEstado?: string;
}

export class BreakdownRecoverResponseDTO implements IBreakdownRecoverResponseDTO {
    constructor(
        public extraDataDTO?: IExtraDataDesglose,
        public codCliente?: string,
        public codTipoDes?: string,
        public codSubtipoDes?: string,
        public codMoneda?: string,
        public codPlantilla?: string,
        public codUnidMoneda?: string,
        public glsSubtipoDes?: string,
        public codEstado?: string,
        public glsEstado?: string
    ) {
    }
}
