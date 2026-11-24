import { Moment } from 'moment';

export interface IDesgloseAuditoria {
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    cod_plantilla?: number;
    fecEvento?: Moment;
    numLinea?: number;
    codEvento?: string;
    rolEmpleadoAccion?: string;
    glsRol?: string;
    codEstadoOri?: string;
    codEstadoNvo?: string;
    rolEmpleadoOri?: string;
    rolEmpleadoNvo?: string;
    flgResultValid?: string;
    glsComentario?: string;
    id?: number;
}

export class DesgloseAuditoria implements IDesgloseAuditoria {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public cod_plantilla?: number,
        public fecEvento?: Moment,
        public numLinea?: number,
        public codEvento?: string,
        public rolEmpleadoAccion?: string,
        public glsRol?: string,
        public codEstadoOri?: string,
        public codEstadoNvo?: string,
        public rolEmpleadoOri?: string,
        public rolEmpleadoNvo?: string,
        public flgResultValid?: string,
        public glsComentario?: string,
        public id?: number
    ) {
    }
}
