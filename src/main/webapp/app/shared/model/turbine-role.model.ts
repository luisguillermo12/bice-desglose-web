export interface ITurbineRole {
    roleId?: number;
    roleName?: string;
    objectdataContentType?: string;
    objectdata?: any;
    id?: number;
}

export class TurbineRole implements ITurbineRole {
    constructor(
        public roleId?: number,
        public roleName?: string,
        public objectdataContentType?: string,
        public objectdata?: any,
        public id?: number
    ) {
    }
}
