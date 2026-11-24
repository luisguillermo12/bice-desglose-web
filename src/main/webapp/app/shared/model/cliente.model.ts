import { IBreakdownSearchResponseDTO } from 'app/entities/desglose/search-breakdown-response.model';

export const enum ClientType {
    EMPRESA = '0',
    PERSONA_NATURAL = '1'
}

export interface ICliente {
    rut?: string;
    rutVerificationNumber?: string;
    name?: string;
    fatherLastName?: string;
    motherLastName?: string;
    activity?: string;
    clientCode?: string;
    clientType?: string;
    sector?: string;
    subSector?: string;
    identityType?: string;
    isCompany?: boolean;
    breakdownSearchResponseDTO?: IBreakdownSearchResponseDTO;
    warnings?: string[];
}

export class Cliente implements ICliente {
    constructor(
        public activity?: string,
        public rut?: string,
        public rutVerificationNumber?: string,
        public name?: string,
        public fatherLastName?: string,
        public motherLastName?: string,
        public clientCode?: string,
        public clientType?: string,
        public sector?: string,
        public subSector?: string,
        public identityType?: string,
        public isCompany?: boolean,
        public breakdownSearchResponseDTO?: IBreakdownSearchResponseDTO,
        public warnings?: string[]
    ) {}
}
