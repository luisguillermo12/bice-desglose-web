export interface ITurbineUsuarios {
    loginName?: string;
    nombre?: string;
    operatorCode?: string;
    user_id?: string;
}

export class TurbineUsuarios implements ITurbineUsuarios {
    constructor(public loginName?: string, public nombre?: string, public operatorCode?: string, public user_id?: string) {}
}
