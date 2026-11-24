import { Moment } from 'moment';

export interface IDesgloseMensaje {
    fecEvent?: Moment;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    numRolEmpleado?: string;
    glsComentarios?: string;
    codPlantilla?: number;
    id?: number;
}

export class DesgloseMensaje implements IDesgloseMensaje {
    constructor(
        public fecEvent?: Moment,
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public numRolEmpleado?: string,
        public glsComentarios?: string,
        public codPlantilla?: number,
        public id?: number
    ) {
    }
}
