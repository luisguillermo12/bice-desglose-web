import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPeriodo } from 'app/shared/model/periodo.model';
import { IPeriodDeleteRequestDTO } from 'app/entities/periodo/period-delete-request.model';

type EntityResponseType = HttpResponse<IPeriodo>;
type EntityArrayResponseType = HttpResponse<IPeriodo[]>;

@Injectable({ providedIn: 'root' })
export class PeriodoService {
    private resourceUrl = SERVER_API_URL + 'api/periodos';
    private resourceDeletePeriodUrl = SERVER_API_URL + 'api/periodos/delete';

    constructor(private http: HttpClient) {
    }

    create(periodo: IPeriodo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(periodo);
        return this.http
        .post<IPeriodo>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(periodo: IPeriodo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(periodo);
        return this.http
        .put<IPeriodo>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(codCliente: string, codTipoDes: number, codSubtipoDes: number, codMoneda: string): Observable<EntityResponseType> {
        return this.http
        .get<IPeriodo>(`${ this.resourceUrl }/${ codCliente }/${ codTipoDes }/${ codSubtipoDes }/${ codMoneda }`, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
        .get<IPeriodo[]>(this.resourceUrl, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(codCliente: string, codTipoDes: string, codSubtipoDes: string, codMoneda: string, fecPeriodo: Moment, codPlantilla: string): Observable<HttpResponse<any>> {
        const fecPeriodoFromClient = fecPeriodo != null && fecPeriodo.isValid() ? fecPeriodo.format(DATE_FORMAT) : null;
        return this.http.delete<any>(`${ this.resourceUrl }/${ codCliente }/${ codTipoDes }/${ codSubtipoDes }/${ codMoneda }/${ fecPeriodoFromClient }/${ codPlantilla }`, {
            observe: 'response'
        });
    }

    private convertDateFromClient(periodo: IPeriodo): IPeriodo {
        const copy: IPeriodo = Object.assign({}, periodo, {
            fecPeriodo: periodo.fecPeriodo != null && periodo.fecPeriodo.isValid() ? periodo.fecPeriodo.format(DATE_FORMAT) : null,
            fecUserCrea: periodo.fecUserCrea != null && periodo.fecUserCrea.isValid() ? periodo.fecUserCrea.format(DATE_FORMAT) : null,
            fecUserModif: periodo.fecUserModif != null && periodo.fecUserModif.isValid() ? periodo.fecUserModif.format(DATE_FORMAT) : null,
            fecInformSbif: periodo.fecInformSbif != null && periodo.fecInformSbif.isValid() ? periodo.fecInformSbif.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecPeriodo = res.body.fecPeriodo != null ? moment(res.body.fecPeriodo) : null;
        res.body.fecUserCrea = res.body.fecUserCrea != null ? moment(res.body.fecUserCrea) : null;
        res.body.fecUserModif = res.body.fecUserModif != null ? moment(res.body.fecUserModif) : null;
        res.body.fecInformSbif = res.body.fecInformSbif != null ? moment(res.body.fecInformSbif) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((periodo: IPeriodo) => {
            periodo.fecPeriodo = periodo.fecPeriodo != null ? moment(periodo.fecPeriodo) : null;
            periodo.fecUserCrea = periodo.fecUserCrea != null ? moment(periodo.fecUserCrea) : null;
            periodo.fecUserModif = periodo.fecUserModif != null ? moment(periodo.fecUserModif) : null;
            periodo.fecInformSbif = periodo.fecInformSbif != null ? moment(periodo.fecInformSbif) : null;
        });
        return res;
    }

    private convertDateToDeleteFromClient(periodDeleteRequestDTO: IPeriodDeleteRequestDTO): IPeriodDeleteRequestDTO {
        const copy: IPeriodDeleteRequestDTO = Object.assign({}, periodDeleteRequestDTO, {
            fecPeriodo: periodDeleteRequestDTO.fecPeriodo != null && periodDeleteRequestDTO.fecPeriodo.isValid() ? periodDeleteRequestDTO.fecPeriodo.format(DATE_FORMAT) : null
        });
        return copy;
    }
}
