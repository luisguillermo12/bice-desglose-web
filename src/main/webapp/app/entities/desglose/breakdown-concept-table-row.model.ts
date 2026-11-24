export interface IBreakdownConceptTableRow {
    codConcepto?: string;
    glsConcepto?: string;
    glsFormulaVal?: string;
    glsFormulaPrc?: string;
}

export class BreakdownConceptTableRow implements IBreakdownConceptTableRow {
    constructor(
        public codConcepto?: string,
        public glsConcepto?: string,
        public glsFormulaVal?: string,
        public glsFormulaPrc?: string
    ) {
    }
}
