import { Moment } from 'moment';

export interface IDesgloseRevision {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    rolDigitador?: string;
    rolRevisor?: string;
    rolRevisorReem?: string;
    glsNotificacion?: string;
    flgNotificado?: boolean;
    fecEnvioRevision?: Moment;
    fecEnRevision?: Moment;
    fecTerminoRevision?: Moment;
    codRegStatus?: number;
    codPlantilla?: string;
    id?: number;
}

export class DesgloseRevision implements IDesgloseRevision {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public rolDigitador?: string,
        public rolRevisor?: string,
        public rolRevisorReem?: string,
        public glsNotificacion?: string,
        public flgNotificado?: boolean,
        public fecEnvioRevision?: Moment,
        public fecEnRevision?: Moment,
        public fecTerminoRevision?: Moment,
        public codRegStatus?: number,
        public codPlantilla?: string,
        public id?: number
    ) {
        this.flgNotificado = this.flgNotificado || false;
    }
}
