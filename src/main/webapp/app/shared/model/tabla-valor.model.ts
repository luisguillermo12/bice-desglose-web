import { Moment } from 'moment';

export interface ITablaValor {
    id?: number;
    codTabla?: string;
    codGenerico?: string;
    fecValidez?: Moment;
    valor?: number;
    codTipovalor?: number;
    codRegStatus?: number;
    codRegLog?: number;
}

export class TablaValor implements ITablaValor {
    constructor(
        public id?: number,
        public codTabla?: string,
        public codGenerico?: string,
        public fecValidez?: Moment,
        public valor?: number,
        public codTipovalor?: number,
        public codRegStatus?: number,
        public codRegLog?: number
    ) {}
}
