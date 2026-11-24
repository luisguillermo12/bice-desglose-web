import { Moment } from 'moment';

export interface IDesgloseRevisionLog {
    id?: number;
    fecEvent?: Moment;
    numLineaLog?: number;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    numRolEmpleado?: string;
    codEstado?: string;
    codEstadoOld?: string;
    numRolEmpReem?: string;
    glsComentarios?: string;
}

export class DesgloseRevisionLog implements IDesgloseRevisionLog {
    constructor(
        public id?: number,
        public fecEvent?: Moment,
        public numLineaLog?: number,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public numRolEmpleado?: string,
        public codEstado?: string,
        public codEstadoOld?: string,
        public numRolEmpReem?: string,
        public glsComentarios?: string
    ) {}
}
