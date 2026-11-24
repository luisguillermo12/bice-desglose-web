export interface ISfStfOperator {
    branchCode?: number;
    operatorCode?: number;
    loginName?: string;
    isSupervisor?: number;
    signedOn?: number;
    dayOpen?: number;
    id?: number;
}

export class SfStfOperator implements ISfStfOperator {
    constructor(
        public branchCode?: number,
        public operatorCode?: number,
        public loginName?: string,
        public isSupervisor?: number,
        public signedOn?: number,
        public dayOpen?: number,
        public id?: number
    ) {
    }
}
