export interface ICuadre {
    codTpoConcep1?: string;
    corConcepto1?: number;
    codTpoConcep2?: string;
    corConcepto2?: number;
    glsMensaje?: string;
}

export class Cuadre implements ICuadre {
    constructor(
        public codTpoConcep1?: string,
        public corConcepto1?: number,
        public codTpoConcep2?: string,
        public corConcepto2?: number,
        public glsMensaje?: string
    ) {}
}
