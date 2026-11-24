import { Moment } from 'moment';

export interface IBreakdownFilterResponseDTO {
    codCliente?: string;
    nomCliente?: string;
    isEmpresa?: boolean;
    hasRut?: boolean;
    codActividad?: string;
    codSector?: string;
    glsSector?: string;
    codTipoDes?: string;
    glsTipoDes?: string;
    codSubTipoDes?: string;
    glsSubTipoDes?: string;
    codMoneda?: string;
    glsMoneda?: string;
    codMonedaImp?: string;
    glsMonedaImp?: string;
    codUnidadMon?: string;
    glsUnidadMon?: string;
    fechaDesglose?: Moment;
    userAnalista?: string;
    userOpen?: string;
    nombreAnalista?: string;
    nombreUserOpen?: string;
    codEstado?: string;
    glsEstado?: string;
    codPlantillaDesglose?: string;
    glsPlantillaDesglose?: string;
    fechaEnvioRevision?: Moment;
    fechaRevision?: Moment;
    nombreRevisor?: string;
    fechaPeriodos?: Moment[];
    rolEmpleadoDefEnvPdf?: string;

    codClienteFormatted?: string;
    flgTipoCliente?: string;

    fechaDesglosefechaDesglose?: Moment;
    userCreacion?: string;
    nombreUserCreacion?: string;

    fechaLastUpdate?: Moment;
    userUpdate?: string;
    nombreUserUpdate?: string;

    fechaDesgloDef?: Moment;
    nombreRevisorDef?: string;
    nombreRevisorModif?: string;
}

export class BreakdownFilterResponseDTO implements IBreakdownFilterResponseDTO {
    constructor(
        public codCliente?: string,
        public nomCliente?: string,
        public isEmpresa?: boolean,
        public hasRut?: boolean,
        public codActividad?: string,
        public codSector?: string,
        public glsSector?: string,
        public codTipoDes?: string,
        public glsTipoDes?: string,
        public codSubTipoDes?: string,
        public glsSubTipoDes?: string,
        public codMoneda?: string,
        public glsMoneda?: string,
        public codMonedaImp?: string,
        public glsMonedaImp?: string,
        public codUnidadMon?: string,
        public glsUnidadMon?: string,
        public fechaDesglose?: Moment,
        public fechaLastUpdate?: Moment,
        public userAnalista?: string,
        public nombreAnalista?: string,
        public codEstado?: string,
        public glsEstado?: string,
        public codPlantillaDesglose?: string,
        public glsPlantillaDesglose?: string,
        public fechaEnvioRevision?: Moment,
        public fechaRevision?: Moment,
        public nombreRevisor?: string,
        public fechaPeriodos?: Moment[],
        public rolEmpleadoDefEnvPdf?: string,
        public codClienteFormatted?: string,
        public flgTipoCliente?: string,
        public fechaDesglosefechaDesglose?: Moment,
        public userCreacion?: string,
        public nombreUserCreacion?: string,
        public userUpdate?: string,
        public nombreUserUpdate?: string,
        public fechaDesgloDef?: Moment,
        public nombreRevisorDef?: string,
        public nombreRevisorModif?: string,
        public userOpen?: string,
        public nombreUserOpen?: string
    ) {
    }
}
