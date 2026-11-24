import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClienteNatural } from 'app/shared/model/cliente-natural.model';

type EntityResponseType = HttpResponse<IClienteNatural>;
type EntityArrayResponseType = HttpResponse<IClienteNatural[]>;

@Injectable({ providedIn: 'root' })
export class ClienteNaturalService {
    private resourceUrl = SERVER_API_URL + 'api/cliente-naturals';

    constructor(private http: HttpClient) {}

    create(clienteNatural: IClienteNatural): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(clienteNatural);
        return this.http
            .post<IClienteNatural>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(clienteNatural: IClienteNatural): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(clienteNatural);
        return this.http
            .put<IClienteNatural>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IClienteNatural>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IClienteNatural[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(clienteNatural: IClienteNatural): IClienteNatural {
        const copy: IClienteNatural = Object.assign({}, clienteNatural, {
            fecNacim:
                clienteNatural.fecNacim != null && clienteNatural.fecNacim.isValid() ? clienteNatural.fecNacim.format(DATE_FORMAT) : null,
            fecAntigViv:
                clienteNatural.fecAntigViv != null && clienteNatural.fecAntigViv.isValid()
                    ? clienteNatural.fecAntigViv.format(DATE_FORMAT)
                    : null,
            fecDefuncion:
                clienteNatural.fecDefuncion != null && clienteNatural.fecDefuncion.isValid()
                    ? clienteNatural.fecDefuncion.format(DATE_FORMAT)
                    : null,
            fecCertEcivi:
                clienteNatural.fecCertEcivi != null && clienteNatural.fecCertEcivi.isValid()
                    ? clienteNatural.fecCertEcivi.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecNacim = res.body.fecNacim != null ? moment(res.body.fecNacim) : null;
        res.body.fecAntigViv = res.body.fecAntigViv != null ? moment(res.body.fecAntigViv) : null;
        res.body.fecDefuncion = res.body.fecDefuncion != null ? moment(res.body.fecDefuncion) : null;
        res.body.fecCertEcivi = res.body.fecCertEcivi != null ? moment(res.body.fecCertEcivi) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((clienteNatural: IClienteNatural) => {
            clienteNatural.fecNacim = clienteNatural.fecNacim != null ? moment(clienteNatural.fecNacim) : null;
            clienteNatural.fecAntigViv = clienteNatural.fecAntigViv != null ? moment(clienteNatural.fecAntigViv) : null;
            clienteNatural.fecDefuncion = clienteNatural.fecDefuncion != null ? moment(clienteNatural.fecDefuncion) : null;
            clienteNatural.fecCertEcivi = clienteNatural.fecCertEcivi != null ? moment(clienteNatural.fecCertEcivi) : null;
        });
        return res;
    }
}
