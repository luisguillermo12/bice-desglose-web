import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPeriodoDesgConsol } from 'app/shared/model/periodo-desg-consol.model';

type EntityResponseType = HttpResponse<IPeriodoDesgConsol>;
type EntityArrayResponseType = HttpResponse<IPeriodoDesgConsol[]>;

@Injectable({ providedIn: 'root' })
export class PeriodoDesgConsolService {
    private resourceUrl = SERVER_API_URL + 'api/periodo-desg-consols';

    constructor(private http: HttpClient) {}

    create(periodoDesgConsol: IPeriodoDesgConsol): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(periodoDesgConsol);
        return this.http
            .post<IPeriodoDesgConsol>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(periodoDesgConsol: IPeriodoDesgConsol): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(periodoDesgConsol);
        return this.http
            .put<IPeriodoDesgConsol>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPeriodoDesgConsol>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPeriodoDesgConsol[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(periodoDesgConsol: IPeriodoDesgConsol): IPeriodoDesgConsol {
        const copy: IPeriodoDesgConsol = Object.assign({}, periodoDesgConsol, {
            fecPeriodo:
                periodoDesgConsol.fecPeriodo != null && periodoDesgConsol.fecPeriodo.isValid()
                    ? periodoDesgConsol.fecPeriodo.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecPeriodo = res.body.fecPeriodo != null ? moment(res.body.fecPeriodo) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((periodoDesgConsol: IPeriodoDesgConsol) => {
            periodoDesgConsol.fecPeriodo = periodoDesgConsol.fecPeriodo != null ? moment(periodoDesgConsol.fecPeriodo) : null;
        });
        return res;
    }
}
