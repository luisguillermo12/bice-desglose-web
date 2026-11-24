export const enum OrderByDirection {
    ASC = 'ASC',
    DESC = 'DESC'
}

export interface IConceptRequestDTO {
    templateCode?: number;
    conceptCode?: string;
    conceptTypeCodesIn?: string[];
    orderByFields?: string[];
    orderByDirection?: OrderByDirection; // 'ASC' | 'DESC'
    columnLetterGlsFormulaVal1?: string;
    columnLetterGlsFormulaVal2?: string;
    columnLetterGlsFormulaVal3?: string;
    columnLetterGlsFormulaVal4?: string;
    columnLetterGlsFormulaPrc1?: string;
    columnLetterGlsFormulaPrc2?: string;
    columnLetterGlsFormulaPrc3?: string;
    columnLetterGlsFormulaPrc4?: string;
    columnLetterGlsFormulaDef1?: string;
    columnLetterGlsFormulaDef2?: string;
    columnLetterGlsFormulaDef3?: string;
    columnLetterGlsFormulaDef4?: string;
    cellNumMeses1?: string;
    cellNumMeses2?: string;
    cellNumMeses3?: string;
    cellNumMeses4?: string;
    cellIva?: string;
    cellDeflacBase?: string;
    cellDeflacPeriod1?: string;
    cellDeflacPeriod2?: string;
    cellDeflacPeriod3?: string;
    cellDeflacPeriod4?: string;
    flgConActivo?: number;
    templateCodeRelacionada?:number;
}

export class ConceptRequestDTO implements IConceptRequestDTO {
    constructor(
        public templateCode?: number,
        public conceptCode?: string,
        public conceptTypeCodesIn?: string[],
        public orderByFields?: string[],
        public orderByDirection?: OrderByDirection,
        public columnLetterGlsFormulaVal1?: string,
        public columnLetterGlsFormulaVal2?: string,
        public columnLetterGlsFormulaVal3?: string,
        public columnLetterGlsFormulaVal4?: string,
        public columnLetterGlsFormulaPrc1?: string,
        public columnLetterGlsFormulaPrc2?: string,
        public columnLetterGlsFormulaPrc3?: string,
        public columnLetterGlsFormulaPrc4?: string,
        public columnLetterGlsFormulaDef1?: string,
        public columnLetterGlsFormulaDef2?: string,
        public columnLetterGlsFormulaDef3?: string,
        public columnLetterGlsFormulaDef4?: string,
        public cellNumMeses1?: string,
        public cellNumMeses2?: string,
        public cellNumMeses3?: string,
        public cellNumMeses4?: string,
        public cellIva?: string,
        public cellDeflacBase?: string,
        public cellDeflacPeriod1?: string,
        public cellDeflacPeriod2?: string,
        public cellDeflacPeriod3?: string,
        public cellDeflacPeriod4?: string,
        public flgConActivo?: number,
        public templateCodeRelacionada?:number
    ) {
    }
}
