import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IValPeriodoDesgImpr } from 'app/shared/model/val-periodo-desg-impr.model';

type EntityResponseType = HttpResponse<IValPeriodoDesgImpr>;
type EntityArrayResponseType = HttpResponse<IValPeriodoDesgImpr[]>;

@Injectable({ providedIn: 'root' })
export class ValPeriodoDesgImprService {
    private resourceUrl = SERVER_API_URL + 'api/val-periodo-desg-imprs';
    private resourceUrlCifSpInsValImp = this.resourceUrl + '/executeCifSpInsValImp';
    constructor(private http: HttpClient) {}

    create(valPeriodoDesgImpr: IValPeriodoDesgImpr): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(valPeriodoDesgImpr);
        return this.http
            .post<IValPeriodoDesgImpr>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    CifSpInsValImp(valPeriodoDesgImpr: IValPeriodoDesgImpr): Observable<any> {
        const copy = this.convertDateFromClient(valPeriodoDesgImpr);
        return this.http
        .post<IValPeriodoDesgImpr>(this.resourceUrlCifSpInsValImp, copy, { observe: 'response' });
    }
    update(valPeriodoDesgImpr: IValPeriodoDesgImpr): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(valPeriodoDesgImpr);
        return this.http
            .put<IValPeriodoDesgImpr>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IValPeriodoDesgImpr>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IValPeriodoDesgImpr[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(valPeriodoDesgImpr: IValPeriodoDesgImpr): IValPeriodoDesgImpr {
        const copy: IValPeriodoDesgImpr = Object.assign({}, valPeriodoDesgImpr, {
            fecPeriodo:
                valPeriodoDesgImpr.fecPeriodo != null && valPeriodoDesgImpr.fecPeriodo.isValid()
                    ? valPeriodoDesgImpr.fecPeriodo.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecPeriodo = res.body.fecPeriodo != null ? moment(res.body.fecPeriodo) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((valPeriodoDesgImpr: IValPeriodoDesgImpr) => {
            valPeriodoDesgImpr.fecPeriodo = valPeriodoDesgImpr.fecPeriodo != null ? moment(valPeriodoDesgImpr.fecPeriodo) : null;
        });
        return res;
    }
}
