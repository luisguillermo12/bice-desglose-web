import { Moment } from 'moment';

export interface ITurbineUser {
    userId?: number;
    loginName?: string;
    passwordValue?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    confirmValue?: string;
    modified?: Moment;
    created?: Moment;
    lastLogin?: Moment;
    expire?: Moment;
    expirePassword?: Moment;
    objectdataContentType?: string;
    objectdata?: any;
    id?: number;
    fullName?: string;
}

export class TurbineUser implements ITurbineUser {
    constructor(
        public userId?: number,
        public loginName?: string,
        public passwordValue?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public confirmValue?: string,
        public modified?: Moment,
        public created?: Moment,
        public lastLogin?: Moment,
        public expire?: Moment,
        public expirePassword?: Moment,
        public objectdataContentType?: string,
        public objectdata?: any,
        public id?: number,
        public fullName?: string
    ) {
    }
}
