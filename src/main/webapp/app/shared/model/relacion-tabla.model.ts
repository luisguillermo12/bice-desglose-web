export interface IRelacionTabla {
    id?: number;
    numCorRegistro?: number;
    codTabla1?: string;
    codTabla2?: string;
    codGenerico1?: string;
    codGenerico2?: string;
    codRegStatus?: number;
    codRegLog?: number;
}

export class RelacionTabla implements IRelacionTabla {
    constructor(
        public id?: number,
        public numCorRegistro?: number,
        public codTabla1?: string,
        public codTabla2?: string,
        public codGenerico1?: string,
        public codGenerico2?: string,
        public codRegStatus?: number,
        public codRegLog?: number
    ) {}
}
