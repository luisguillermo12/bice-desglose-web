import { Moment } from 'moment';
import { IConcepto } from 'app/shared/model/concepto.model';
import { IEmpresaConsolidada } from 'app/shared/model/empresa-consolidada.model';
export interface IObjetoImpresion {
     rut?:                  string;
     nombre?:               string;
     actividad?:            string;
     tipo_plantilla?:       string;
     tipo_concepto?:        string;
     subtipo_concepto?:     string;
     moneda?:               string;
     montos?:               string;
     deflactor_base?:       number;
     fecha_actualizacion?:  Moment;
     moneda_impreso?:       string;
     deflactor_periodo?:    string;
     tipo_cambio_periodo?:  string;
     conceptos_totales?:    IConcepto[];
     conceptos_resumidos?:  IConcepto[];
     conceptos_otros_datos?:IConcepto[];
     observaciones?:        string;
     empresas_consolidan?:  IEmpresaConsolidada[];
}
export class ObjetoImpresion  implements  IObjetoImpresion {
    constructor(public rut?:                string,
    public nombre?:                         string,
    public actividad?:                      string,
    public tipo_plantilla?:                 string,
    public tipo_concepto?:                  string,
    public subtipo_concepto?:               string,
    public moneda?:                         string,
    public montos?:                         string,
    public deflactor_base?:                 number,
    public fecha_actualizacion?:            Moment,
    public moneda_impreso?:                 string,
    public deflactor_periodo?:              string,
    public tipo_cambio_periodo?:            string,
    public conceptos_totales?:              IConcepto[],
    public conceptos_resumidos?:    |       IConcepto[],
    public conceptos_otros_datos?:          IConcepto[],
    public observaciones?:                  string,
    public empresas_consolidan?:            IEmpresaConsolidada[]) {}
}
