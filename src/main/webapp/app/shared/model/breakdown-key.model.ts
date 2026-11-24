export interface IBreakdownKey {
    codTipoDes?: string;
    codSubtipoDes?: string;
    codMoneda?: string;
}

export class BreakdownKey implements IBreakdownKey {
    constructor(
        public codTipoDes?: string,
        public codSubtipoDes?: string,
        public codMoneda?: string
    ) {
    }
}
