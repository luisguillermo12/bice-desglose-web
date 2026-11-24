export interface ITableValue {
    corConcept?: string;
    conceptName?: string;
    period1Value?: number;
    period1Percent?: number;
    period2Value?: number;
    period2Percent?: number;
    period3Value?: number;
    period3Percent?: number;
    period4Value?: number;
    period4Percent?: number;
    isTotal?: boolean;
    isHeader?: boolean;
    codTipoConcepto?: string;
    corConcept1?: number;
    flgIsBold?: number;
    flgIsEnclosed?: number;
}

export class TableValue implements ITableValue {
    constructor(
        public corConcept?: string,
        public conceptName?: string,
        public period1Value?: number,
        public period1Percent?: number,
        public period2Value?: number,
        public period2Percent?: number,
        public period3Value?: number,
        public period3Percent?: number,
        public period4Value?: number,
        public period4Percent?: number,
        public isTotal?: boolean,
        public isHeader?: boolean,
        public codTipoConcepto?: string,
        public corConcept1?: number,
        public flgIsBold?: number,
        public flgIsEnclosed?: number,
    ) {
    }
}
