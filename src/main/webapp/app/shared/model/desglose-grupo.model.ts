export interface IDesgloseGrupo {
    codCargo?: string;
    numRolEmpleado?: string;
    desCodCargo?: string;
    desNumRolEmpleado?: string;
    glsNumRolEmpleado?: string;
    id?: number;
}

export class DesgloseGrupo implements IDesgloseGrupo {
    constructor(
        public codCargo?: string,
        public numRolEmpleado?: string,
        public desCodCargo?: string,
        public desNumRolEmpleado?: string,
        public glsNumRolEmpleado?: string,
        public id?: number
    ) {
    }
}
