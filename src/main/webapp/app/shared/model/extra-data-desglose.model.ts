import { Moment } from 'moment';
import { isNullOrUndefined } from 'util';
import { IConceptoImpresion } from 'app/shared/model/concepto-impresion.model';
import { IPeriodo } from 'app/shared/model/periodo.model';
import { IConcepto } from 'app/shared/model/concepto.model';
import { IEmpresaConsolidada } from 'app/shared/model/empresa-consolidada.model';
import { IPeriodoDesgConsol } from 'app/shared/model/periodo-desg-consol.model';
import { IEmprConsolPeriodoDesgConsol } from 'app/shared/model/empr-consol-periodo-desg-consol.model';
import { IBreakdownAuditor } from 'app/entities/desglose/breakdown-auditor.model';

export interface IExtraDataDesglose {
    numPeriodosLoaded?: number;
    numPeriodosConInfo?: number;
    monthsPeriod?: number[];
    deflatorPeriod?: number[];
    deflatorPeriodStr?: string[];
    periodRating?: string[];
    fecPeriodo?: Moment[];
    enProceso?: boolean;
    validado?: boolean;
    isAutoriced?: boolean;
    isTemporal?: boolean;
    userOpen?: string;
    nomUserOpen?: string;
    requireValidation?: boolean;
    canValidate?: boolean;
    canLoadSBIF?: boolean;
    /**
     * Conceptos de impresión en forma de lista de objetos en vez de una matriz.
     */
    matrizConImp?: IConceptoImpresion[];
    periodos?: IPeriodo[];
    conceptos?: IConcepto[];
    conceptos1?:IConcepto[];
    conceptos2?:IConcepto[];
    empresaConsolidadas?: IEmpresaConsolidada[];
    periodoDesgConsols?: IPeriodoDesgConsol[];
    /**
     * Unifica los datos de empresaConsolidadas y periodoDesgConsols
     */
    empConsolPeriodoDesgConsolList?: IEmprConsolPeriodoDesgConsol[];
    auditors?: IBreakdownAuditor[];
    data?: any;
    data1?: any;
    visualData?: any;
    visualData1?: any;
}

export class ExtraDataDesglose implements IExtraDataDesglose {
    constructor(
        public numPeriodosLoaded?: number,
        public numPeriodosConInfo?: number,
        public monthsPeriod?: number[],
        public deflatorPeriod?: number[],
        public periodRating?: string[],
        public fecPeriodo?: Moment[],
        public enProceso?: boolean,
        public validado?: boolean,
        public isAutoriced?: boolean,
        public isTemporal?: boolean,
        public userOpen?: string,
        public nomUserOpen?: string,
        public requireValidation?: boolean,
        public canValidate?: boolean,
        public canLoadSBIF?: boolean,
        public matrizConImp?: IConceptoImpresion[],
        public periodos?: IPeriodo[],
        public conceptos?: IConcepto[],
        public conceptos1?: IConcepto[],
        public conceptos2?: IConcepto[],
        public empresaConsolidadas?: IEmpresaConsolidada[],
        public periodoDesgConsols?: IPeriodoDesgConsol[],
        public empConsolPeriodoDesgConsolList?: IEmprConsolPeriodoDesgConsol[],
        public auditors?: IBreakdownAuditor[],
        public data?: any,
        public visualData?: any,
        public deflatorPeriodStr?: string[]
    ) {
        if (isNullOrUndefined(this.monthsPeriod)) {
            this.monthsPeriod = [];
        }
        if (isNullOrUndefined(this.deflatorPeriod)) {
            this.deflatorPeriod = [];
        }
        if (isNullOrUndefined(this.deflatorPeriodStr)) {
            this.deflatorPeriodStr = [];
        }
        if (isNullOrUndefined(this.periodRating)) {
            this.periodRating = [];
        }
        if (isNullOrUndefined(this.fecPeriodo)) {
            this.fecPeriodo = [];
        }
        if (isNullOrUndefined(this.matrizConImp)) {
            this.matrizConImp = [];
        }
        if (isNullOrUndefined(this.periodos)) {
            this.periodos = [];
        }
        if (isNullOrUndefined(this.conceptos)) {
            this.conceptos = [];
        }
        if (isNullOrUndefined(this.conceptos1)) {
            this.conceptos1 = [];
        }
        if (isNullOrUndefined(this.conceptos2)) {
            this.conceptos2 = [];
        }
        if (isNullOrUndefined(this.empresaConsolidadas)) {
            this.empresaConsolidadas = [];
        }
        if (isNullOrUndefined(this.periodoDesgConsols)) {
            this.periodoDesgConsols = [];
        }
        if (isNullOrUndefined(this.empConsolPeriodoDesgConsolList)) {
            this.empConsolPeriodoDesgConsolList = [];
        }
        if (isNullOrUndefined(this.auditors)) {
            this.auditors = [];
        }
        if (isNullOrUndefined(this.data)) {
            this.data = [ [] ];
        }
        if (isNullOrUndefined(this.visualData)) {
            this.visualData = [ [] ];
        }
    }
}
