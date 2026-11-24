export interface IDescripcionCodigo {
    codTabla?: string;
    codGenerico?: string;
    glsGenericacorta?: string;
    glsGenericalarga?: string;
    codRegStatus?: number;
    codRegLog?: number;
    id?: number;
}

export class DescripcionCodigo implements IDescripcionCodigo {
    constructor(
        public codTabla?: string,
        public codGenerico?: string,
        public glsGenericacorta?: string,
        public glsGenericalarga?: string,
        public codRegStatus?: number,
        public codRegLog?: number,
        public id?: number
    ) {
    }
}
