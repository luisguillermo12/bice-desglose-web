import { Moment } from 'moment';

export interface IIdentificacionCliente {
    id?: number;
    numCliente?: number;
    codTipoIden?: number;
    numIden?: string;
    fecCreacion?: Moment;
    codRegStatus?: number;
    codRegLog?: number;
}

export class IdentificacionCliente implements IIdentificacionCliente {
    constructor(
        public id?: number,
        public numCliente?: number,
        public codTipoIden?: number,
        public numIden?: string,
        public fecCreacion?: Moment,
        public codRegStatus?: number,
        public codRegLog?: number
    ) {}
}
