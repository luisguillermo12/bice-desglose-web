import { Moment } from 'moment';
import { IValPeriodoDesglose } from 'app/shared/model/val-periodo-desglose.model';
import { IValPeriodoProyeccionDesglose } from 'app/shared/model/val-periodo-proyeccion-desglose.model';

export interface IPeriodo {
    id?: number;
    codCliente?: string;
    codTipoDes?: number;
    codSubtipoDes?: number;
    codMoneda?: string;
    codPlantilla?: number;
    fecPeriodo?: Moment;
    numMeses?: number;
    valDeflacPrd?: number;
    codAuditor?: string;
    glsOtrosAudit?: string;
    codEstadoPrd?: number;
    codEstadoAudi?: number;
    mtoSbifDdVig?: number;
    mtoSbifDdMor?: number;
    mtoSbifDdVen?: number;
    mtoSbifDdCas?: number;
    mtoSbifDiVig?: number;
    mtoSbifDiVen?: number;
    mtoSbifDiCas?: number;
    nomUserCrea?: string;
    fecUserCrea?: Moment;
    nomUserModif?: string;
    fecUserModif?: Moment;
    fecInformSbif?: Moment;
    valTipoCambio?: number;
    mtoSbifImp90179?: number;
    valPeriodoDesgloseDTOList?: IValPeriodoDesglose[];
    valPeriodoProyeccionDesgloseDTOList?: IValPeriodoProyeccionDesglose[];
    codEstadoPry?:string;
}

export class Periodo implements IPeriodo {
    constructor(
        public codCliente?: string,
        public codTipoDes?: number,
        public codSubtipoDes?: number,
        public codMoneda?: string,
        public fecPeriodo?: Moment,
        public numMeses?: number,
        public valDeflacPrd?: number,
        public codAuditor?: string,
        public glsOtrosAudit?: string,
        public codEstadoPrd?: number,
        public codEstadoAudi?: number,
        public mtoSbifDdVig?: number,
        public mtoSbifDdMor?: number,
        public mtoSbifDdVen?: number,
        public mtoSbifDdCas?: number,
        public mtoSbifDiVig?: number,
        public mtoSbifDiVen?: number,
        public mtoSbifDiCas?: number,
        public nomUserCrea?: string,
        public fecUserCrea?: Moment,
        public nomUserModif?: string,
        public fecUserModif?: Moment,
        public fecInformSbif?: Moment,
        public valTipoCambio?: number,
        public mtoSbifImp90179?: number,
        public valPeriodoDesgloseDTOList?: IValPeriodoDesglose[],
        public id?: number,
        public codPlantilla?: number,
        public  valPeriodoProyeccionDesgloseDTOList?: IValPeriodoProyeccionDesglose[],
        public codEstadoPry?:string
    ) {
    }
}
