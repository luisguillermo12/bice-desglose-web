export interface IDesgloseFirma {
    codCargo?: string;
    numRolEmpleado?: string;
    numRolEmpleado2fir?: string;
    id?: number;
}

export class DesgloseFirma implements IDesgloseFirma {
    constructor(
        public codCargo?: string,
        public numRolEmpleado?: string,
        public numRolEmpleado2fir?: string,
        public id?: number
    ) {
    }
}
