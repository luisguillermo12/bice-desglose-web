import { Moment } from 'moment';

export interface IBlackListJwt {
    id?: number;
    invalidateTime?: Moment;
    jwt?: string;
}

export class BlackListJwt implements IBlackListJwt {
    constructor(public id?: number, public invalidateTime?: Moment, public jwt?: string) {
    }
}
