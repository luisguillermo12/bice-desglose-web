import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClienteBasico } from 'app/shared/model/cliente-basico.model';

type EntityResponseType = HttpResponse<IClienteBasico>;
type EntityArrayResponseType = HttpResponse<IClienteBasico[]>;

@Injectable({ providedIn: 'root' })
export class ClienteBasicoService {
    private resourceUrl = SERVER_API_URL + 'api/cliente-basicos';

    constructor(private http: HttpClient) {}

    create(clienteBasico: IClienteBasico): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(clienteBasico);
        return this.http
            .post<IClienteBasico>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(clienteBasico: IClienteBasico): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(clienteBasico);
        return this.http
            .put<IClienteBasico>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IClienteBasico>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IClienteBasico[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(clienteBasico: IClienteBasico): IClienteBasico {
        const copy: IClienteBasico = Object.assign({}, clienteBasico, {
            fecCliente:
                clienteBasico.fecCliente != null && clienteBasico.fecCliente.isValid()
                    ? clienteBasico.fecCliente.format(DATE_FORMAT)
                    : null,
            fecDeclJurada:
                clienteBasico.fecDeclJurada != null && clienteBasico.fecDeclJurada.isValid()
                    ? clienteBasico.fecDeclJurada.format(DATE_FORMAT)
                    : null,
            fecVerifDomlegal:
                clienteBasico.fecVerifDomlegal != null && clienteBasico.fecVerifDomlegal.isValid()
                    ? clienteBasico.fecVerifDomlegal.format(DATE_FORMAT)
                    : null,
            fecFirmaCobprejud:
                clienteBasico.fecFirmaCobprejud != null && clienteBasico.fecFirmaCobprejud.isValid()
                    ? clienteBasico.fecFirmaCobprejud.format(DATE_FORMAT)
                    : null,
            fecAlerta:
                clienteBasico.fecAlerta != null && clienteBasico.fecAlerta.isValid() ? clienteBasico.fecAlerta.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecCliente = res.body.fecCliente != null ? moment(res.body.fecCliente) : null;
        res.body.fecDeclJurada = res.body.fecDeclJurada != null ? moment(res.body.fecDeclJurada) : null;
        res.body.fecVerifDomlegal = res.body.fecVerifDomlegal != null ? moment(res.body.fecVerifDomlegal) : null;
        res.body.fecFirmaCobprejud = res.body.fecFirmaCobprejud != null ? moment(res.body.fecFirmaCobprejud) : null;
        res.body.fecAlerta = res.body.fecAlerta != null ? moment(res.body.fecAlerta) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((clienteBasico: IClienteBasico) => {
            clienteBasico.fecCliente = clienteBasico.fecCliente != null ? moment(clienteBasico.fecCliente) : null;
            clienteBasico.fecDeclJurada = clienteBasico.fecDeclJurada != null ? moment(clienteBasico.fecDeclJurada) : null;
            clienteBasico.fecVerifDomlegal = clienteBasico.fecVerifDomlegal != null ? moment(clienteBasico.fecVerifDomlegal) : null;
            clienteBasico.fecFirmaCobprejud = clienteBasico.fecFirmaCobprejud != null ? moment(clienteBasico.fecFirmaCobprejud) : null;
            clienteBasico.fecAlerta = clienteBasico.fecAlerta != null ? moment(clienteBasico.fecAlerta) : null;
        });
        return res;
    }
}
