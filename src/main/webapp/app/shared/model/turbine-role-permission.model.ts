export interface ITurbineRolePermission {
    roleId?: number;
    permissionId?: number;
    id?: number;
}

export class TurbineRolePermission implements ITurbineRolePermission {
    constructor(
        public roleId?: number,
        public permissionId?: number,
        public id?: number
    ) {
    }
}
