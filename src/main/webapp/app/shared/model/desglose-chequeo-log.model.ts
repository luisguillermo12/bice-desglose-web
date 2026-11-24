import { Moment } from 'moment';

export interface IDesgloseChequeoLog {
    id?: number;
    codTipoProceso?: string;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    fecLog?: Moment;
    glsLog?: string;
}

export class DesgloseChequeoLog implements IDesgloseChequeoLog {
    constructor(
        public id?: number,
        public codTipoProceso?: string,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public fecLog?: Moment,
        public glsLog?: string
    ) {}
}
