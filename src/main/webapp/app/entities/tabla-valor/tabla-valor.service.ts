import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITablaValor } from 'app/shared/model/tabla-valor.model';

type EntityResponseType = HttpResponse<ITablaValor>;
type EntityArrayResponseType = HttpResponse<ITablaValor[]>;

@Injectable({ providedIn: 'root' })
export class TablaValorService {
    private resourceUrl = SERVER_API_URL + 'api/tabla-valors';
    private resourceUrlGetIva = SERVER_API_URL + 'api/tabla-valors/get-iva-value';

    constructor(private http: HttpClient) {}

    create(tablaValor: ITablaValor): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(tablaValor);
        return this.http
            .post<ITablaValor>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(tablaValor: ITablaValor): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(tablaValor);
        return this.http
            .put<ITablaValor>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITablaValor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getIvaValue(): Observable<EntityResponseType> {
        return this.http
            .get<ITablaValor>(`${this.resourceUrlGetIva}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findByCodTablaAndCodRegStatus(codTabla: string, codRegStatus: number): Observable<EntityArrayResponseType> {
        return this.http
            .get<ITablaValor[]>(`${this.resourceUrl}/findByCodTablaAndCodRegStatus/${codTabla}/${codRegStatus}`, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITablaValor[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(tablaValor: ITablaValor): ITablaValor {
        const copy: ITablaValor = Object.assign({}, tablaValor, {
            fecValidez: tablaValor.fecValidez != null && tablaValor.fecValidez.isValid() ? tablaValor.fecValidez.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecValidez = res.body.fecValidez != null ? moment(res.body.fecValidez) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((tablaValor: ITablaValor) => {
            tablaValor.fecValidez = tablaValor.fecValidez != null ? moment(tablaValor.fecValidez) : null;
        });
        return res;
    }
}
