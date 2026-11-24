import { Moment } from 'moment';

export interface IClienteNatural {
    id?: number;
    numCliente?: number;
    flgEmpleado?: string;
    nomPaterno?: string;
    nomMaterno?: string;
    nomNombres?: string;
    nomNickname?: string;
    codNacion?: string;
    codResidencia?: string;
    codProfesion?: number;
    fecNacim?: Moment;
    codSexo?: string;
    codNivEducac?: number;
    codEstcivil?: string;
    codSepBienes?: string;
    numCargas?: number;
    fecAntigViv?: Moment;
    fecDefuncion?: Moment;
    fecCertEcivi?: Moment;
    codRegStatus?: number;
    codRegLog?: number;
}

export class ClienteNatural implements IClienteNatural {
    constructor(
        public id?: number,
        public numCliente?: number,
        public flgEmpleado?: string,
        public nomPaterno?: string,
        public nomMaterno?: string,
        public nomNombres?: string,
        public nomNickname?: string,
        public codNacion?: string,
        public codResidencia?: string,
        public codProfesion?: number,
        public fecNacim?: Moment,
        public codSexo?: string,
        public codNivEducac?: number,
        public codEstcivil?: string,
        public codSepBienes?: string,
        public numCargas?: number,
        public fecAntigViv?: Moment,
        public fecDefuncion?: Moment,
        public fecCertEcivi?: Moment,
        public codRegStatus?: number,
        public codRegLog?: number
    ) {}
}
