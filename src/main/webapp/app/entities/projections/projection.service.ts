import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { IDesglose } from 'app/shared/model/desglose.model';
import { IProjectionHistoricDataRequestDTO } from 'app/projections/projection-historic-data-request.model';
import { IBreakdownRecoverResponseDTO } from 'app/entities/desglose/recover-breakdown-response.model';
import { isNullOrUndefined } from 'util';
import { IPeriodo } from 'app/shared/model/periodo.model';
import { IValPeriodoProyeccionDesglose } from 'app/shared/model/val-periodo-proyeccion-desglose.model';

type EntityResponseType = HttpResponse<IDesglose>;
type EntityHistoricDataArrayResponseType = HttpResponse<IBreakdownRecoverResponseDTO[]>;
type EntityHistoricDataResponseType = HttpResponse<IBreakdownRecoverResponseDTO>;
type EntityPeriodoDataResponseType = HttpResponse<IPeriodo>;
@Injectable({ providedIn: 'root' })
export class ProjectionService {
    private resourceUrlGetHistoricData = SERVER_API_URL + 'api/projection-historic-data';
    private resourceUrl2 = SERVER_API_URL + 'api/report';
    private INFORME_PROYECCION = this.resourceUrl2 + '/download/informe-proyeccion';
    private cerrarProyecciones = SERVER_API_URL + 'api/projection-cerrar';
    private editarProyecciones = SERVER_API_URL + 'api/projection-editar';

    constructor(private http: HttpClient) {
    }

    InformeProyeccion(historicDataRequestDTO: IProjectionHistoricDataRequestDTO): Observable<any> {
        const copy = this.convertHitoricDateFromClient(historicDataRequestDTO);
        return this.http.post<IBreakdownRecoverResponseDTO>(this.INFORME_PROYECCION, copy, {
            'responseType': 'arraybuffer' as 'json'
        });
    }

    getHistoricData(historicDataRequestDTO: IProjectionHistoricDataRequestDTO): Observable<EntityHistoricDataResponseType> {
        const copy = this.convertHitoricDateFromClient(historicDataRequestDTO);
        return this.http.post<IBreakdownRecoverResponseDTO>(this.resourceUrlGetHistoricData, copy, { observe: 'response' }).pipe(
            map((res: EntityHistoricDataResponseType) => this.convertDateArrayHistoricDataFromServer(res)));
    }

    cerrarProyeccion(historicDataRequestDTO: IProjectionHistoricDataRequestDTO): Observable<EntityPeriodoDataResponseType> {
        const copy = this.convertHitoricDateFromClient(historicDataRequestDTO);
        return this.http.post<IPeriodo>(this.cerrarProyecciones, copy, { observe: 'response' }).pipe(
            map((res: EntityPeriodoDataResponseType) => this.convertDateFromServerPeriod(res)));
    }

    editarProyeccion(historicDataRequestDTO: IProjectionHistoricDataRequestDTO): Observable<EntityPeriodoDataResponseType> {
        const copy = this.convertHitoricDateFromClient(historicDataRequestDTO);
        return this.http.post<IPeriodo>(this.editarProyecciones, copy, { observe: 'response' }).pipe(
            map((res: EntityPeriodoDataResponseType) => this.convertDateFromServerPeriod(res)));
    }

