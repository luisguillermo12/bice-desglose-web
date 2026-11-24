import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';

type EntityResponseType = HttpResponse<IDesgloseReemplazo>;
type EntityArrayResponseType = HttpResponse<IDesgloseReemplazo[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseReemplazoService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-reemplazos';

    constructor(private http: HttpClient) {
    }

    create(desgloseReemplazo: IDesgloseReemplazo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseReemplazo);
        return this.http
        .post<IDesgloseReemplazo>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(desgloseReemplazo: IDesgloseReemplazo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseReemplazo);
        return this.http
        .put<IDesgloseReemplazo>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateNativo(desgloseReemplazoLista: IDesgloseReemplazo[]): Observable<EntityResponseType> {
        return this.http.put<IDesgloseReemplazo>(`${ this.resourceUrl }/update`, desgloseReemplazoLista, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
        .get<IDesgloseReemplazo>(`${ this.resourceUrl }/${ id }`, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
        .get<IDesgloseReemplazo[]>(this.resourceUrl, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ id }`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseReemplazo: IDesgloseReemplazo): IDesgloseReemplazo {
        const copy: IDesgloseReemplazo = Object.assign({}, desgloseReemplazo, {
            fecReemplazo: desgloseReemplazo.fecReemplazo != null && desgloseReemplazo.fecReemplazo.isValid() ? desgloseReemplazo.fecReemplazo.format(DATE_FORMAT) : null,
            fecElimReemplazo: desgloseReemplazo.fecElimReemplazo != null && desgloseReemplazo.fecElimReemplazo.isValid()
                ? desgloseReemplazo.fecElimReemplazo.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecReemplazo = res.body.fecReemplazo != null ? moment(res.body.fecReemplazo) : null;
        res.body.fecElimReemplazo = res.body.fecElimReemplazo != null ? moment(res.body.fecElimReemplazo) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseReemplazo: IDesgloseReemplazo) => {
            desgloseReemplazo.fecReemplazo = desgloseReemplazo.fecReemplazo != null ? moment(desgloseReemplazo.fecReemplazo) : null;
            desgloseReemplazo.fecElimReemplazo = desgloseReemplazo.fecElimReemplazo != null ? moment(desgloseReemplazo.fecElimReemplazo) : null;
        });
        return res;
    }
}
