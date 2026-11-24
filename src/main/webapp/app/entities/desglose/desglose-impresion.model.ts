import { Moment } from 'moment';

export interface IDesgloseImpresionDTO {
    codCliente                  ?:string;
    codTipoPlantilla            ?:number;
    codTipoDes                  ?:number;
    codSubtipoDes               ?:number;
    codMoneda                   ?:string;
    activePeriodsIndexes        ?:number[];
    impresionRapida           ?:boolean;
    codMonedaImpresion          ?:string;
    deflactorBase               ?:number;
    deflactores               ?:number[];
    tiposCambio               ?:number[];
    fecDeflacBase             ?:Moment;
}
export class DesgloseImpresionDTO implements  DesgloseImpresionDTO {
           constructor(
               public codCliente           ?:string,
               public codTipoPlantilla     ?:number,
               public codTipoDes           ?:number,
               public codSubtipoDes        ?:number,
               public codMoneda            ?:string,
               public activePeriodsIndexes ?:number[],
               public impresionRapida    ?:boolean,
               public codMonedaImpresion   ?:string,
               public deflactorBase        ?:number,
               public deflactores        ?:number[],
               public tiposCambio        ?:number[],
               public fecDeflacBase      ?:Moment
           ) {
           }
}
