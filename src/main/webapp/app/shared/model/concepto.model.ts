import { IBreakdownValidationError } from 'app/entities/desglose/breakdown-validation-error.model';

export interface IConcepto {
    codTipoPlantilla?: number;
    codTipoConcep?: string;
    corConcepto?: number;
    codConcepto?: string;
    glsConcepto?: string;
    numSecuencia?: number;
    numLvlIndent?: number;
    codTipoRegis?: string;
    glsFormulaValOriginal?: string;
    glsFormulaVal?: string;
    glsFormulaVal2?: string;
    glsFormulaVal3?: string;
    glsFormulaVal4?: string;
    glsFormulaPrcOriginal?: string;
    glsFormulaPrc?: string;
    glsFormulaPrc2?: string;
    glsFormulaPrc3?: string;
    glsFormulaPrc4?: string;
    glsFormulaDef?: string;
    glsFormulaDefOriginal?: string;
    glsFormulaDef2?: string;
    glsFormulaDef3?: string;
    glsFormulaDef4?: string;
    flgIsTotal?: any;
    flgUserModif?: any;
    flgConActivo?: any;
    numDecimales?: any;
    flgHaceAjust?: any;
    flgIsBold?: any;
    flgIsUcase?: any;
    flgIsEnclosed?: any;
    flgIsPrinted?: any;
    numDecPrint?: any;
    flgNonegativ?: any;
    flgAfterspace?: any;
    concepto?: any[];

    // Campos nuevos que vienen desde la UI para ser utilizados en los SP de guardar ValPrd
    corValor?: number;
    newValue?: number;
    newFormula?: string;
    newGlosa?: string;
    newPorcent?: number;

    expValidacionVal?: string;
    expValidacionPrc?: string;
    expValidacionDef?: string;
    msgErrValidacionVal?: string;
    msgErrValidacionPrc?: string;
    msgErrValidacionDef?: string;
    unidad?: string;
    updateFormula?:boolean;

    glsFormulaValWeb?: string;
    glsFormulaPrcWeb?: string;
    glsFormulaDefWeb?: string;
}

export class Concepto implements IConcepto {
    constructor(public codTipoPlantilla?: number, public codTipoConcep?: string, public corConcepto?: number, public codConcepto?: string, public glsConcepto?: string,
                public numSecuencia?: number, public numLvlIndent?: number, public codTipoRegis?: string,public glsFormulaValOriginal?: string, public glsFormulaVal?: string, public glsFormulaVal2?: string,
                public glsFormulaVal3?: string, public glsFormulaVal4?: string, public glsFormulaPrcOriginal?: string,public glsFormulaPrc?: string, public glsFormulaPrc2?: string, public glsFormulaPrc3?: string,
                public glsFormulaPrc4?: string, public glsFormulaDefOriginal?: string,public glsFormulaDef?: string, public glsFormulaDef2?: string, public glsFormulaDef3?: string, public glsFormulaDef4?: string,
                public flgIsTotal?: any, public flgUserModif?: any, public flgConActivo?: any, public numDecimales?: any, public flgHaceAjust?: any, public flgIsBold?: any,
                public flgIsUcase?: any, public flgIsEnclosed?: any, public flgIsPrinted?: any, public numDecPrint?: any, public flgNonegativ?: any, public flgAfterspace?: any,
                public concepto?: any[], public corValor?: number, public newValue?: number, public newFormula?: string, public newGlosa?: string, public newPorcent?: number,
                public expValidacionVal?: string, public expValidacionPrc?: string, public expValidacionDef?: string, public msgErrValidacionVal?: string,
                public msgErrValidacionPrc?: string, public msgErrValidacionDef?: string, public unidad?: string,public updateFormula?:boolean,
                public glsFormulaValWeb?: string, public glsFormulaPrcWeb?: string, public glsFormulaDefWeb?: string) {
        this.flgIsTotal = this.flgIsTotal || 0;
        this.flgUserModif = this.flgUserModif || 0;
        this.flgConActivo = this.flgConActivo || 0;
        this.numDecimales = this.numDecimales || 0;
        this.flgHaceAjust = this.flgHaceAjust || 0;
        this.flgIsBold = this.flgIsBold || 0;
        this.flgIsUcase = this.flgIsUcase || 0;
        this.flgIsEnclosed = this.flgIsEnclosed || 0;
        this.flgIsPrinted = this.flgIsPrinted || 0;
        this.numDecPrint = this.numDecPrint || 0;
        this.flgNonegativ = this.flgNonegativ || 0;
        this.flgAfterspace = this.flgAfterspace || 0;
        this.updateFormula = false;
    }
}
