import { Moment } from 'moment';
import { ITableValue } from 'app/shared/model/table-value.model';

export interface IPdfIngressReport {
    fileName?:string;
    codCliente?: string;
    codTipoPlantilla?:string;
    codTipoDes?:string;
    codSubtipoDes?:string;
    codMoneda?:string;
    rutCliente?:string;
    nombreCliente?:string;
    tipoPlantillaDesglose?:string;
    tipoDesglose?:string;
    subTipoDesglose?:string;
    moneda?:string;
    sector?:string;
    subSector?:string;
    actividad?:string;
    iva?:string;
    montosEn?:string;
    actualizadoAl?:string;
    auditor1?:string;
    auditor2?:string;
    auditor3?:string;
    auditor4?:string;
    fecPerido1?:string;
    fecPerido2?:string;
    fecPerido3?:string;
    fecPerido4?:string;
    numMeses1?:string;
    numMeses2?:string;
    numMeses3?:string;
    numMeses4?:string;
    tableValueDTOS?:ITableValue[];
    deflactorPerido1?:number;
    deflactorPerido2?:number;
    deflactorPerido3?:number;
    deflactorPerido4?:number;
    tipoCambioPerido1?:number;
    tipoCambioPerido2?:number;
    tipoCambioPerido3?:number;
    tipoCambioPerido4?:number;
}
export class PdfIngressReport {
    constructor(
        public fileName?:string,
    public codCliente?: string,
    public codTipoPlantilla?:string,
    public codTipoDes?:string,
    public codSubtipoDes?:string,
    public codMoneda?:string,
    public rutCliente?:string,
    public nombreCliente?:string,
    public tipoPlantillaDesglose?:string,
    public tipoDesglose?:string,
    public subTipoDesglose?:string,
    public moneda?:string,
    public sector?:string,
    public subSector?:string,
    public actividad?:string,
    public iva?:string,
    public montosEn?:string,
    public actualizadoAl?:string,
    public auditor1?:string,
    public auditor2?:string,
    public auditor3?:string,
    public auditor4?:string,
    public fecPerido1?:string,
    public fecPerido2?:string,
    public fecPerido3?:string,
    public fecPerido4?:string,
    public numMeses1?:string,
    public numMeses2?:string,
    public numMeses3?:string,
    public numMeses4?:string,
    public tableValueDTOS?:ITableValue[],
    public deflactorPerido1?:number,
    public deflactorPerido2?:number,
    public deflactorPerido3?:number,
    public deflactorPerido4?:number,
    public tipoCambioPerido1?:number,
    public tipoCambioPerido2?:number,
    public tipoCambioPerido3?:number,
    public tipoCambioPerido4?:number
    ) {}
}
