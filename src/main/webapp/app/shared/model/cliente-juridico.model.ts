import { Moment } from 'moment';

export interface IClienteJuridico {
    id?: number;
    numCliente?: number;
    glsRazonSoci?: string;
    nomFantasia?: string;
    codGrupoEcon?: number;
    fecConstitucion?: Moment;
    codTipoCliempresa?: string;
    glsWeb?: string;
    codRegStatus?: number;
    codRegLog?: number;
    glsRazonSocialLegal?: string;
}

export class ClienteJuridico implements IClienteJuridico {
    constructor(
        public id?: number,
        public numCliente?: number,
        public glsRazonSoci?: string,
        public nomFantasia?: string,
        public codGrupoEcon?: number,
        public fecConstitucion?: Moment,
        public codTipoCliempresa?: string,
        public glsWeb?: string,
        public codRegStatus?: number,
        public codRegLog?: number,
        public glsRazonSocialLegal?: string
    ) {}
}
