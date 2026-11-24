export interface IClienteSinRut {
    codCliente?: string;
    nomCliente?: string;
    nomPaterno?: string;
    nomMaterno?: string;
    codActividad?: string;
    actividad?: string;
}

export class ClienteSinRut implements IClienteSinRut {
    constructor(
        public codCliente?: string,
        public nomCliente?: string,
        public nomPaterno?: string,
        public nomMaterno?: string,
        public codActividad?: string,
        public actividad?: string
    ) {}
}