    private convertHitoricDateFromClient(filtroDesgloseDTO: IProjectionHistoricDataRequestDTO): IProjectionHistoricDataRequestDTO {
        const copy: IProjectionHistoricDataRequestDTO = Object.assign({}, filtroDesgloseDTO, {
            fecPeriodo: filtroDesgloseDTO.fecPeriodo != null && filtroDesgloseDTO.fecPeriodo.isValid() ? filtroDesgloseDTO.fecPeriodo.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecUserCrea = res.body.fecUserCrea != null ? moment(res.body.fecUserCrea) : null;
        res.body.fecUserModif = res.body.fecUserModif != null ? moment(res.body.fecUserModif) : null;
        res.body.fecDeflacBase = res.body.fecDeflacBase != null ? moment(res.body.fecDeflacBase) : null;
        res.body.fecHraOpen = res.body.fecHraOpen != null ? moment(res.body.fecHraOpen) : null;
        res.body.fecDesgloDef = res.body.fecDesgloDef != null ? moment(res.body.fecDesgloDef) : null;
        res.body.fecDesgloEnvPdf = res.body.fecDesgloEnvPdf != null ? moment(res.body.fecDesgloEnvPdf) : null;
        res.body.fecPeriodo1Rpt = res.body.fecPeriodo1Rpt != null ? moment(res.body.fecPeriodo1Rpt) : null;
        res.body.fecPeriodo2Rpt = res.body.fecPeriodo2Rpt != null ? moment(res.body.fecPeriodo2Rpt) : null;
        res.body.fecPeriodo3Rpt = res.body.fecPeriodo3Rpt != null ? moment(res.body.fecPeriodo3Rpt) : null;
        res.body.fecPeriodo4Rpt = res.body.fecPeriodo4Rpt != null ? moment(res.body.fecPeriodo4Rpt) : null;
        res.body.fecPerConsol1Rpt = res.body.fecPerConsol1Rpt != null ? moment(res.body.fecPerConsol1Rpt) : null;
        res.body.fecPerConsol2Rpt = res.body.fecPerConsol2Rpt != null ? moment(res.body.fecPerConsol2Rpt) : null;
        res.body.fecUlt1Rpt = res.body.fecUlt1Rpt != null ? moment(res.body.fecUlt1Rpt) : null;
        res.body.fecUlt2Rpt = res.body.fecUlt2Rpt != null ? moment(res.body.fecUlt2Rpt) : null;
        res.body.fecUserModifRpt = res.body.fecUserModifRpt != null ? moment(res.body.fecUserModifRpt) : null;
        res.body.fecGeneracionRpt = res.body.fecGeneracionRpt != null ? moment(res.body.fecGeneracionRpt) : null;
        return res;
    }

    private convertDateArrayHistoricDataFromServer(res: EntityHistoricDataResponseType): EntityHistoricDataResponseType {
        if (!isNullOrUndefined(res.body.extraDataDTO.periodos)) {
            res.body.extraDataDTO.periodos.forEach((periodo: IPeriodo) => {
                periodo.fecPeriodo = periodo.fecPeriodo != null ? moment(periodo.fecPeriodo) : null;
                periodo.fecUserCrea = periodo.fecUserCrea != null ? moment(periodo.fecUserCrea) : null;
                periodo.fecUserModif = periodo.fecUserModif != null ? moment(periodo.fecUserModif) : null;
                periodo.fecInformSbif = periodo.fecInformSbif != null ? moment(periodo.fecInformSbif) : null;
                periodo.valPeriodoProyeccionDesgloseDTOList.forEach((valPeriodoProyeccionDesglose: IValPeriodoProyeccionDesglose) => {
                    valPeriodoProyeccionDesglose.fecPeriodo = valPeriodoProyeccionDesglose.fecPeriodo != null ? moment(valPeriodoProyeccionDesglose.fecPeriodo) : null;
                    valPeriodoProyeccionDesglose.fecPeriodoPry = valPeriodoProyeccionDesglose.fecPeriodoPry != null ? moment(valPeriodoProyeccionDesglose.fecPeriodoPry) : null;

                });
            });
        }
        if (!isNullOrUndefined(res.body.extraDataDTO.fecPeriodo)) {
            for (let i = 0; i < res.body.extraDataDTO.fecPeriodo.length; i++) {
                res.body.extraDataDTO.fecPeriodo[ i ] = res.body.extraDataDTO.fecPeriodo[ i ] != null ? moment(res.body.extraDataDTO.fecPeriodo[ i ]) : null;
            }
        }
        return res;
    }

    private convertDateFromServerPeriod(res: EntityPeriodoDataResponseType): EntityPeriodoDataResponseType {
        res.body.fecPeriodo = res.body.fecPeriodo != null ? moment(res.body.fecPeriodo) : null;
        res.body.fecUserCrea = res.body.fecUserCrea != null ? moment(res.body.fecUserCrea) : null;
        res.body.fecUserModif = res.body.fecUserModif != null ? moment(res.body.fecUserModif) : null;
        res.body.fecInformSbif = res.body.fecInformSbif != null ? moment(res.body.fecInformSbif) : null;
        return res;
    }
}
