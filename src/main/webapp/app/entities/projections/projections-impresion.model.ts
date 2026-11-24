import { Moment } from 'moment';

export interface IDesgloseImpresionDTOProyections {
    codCliente?: string;
    codTipoPlantilla?: number;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    yearBase?: number;
    projectionYears?: number;
}

export class DesgloseImpresionDTOProyections implements DesgloseImpresionDTOProyections {
    constructor(
        public codCliente           ?: string,
        public codTipoPlantilla     ?: number,
        public codTipoDes           ?: number,
        public codSubtipoDes        ?: number,
        public codMoneda            ?: string,
        public yearBase             ?: number,
        public projectionYears      ?: number,
    ) {
    }
}
