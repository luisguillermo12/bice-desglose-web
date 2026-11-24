import { Moment } from 'moment';

export interface IDesglose {
    codCliente?: string;
    codTipoDes?: string;
    codPlantilla?: string;
    codSubtipoDes?: string;
    codMoneda?: string;
    codUnidMoneda?: string;
    glsSubtipoDes?: string;
    valDeflacBaseStr?: string;
    valDeflacBase?: number;
    valIva?: number;
    nomUserAnalis?: string; // nombre del usuario que tiene ocupado el desglose
    glsObservacion?: string;
    nomUserCrea?: string;
    fecUserCrea?: Moment;
    nomUserModif?: string;
    fecUserModif?: Moment;
    fecDeflacBase?: Moment;
    nomUserOpen?: string; // login del usuario que tiene ocupado el desglose
    flgIsTemporal?: boolean;
    codMonedaImp?: string;
    fecHraOpen?: Moment;
    nomUser?: string; // login del usuario que tiene ocupado el desglose
    flgValidacion?: string;
    flgRevision?: string;
    glsComentValid?: string;
    codEstado?: string;
    rolEmpleadoDesDef?: string;
    nomRevisorDef?: string;
    fecDesgloDef?: Moment;
    rolEmpleadoRevMod?: string;
    rolEmpleadoDefEnvPdf?: string;
    fecDesgloEnvPdf?: Moment;
    codMonedaRpt?: number;
    corImpresionRpt?: number;
    flgEjeFactoringRpt?: string;
    fecPeriodo1Rpt?: Moment;
    fecPeriodo2Rpt?: Moment;
    fecPeriodo3Rpt?: Moment;
    fecPeriodo4Rpt?: Moment;
    fecPerConsol1Rpt?: Moment;
    fecPerConsol2Rpt?: Moment;
    fecUlt1Rpt?: Moment;
    fecUlt2Rpt?: Moment;
    fecUserModifRpt?: Moment;
    nomUserModifRpt?: string;
    numReporteRpt?: number;
    fecGeneracionRpt?: Moment;
    numDevolEjecutor?: number;
    numRolChgEstado?: string;
    flgReq2firma?: boolean;
    glsEstado?: string;
    codPlantillaProyeccion?:number;
}

export class Desglose implements IDesglose {
    constructor(public codCliente?: string, public codTipoDes?: string,
                public codPlantilla?: string, public codSubtipoDes?: string, public codMoneda?: string, public codUnidMoneda?: string, public glsSubtipoDes?: string,
                public valDeflacBase?: number, public valIva?: number, public nomUserAnalis?: string, public glsObservacion?: string, public nomUserCrea?: string,
                public fecUserCrea?: Moment, public nomUserModif?: string, public fecUserModif?: Moment, public fecDeflacBase?: Moment, public nomUserOpen?: string,
                public flgIsTemporal?: boolean, public codMonedaImp?: string, public fecHraOpen?: Moment, public nomUser?: string, public flgValidacion?: string,
                public flgRevision?: string, public glsComentValid?: string, public codEstado?: string, public rolEmpleadoDesDef?: string, public nomRevisorDef?: string,
                public fecDesgloDef?: Moment, public rolEmpleadoRevMod?: string, public rolEmpleadoDefEnvPdf?: string, public fecDesgloEnvPdf?: Moment, public codMonedaRpt?: number,
                public corImpresionRpt?: number, public flgEjeFactoringRpt?: string, public fecPeriodo1Rpt?: Moment, public fecPeriodo2Rpt?: Moment, public fecPeriodo3Rpt?: Moment,
                public fecPeriodo4Rpt?: Moment, public fecPerConsol1Rpt?: Moment, public fecPerConsol2Rpt?: Moment, public fecUlt1Rpt?: Moment, public fecUlt2Rpt?: Moment,
                public fecUserModifRpt?: Moment, public nomUserModifRpt?: string, public numReporteRpt?: number, public fecGeneracionRpt?: Moment, public numDevolEjecutor?: number,
                public numRolChgEstado?: string, public flgReq2firma?: boolean, public glsEstado?: string, public valDeflacBaseStr?: string,public codPlantillaProyeccion?:number
    ) {
        this.flgIsTemporal = this.flgIsTemporal || false;
    }
}
