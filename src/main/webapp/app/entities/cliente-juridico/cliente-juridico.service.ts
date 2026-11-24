import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClienteJuridico } from 'app/shared/model/cliente-juridico.model';

type EntityResponseType = HttpResponse<IClienteJuridico>;
type EntityArrayResponseType = HttpResponse<IClienteJuridico[]>;

@Injectable({ providedIn: 'root' })
export class ClienteJuridicoService {
    private resourceUrl = SERVER_API_URL + 'api/cliente-juridicos';

    constructor(private http: HttpClient) {}

    create(clienteJuridico: IClienteJuridico): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(clienteJuridico);
        return this.http
            .post<IClienteJuridico>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(clienteJuridico: IClienteJuridico): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(clienteJuridico);
        return this.http
            .put<IClienteJuridico>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IClienteJuridico>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IClienteJuridico[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(clienteJuridico: IClienteJuridico): IClienteJuridico {
        const copy: IClienteJuridico = Object.assign({}, clienteJuridico, {
            fecConstitucion:
                clienteJuridico.fecConstitucion != null && clienteJuridico.fecConstitucion.isValid()
                    ? clienteJuridico.fecConstitucion.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecConstitucion = res.body.fecConstitucion != null ? moment(res.body.fecConstitucion) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((clienteJuridico: IClienteJuridico) => {
            clienteJuridico.fecConstitucion = clienteJuridico.fecConstitucion != null ? moment(clienteJuridico.fecConstitucion) : null;
        });
        return res;
    }
}
