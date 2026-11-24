import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIdentificacionCliente } from 'app/shared/model/identificacion-cliente.model';

type EntityResponseType = HttpResponse<IIdentificacionCliente>;
type EntityArrayResponseType = HttpResponse<IIdentificacionCliente[]>;

@Injectable({ providedIn: 'root' })
export class IdentificacionClienteService {
    private resourceUrl = SERVER_API_URL + 'api/identificacion-clientes';

    constructor(private http: HttpClient) {}

    create(identificacionCliente: IIdentificacionCliente): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(identificacionCliente);
        return this.http
            .post<IIdentificacionCliente>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(identificacionCliente: IIdentificacionCliente): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(identificacionCliente);
        return this.http
            .put<IIdentificacionCliente>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IIdentificacionCliente>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIdentificacionCliente[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(identificacionCliente: IIdentificacionCliente): IIdentificacionCliente {
        const copy: IIdentificacionCliente = Object.assign({}, identificacionCliente, {
            fecCreacion:
                identificacionCliente.fecCreacion != null && identificacionCliente.fecCreacion.isValid()
                    ? identificacionCliente.fecCreacion.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecCreacion = res.body.fecCreacion != null ? moment(res.body.fecCreacion) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((identificacionCliente: IIdentificacionCliente) => {
            identificacionCliente.fecCreacion =
                identificacionCliente.fecCreacion != null ? moment(identificacionCliente.fecCreacion) : null;
        });
        return res;
    }
}
