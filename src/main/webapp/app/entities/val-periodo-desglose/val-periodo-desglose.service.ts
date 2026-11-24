import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IValPeriodoDesglose } from 'app/shared/model/val-periodo-desglose.model';

type EntityResponseType = HttpResponse<IValPeriodoDesglose>;
type EntityArrayResponseType = HttpResponse<IValPeriodoDesglose[]>;

@Injectable({ providedIn: 'root' })
export class ValPeriodoDesgloseService {
    private resourceUrl = SERVER_API_URL + 'api/val-periodo-desgloses';
    private resourceUrlDeleteMultiregistro = this.resourceUrl + '/delete';

    constructor(private http: HttpClient) {
    }

    create(valPeriodoDesglose: IValPeriodoDesglose): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(valPeriodoDesglose);
        return this.http
        .post<IValPeriodoDesglose>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(valPeriodoDesglose: IValPeriodoDesglose): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(valPeriodoDesglose);
        return this.http
        .put<IValPeriodoDesglose>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
        .get<IValPeriodoDesglose>(`${ this.resourceUrl }/${ id }`, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findByBusquedaValores(codCliente: string, codTipoDesglose: number, codSubtipoDesglose: number, codMoneda: number, codPlantilla: number): Observable<EntityArrayResponseType> {
        return this.http
        .get<IValPeriodoDesglose[]>(`${ this.resourceUrl }/busquedaValores/${ codCliente }/${ codTipoDesglose }/${ codSubtipoDesglose }/${ codMoneda }/${ codPlantilla }`, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
        .get<IValPeriodoDesglose[]>(this.resourceUrl, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ id }`, { observe: 'response' });
    }
    deleteMultiregistros(codCliente:string,codTipoDesglose:number, codSubtipoDesglose:number,codMoneda:string,codPlantilla:number,corConcepto:number,corValor:number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrlDeleteMultiregistro }/${ codCliente }/${ codTipoDesglose }/${ codSubtipoDesglose }/${ codMoneda }/${ codPlantilla }/${ corConcepto }/${ corValor }`, { observe: 'response' });
    }

    private convertDateFromClient(valPeriodoDesglose: IValPeriodoDesglose): IValPeriodoDesglose {
        const copy: IValPeriodoDesglose = Object.assign({}, valPeriodoDesglose, {
            fecPeriodo:
                valPeriodoDesglose.fecPeriodo != null && valPeriodoDesglose.fecPeriodo.isValid()
                    ? valPeriodoDesglose.fecPeriodo.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecPeriodo = res.body.fecPeriodo != null ? moment(res.body.fecPeriodo) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((valPeriodoDesglose: IValPeriodoDesglose) => {
            valPeriodoDesglose.fecPeriodo = valPeriodoDesglose.fecPeriodo != null ? moment(valPeriodoDesglose.fecPeriodo) : null;
        });
        return res;
    }
}
