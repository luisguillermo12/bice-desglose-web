export interface ILog {
    fichero?: string;
    nombre?: string;
    megaBytes?:string;
}

export class Log implements ILog {
    constructor(
        public fichero?: string,
        public nombre?: string,
        public megaBytes?:string
    ) {}
}
