import { Moment } from 'moment';

export interface IClienteBasico {
    id?: number;
    numCliente?: number;
    numRolEmpleado?: string;
    codTipoJuridi?: number;
    codSucursal?: number;
    codCliente?: string;
    codTipoCliente?: number;
    codSectorEconom?: number;
    fecCliente?: Moment;
    numCorDomlegal?: number;
    codActividad?: number;
    codSector?: number;
    codCatdeu?: number;
    codRelaBice?: number;
    codGrupRela?: number;
    codEstaDeud?: number;
    numCarpeta?: number;
    numCasiInte?: number;
    glsSinoSbif?: string;
    fecDeclJurada?: Moment;
    fecVerifDomlegal?: Moment;
    codRegStatus?: number;
    codRegLog?: number;
    flgFirmaCobprejud?: string;
    fecFirmaCobprejud?: Moment;
    numTaxId?: string;
    codSiiExeimp?: string;
    flgSiiContprimeracat?: string;
    codTipoAlerta?: string;
    fecAlerta?: Moment;
    flgMandatoInv?: string;
    codGirosii?: number;
    codAlertaFrd?: string;
}

export class ClienteBasico implements IClienteBasico {
    constructor(
        public id?: number,
        public numCliente?: number,
        public numRolEmpleado?: string,
        public codTipoJuridi?: number,
        public codSucursal?: number,
        public codCliente?: string,
        public codTipoCliente?: number,
        public codSectorEconom?: number,
        public fecCliente?: Moment,
        public numCorDomlegal?: number,
        public codActividad?: number,
        public codSector?: number,
        public codCatdeu?: number,
        public codRelaBice?: number,
        public codGrupRela?: number,
        public codEstaDeud?: number,
        public numCarpeta?: number,
        public numCasiInte?: number,
        public glsSinoSbif?: string,
        public fecDeclJurada?: Moment,
        public fecVerifDomlegal?: Moment,
        public codRegStatus?: number,
        public codRegLog?: number,
        public flgFirmaCobprejud?: string,
        public fecFirmaCobprejud?: Moment,
        public numTaxId?: string,
        public codSiiExeimp?: string,
        public flgSiiContprimeracat?: string,
        public codTipoAlerta?: string,
        public fecAlerta?: Moment,
        public flgMandatoInv?: string,
        public codGirosii?: number,
        public codAlertaFrd?: string
    ) {}
}
