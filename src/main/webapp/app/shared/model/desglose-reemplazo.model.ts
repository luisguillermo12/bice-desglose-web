import { Moment } from 'moment';

export interface IDesgloseReemplazo {
    id?: number;
    codCargo?: string;
    numRolEmpleado?: string;
    desCodCargo?: string;
    desNumRolEmpleado?: string;
    fecReemplazo?: Moment;
    fecElimReemplazo?: Moment;
    numRolEmpleadoCrea?: string;
    numRolEmpleadoElim?: string;
    codRegStatus?: number;
}

export class DesgloseReemplazo implements IDesgloseReemplazo {
    constructor(
        public id?: number,
        public codCargo?: string,
        public numRolEmpleado?: string,
        public desCodCargo?: string,
        public desNumRolEmpleado?: string,
        public fecReemplazo?: Moment,
        public fecElimReemplazo?: Moment,
        public numRolEmpleadoCrea?: string,
        public numRolEmpleadoElim?: string,
        public codRegStatus?: number
    ) {}
}
