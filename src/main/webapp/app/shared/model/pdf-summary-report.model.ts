import { ITableValue } from 'app/shared/model/table-value.model';
import { INoteValue } from 'app/shared/model/note-value.model';
import { ICompanyValue } from 'app/shared/model/company-value.model';
import { ObservationValue } from 'app/shared/model/observation-value.model';

export class IPdfSummaryReport {
    fileName?: string;

    codCliente?: string;
    codTipoPlantilla?: string;
    codTipoDes?: string;
    codSubtipoDes?: string;
    codMoneda?: string;

    rutCliente?: string;
    nombreCliente?: string;
    tipoPlantillaDesglose?: string;
    tipoDesglose?: string;
    subTipoDesglose?: string;
    moneda?: string;
    sector?: string;
    subSector?: string;
    actividad?: string;
    iva?: string;
    montosEn?: string;
    actualizadoAl?: string;

    auditor1?: string;
    auditor2?: string;
    auditor3?: string;
    auditor4?: string;
    calificacion1?: string;
    calificacion2?: string;
    calificacion3?: string;
    calificacion4?: string;
    fecPerido1?: string;
    fecPerido2?: string;
    fecPerido3?: string;
    fecPerido4?: string;
    numMeses1?: string;
    numMeses2?: string;
    numMeses3?: string;
    numMeses4?: string;

    datosBalance?: ITableValue[];
    datosEstadoResultado?: ITableValue[];
    datosGenFondFlujEfectivo?: ITableValue[];
    datosIndicadorFinanciero?: ITableValue[];
    datosNotasDesglose?: INoteValue[];
    datosEmpresasConsolidan?: ICompanyValue[];
    datosObservaciones?: ObservationValue[];

    nombreUsuario?: string;
    fechaModificacion?: string;

    lastDate?: string;
    beforeLastDate?: string;
}

export class PdfSummaryReport {
    constructor(
        public codCliente?: string,
        public codTipoPlantilla?: string,
        public codTipoDes?: string,
        public codSubtipoDes?: string,
        public fileName?: string,
        public codMoneda?: string,
        public rutCliente?: string,
        public nombreCliente?: string,
        public tipoPlantillaDesglose?: string,
        public tipoDesglose?: string,
        public subTipoDesglose?: string,
        public moneda?: string,
        public sector?: string,
        public subSector?: string,
        public actividad?: string,
        public iva?: string,
        public montosEn?: string,
        public actualizadoAl?: string,
        public auditor1?: string,
        public auditor2?: string,
        public auditor3?: string,
        public auditor4?: string,
        public calificacion1?: string,
        public calificacion2?: string,
        public calificacion3?: string,
        public calificacion4?: string,
        public fecPerido1?: string,
        public fecPerido2?: string,
        public fecPerido3?: string,
        public fecPerido4?: string,
        public numMeses1?: string,
        public numMeses2?: string,
        public numMeses3?: string,
        public numMeses4?: string,
        public datosBalance?: ITableValue[],
        public datosEstadoResultado?: ITableValue[],
        public datosGenFondFlujEfectivo?: ITableValue[],
        public datosIndicadorFinanciero?: ITableValue[],
        public datosNotasDesglose?: INoteValue[],
        public datosEmpresasConsolidan?: ICompanyValue[],
        public datosObservaciones?: ObservationValue[],
        public nombreUsuario?: string,
        public fechaModificacion?: string,
        public lastDate?: string,
        public beforeLastDate?: string
    ) {
    }
}
