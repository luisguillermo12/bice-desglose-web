export interface ITurbinePermission {
    permissionId?: number;
    permissionName?: string;
    objectdataContentType?: string;
    objectdata?: any;
    id?: number;
}

export class TurbinePermission implements ITurbinePermission {
    constructor(
        public permissionId?: number,
        public permissionName?: string,
        public objectdataContentType?: string,
        public objectdata?: any,
        public id?: number
    ) {
    }
}
