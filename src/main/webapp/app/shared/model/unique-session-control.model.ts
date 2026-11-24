import { Moment } from 'moment';

export interface IUniqueSessionControl {
    id?: number;
    loginName?: string;
    sessionCheckTime?: Moment;
    sessionEndTime?: Moment;
    jwt?: string;
}

export class UniqueSessionControl implements IUniqueSessionControl {
    constructor(
        public id?: number,
        public loginName?: string,
        public sessionCheckTime?: Moment,
        public sessionEndTime?: Moment,
        public jwt?: string
    ) {
    }
}
