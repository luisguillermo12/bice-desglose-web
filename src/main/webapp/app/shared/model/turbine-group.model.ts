export interface ITurbineGroup {
    groupId?: number;
    groupName?: string;
    objectdataContentType?: string;
    objectdata?: any;
    id?: number;
}

export class TurbineGroup implements ITurbineGroup {
    constructor(
        public groupId?: number,
        public groupName?: string,
        public objectdataContentType?: string,
        public objectdata?: any,
        public id?: number
    ) {
    }
}
