import { Moment } from 'moment';

export interface IReemplazosVista {
    cargo01?: string;
    rol1?: number;
    nombre1?: string;
    cargo02?: string;
    rol2?: number;
    nombre2?: string;
}
export class ReemplazosVista implements IReemplazosVista {
    constructor(
        public cargo01?: string,
        public cargo02?: string,
        public nombre1?: string,
        public nombre2?: string,
        public rol1?: number,
        public rol2?: number
    ) {}
}
