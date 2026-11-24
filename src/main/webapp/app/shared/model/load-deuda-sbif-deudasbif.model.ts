import { Moment } from 'moment';

export interface ILoadDeudaSbifDTO {

hayDeudaSbif?: boolean;
fecInforme?: Moment;
mtoSbif_DVig?: number;
mtoSbif_DMor?: number;
mtoSbif_DVen?: number;
mtoSbif_DCas?: number;
mtoSbif_IVig?: number;
mtoSbif_IVen?: number;
mtoSbif_ICas?: number;
mtoSbif_Imp90_179?: number;

}

export class LoadDeudaSbifDTO implements ILoadDeudaSbifDTO {
    constructor(
        public hayDeudaSbif?: boolean,
        public fecInforme?: Moment,
        public mtoSbif_DVig?: number,
        public mtoSbif_DMor?: number,
        public mtoSbif_DVen?: number,
        public mtoSbif_DCas?: number,
        public mtoSbif_IVig?: number,
        public mtoSbif_IVen?: number,
        public mtoSbif_ICas?: number,
        public mtoSbif_Imp90_179?: number

) {
    }
}
