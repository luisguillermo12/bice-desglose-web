import { Moment } from 'moment';

export interface IBreakdownSearchResponseDTO {
    codMonedaImp?: string;
    codUnidadMon?: string;
    fecDeflacBase?: Moment;
    fechaModif?: Moment;
    flgIsTemporal?: boolean;
    flgReq2Firma?: boolean;
    glsEjecutor?: string;
    glsObservacion?: string;
    glsTipoDes?: string;
    nomUser?: string;
    nomUserCrea?: string;
    userModif?: string;
    userOpen?: string;
    usrEjecutor?: string;
    valDeflacBase?: number;
    valIva?: number;
    codEstado?: string;
    glsEstado?: string;
    numRol1Firm?: string;
    numRol2Firm?: string;
    numRolRee?: string;
    nomUserOpen?: string;
    validado?: boolean;
    fecPeriodo?: Moment;
    nomEmpresa?: string;
    valorPeriodo?: number;
}

export class BreakdownSearchResponseDTO implements IBreakdownSearchResponseDTO {
    constructor(
        public codMonedaImp?: string,
        public codUnidadMon?: string,
        public fecDeflacBase?: Moment,
        public fechaModif?: Moment,
        public flgIsTemporal?: boolean,
        public flgReq2Firma?: boolean,
        public glsEjecutor?: string,
        public glsObservacion?: string,
        public glsTipoDes?: string,
        public nomUser?: string,
        public nomUserCrea?: string,
        public userModif?: string,
        public userOpen?: string,
        public usrEjecutor?: string,
        public valDeflacBase?: number,
        public valIva?: number,
        public codEstado?: string,
        public glsEstado?: string,
        public numRol1Firm?: string,
        public numRol2Firm?: string,
        public numRolRee?: string,
        public nomUserOpen?: string,
        public validado?: boolean,
        public fecPerido?: Moment,
        public nomEmpresa?: string,
        public valorPeriodo?: number
    ) {
    }
}
