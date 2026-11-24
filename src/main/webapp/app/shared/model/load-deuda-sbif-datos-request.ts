import { Moment } from 'moment';

export interface IDatosConsultaSbifRequestDTO {
    rut?: string;
    fecConsulta?: Moment;
    arrPERIODOS?: Moment[];
    activePeriodsIndexes?: number[];

}

export class DatosConsultaSbifRequestDTO implements IDatosConsultaSbifRequestDTO {
    constructor(
        public rut?: string,
        public fecConsulta?: Moment,
        public arrPERIODOS?: Moment[],
        public activePeriodsIndexes?: number[]
    ) {
    }
}
