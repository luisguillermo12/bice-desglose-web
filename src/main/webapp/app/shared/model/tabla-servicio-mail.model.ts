import { Moment } from 'moment';

export interface ITablaServicioMail {
    codSistema?: string;
    numSolicitud?: number;
    fecSolicitud?: Moment;
    numTrxSistema?: number;
    glsFromNombre?: string;
    glsFromMail?: string;
    glsAsunto?: string;
    glsMensaje?: number;
    fecEnvMail?: Moment;
    numIntento?: number;
    numMaxReintento?: number;
    codEstado?: string;
    glsDetRespuesta?: string;
    glsContentType?: string;
    id?: number;
}

export class TablaServicioMail implements ITablaServicioMail {
    constructor(
        public codSistema?: string,
        public numSolicitud?: number,
        public fecSolicitud?: Moment,
        public numTrxSistema?: number,
        public glsFromNombre?: string,
        public glsFromMail?: string,
        public glsAsunto?: string,
        public glsMensaje?: number,
        public fecEnvMail?: Moment,
        public numIntento?: number,
        public numMaxReintento?: number,
        public codEstado?: string,
        public glsDetRespuesta?: string,
        public glsContentType?: string,
        public id?: number
    ) {
    }
}
