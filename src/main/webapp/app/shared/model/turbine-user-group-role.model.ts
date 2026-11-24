export interface ITurbineUserGroupRole {
    userId?: number;
    groupId?: number;
    roleId?: number;
    id?: number;
}

export class TurbineUserGroupRole implements ITurbineUserGroupRole {
    constructor(
        public userId?: number,
        public groupId?: number,
        public roleId?: number,
        public id?: number
    ) {
    }
}
