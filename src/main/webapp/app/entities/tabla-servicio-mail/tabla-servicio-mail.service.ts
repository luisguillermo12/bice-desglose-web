import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITablaServicioMail } from 'app/shared/model/tabla-servicio-mail.model';

type EntityResponseType = HttpResponse<ITablaServicioMail>;
type EntityArrayResponseType = HttpResponse<ITablaServicioMail[]>;

@Injectable({ providedIn: 'root' })
export class TablaServicioMailService {
    private resourceUrl = SERVER_API_URL + 'api/tabla-servicio-mails';

    constructor(private http: HttpClient) {}

    create(tablaServicioMail: ITablaServicioMail): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(tablaServicioMail);
        return this.http
            .post<ITablaServicioMail>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(tablaServicioMail: ITablaServicioMail): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(tablaServicioMail);
        return this.http
            .put<ITablaServicioMail>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITablaServicioMail>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITablaServicioMail[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(tablaServicioMail: ITablaServicioMail): ITablaServicioMail {
        const copy: ITablaServicioMail = Object.assign({}, tablaServicioMail, {
            fecSolicitud:
                tablaServicioMail.fecSolicitud != null && tablaServicioMail.fecSolicitud.isValid()
                    ? tablaServicioMail.fecSolicitud.format(DATE_FORMAT)
                    : null,
            fecEnvMail:
                tablaServicioMail.fecEnvMail != null && tablaServicioMail.fecEnvMail.isValid()
                    ? tablaServicioMail.fecEnvMail.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecSolicitud = res.body.fecSolicitud != null ? moment(res.body.fecSolicitud) : null;
        res.body.fecEnvMail = res.body.fecEnvMail != null ? moment(res.body.fecEnvMail) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((tablaServicioMail: ITablaServicioMail) => {
            tablaServicioMail.fecSolicitud = tablaServicioMail.fecSolicitud != null ? moment(tablaServicioMail.fecSolicitud) : null;
            tablaServicioMail.fecEnvMail = tablaServicioMail.fecEnvMail != null ? moment(tablaServicioMail.fecEnvMail) : null;
        });
        return res;
    }
}
