import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';

type EntityResponseType = HttpResponse<IDesgloseMensaje>;
type EntityArrayResponseType = HttpResponse<IDesgloseMensaje[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseMensajeService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-mensajes';

    constructor(private http: HttpClient) {}

    create(desgloseMensaje: IDesgloseMensaje): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseMensaje);
        return this.http
            .post<IDesgloseMensaje>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(desgloseMensaje: IDesgloseMensaje): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desgloseMensaje);
        return this.http
            .put<IDesgloseMensaje>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(codCliente: string,codTipoDes: number,codSubtipoDes:number,codMoneda:string,codPlantilla:number): Observable<EntityArrayResponseType> {
        return this.http
            .get<IDesgloseMensaje[]>(`${this.resourceUrl}/${codCliente}/${codTipoDes}/${codSubtipoDes}/${codMoneda}/${codPlantilla}`, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDesgloseMensaje[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(desgloseMensaje: IDesgloseMensaje): IDesgloseMensaje {
        const copy: IDesgloseMensaje = Object.assign({}, desgloseMensaje, {
            fecEvent: desgloseMensaje.fecEvent != null && desgloseMensaje.fecEvent.isValid() ? desgloseMensaje.fecEvent.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecEvent = res.body.fecEvent != null ? moment(res.body.fecEvent) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desgloseMensaje: IDesgloseMensaje) => {
            desgloseMensaje.fecEvent = desgloseMensaje.fecEvent != null ? moment(desgloseMensaje.fecEvent) : null;
        });
        return res;
    }
}
