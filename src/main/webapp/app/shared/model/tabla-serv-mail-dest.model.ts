export interface ITablaServMailDest {
    codSistema?: string;
    numSolicitud?: number;
    glsToNombre?: string;
    glsToMail?: string;
    codTipoDest?: string;
    id?: number;
}

export class TablaServMailDest implements ITablaServMailDest {
    constructor(
        public codSistema?: string,
        public numSolicitud?: number,
        public glsToNombre?: string,
        public glsToMail?: string,
        public codTipoDest?: string,
        public id?: number
    ) {
    }
}
