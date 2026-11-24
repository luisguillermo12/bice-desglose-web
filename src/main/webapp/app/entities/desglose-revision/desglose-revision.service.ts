import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseRevision } from 'app/shared/model/desglose-revision.model';

type EntityResponseType = HttpResponse<IDesgloseRevision>;
type EntityArrayResponseType = HttpResponse<IDesgloseRevision[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseRevisionService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-revisions';
    private resourceUrlIndicadorNotificaciones = SERVER_API_URL + 'api/desglose-revisions-indicador-notificaciones';

    constructor(private http: HttpClient) {
    }

    create(desgloseRevision: IDesgloseRevision): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseRevision);
        return this.http
        .post<IDesgloseRevision>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(desgloseRevision: IDesgloseRevision): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseRevision);
        return this.http
        .put<IDesgloseRevision>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(codCliente: string, codTipoDes: number, codSubtipoDes: number, codMoneda: string, codPlantilla: number): Observable<EntityArrayResponseType> {
        return this.http
        .get<IDesgloseRevision[]>(`${ this.resourceUrl }/${ codCliente }/${ codTipoDes }/${ codSubtipoDes }/${ codMoneda }/${ codPlantilla }`, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    indicadorNotificaciones(): Observable<HttpResponse<number>> {
        return this.http
        .get<number>(this.resourceUrlIndicadorNotificaciones, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
        .get<IDesgloseRevision[]>(this.resourceUrl, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ id }`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseRevision: IDesgloseRevision): IDesgloseRevision {
        const copy: IDesgloseRevision = Object.assign({}, desgloseRevision, {
            fecEnvioRevision: desgloseRevision.fecEnvioRevision != null && desgloseRevision.fecEnvioRevision.isValid() ? desgloseRevision.fecEnvioRevision.toJSON() : null,
            fecEnRevision: desgloseRevision.fecEnRevision != null && desgloseRevision.fecEnRevision.isValid() ? desgloseRevision.fecEnRevision.format(DATE_FORMAT) : null,
            fecTerminoRevision: desgloseRevision.fecTerminoRevision != null && desgloseRevision.fecTerminoRevision.isValid()
                ? desgloseRevision.fecTerminoRevision.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecEnvioRevision = res.body.fecEnvioRevision != null ? moment(res.body.fecEnvioRevision) : null;
        res.body.fecEnRevision = res.body.fecEnRevision != null ? moment(res.body.fecEnRevision) : null;
        res.body.fecTerminoRevision = res.body.fecTerminoRevision != null ? moment(res.body.fecTerminoRevision) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseRevision: IDesgloseRevision) => {
            desgloseRevision.fecEnvioRevision = desgloseRevision.fecEnvioRevision != null ? moment(desgloseRevision.fecEnvioRevision) : null;
            desgloseRevision.fecEnRevision = desgloseRevision.fecEnRevision != null ? moment(desgloseRevision.fecEnRevision) : null;
            desgloseRevision.fecTerminoRevision = desgloseRevision.fecTerminoRevision != null ? moment(desgloseRevision.fecTerminoRevision) : null;
        });
        return res;
    }
}
