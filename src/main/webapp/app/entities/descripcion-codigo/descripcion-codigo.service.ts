import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';

type EntityResponseType = HttpResponse<IDescripcionCodigo>;
type EntityArrayResponseType = HttpResponse<IDescripcionCodigo[]>;

@Injectable({ providedIn: 'root' })
export class DescripcionCodigoService {
    private resourceUrl = SERVER_API_URL + 'api/descripcion-codigos';
    private FIND_BY_CODTABLA = this.resourceUrl + '/find-by-codtabla';
    private FIND_BY_CODTABLA_IN = this.resourceUrl + '/find-by-codtabla-in';
    private FIND_BY_CODTABLA_IN_AND_COD_GENERICO_LIKE = this.resourceUrl + '/find-by-codtabla-in-and-codgenerico-like';
    private FIND_BREAKDOWN_STATUSES = this.resourceUrl + '/find-breakdown-statuses';
    private FIND_BY_CODTABLAANDCODGENERICOLIKEANDCODGENERICONOT = this.resourceUrl + '/findAllByCodTablaAndCodGenericoLikeAndCodGenericoNot';

    constructor(private http: HttpClient) {
    }

    create(descripcionCodigo: IDescripcionCodigo): Observable<EntityResponseType> {
        return this.http.post<IDescripcionCodigo>(this.resourceUrl, descripcionCodigo, { observe: 'response' });
    }

    update(descripcionCodigo: IDescripcionCodigo): Observable<EntityResponseType> {
        return this.http.put<IDescripcionCodigo>(this.resourceUrl, descripcionCodigo, { observe: 'response' });
    }

    find(codTabla: string, codGenerico: string, codRegStatus: number,glsGenericacorta:string,glsGenericalarga:string): Observable<EntityResponseType> {
        return this.http.get<IDescripcionCodigo>(`${ this.resourceUrl }/${ codTabla }/${ codGenerico }/${ codRegStatus }/${ glsGenericacorta }/${ glsGenericalarga }`, { observe: 'response' });
    }

    findByCodTabla(codTabla: string, sort: string): Observable<EntityArrayResponseType> {
        return this.http.get<IDescripcionCodigo[]>(`${ this.FIND_BY_CODTABLA }?codTabla=${ codTabla }&sort=${ sort }`, { observe: 'response' });
    }

    /**
     *
     * @param codTablas Is a CSV with the required codtabla.
     * @param sort
     */
    findByCodTablasIn(codTablas: string, sort: string): Observable<EntityArrayResponseType> {
        return this.http.get<IDescripcionCodigo[]>(`${ this.FIND_BY_CODTABLA_IN }?codTablas=${ codTablas }&sort=${ sort }`, {
            observe: 'response'
        });
    }

    /**
     *
     * @param codTablas Is a CSV with the required codtabla.
     * @param codGenerico
     * @param sort
     */
    findAllByCodTablaInAndCodGenericoStartsWithIgnoreCase(codTablas: string, codGenerico: string, sort: string): Observable<EntityArrayResponseType> {
        return this.http.get<IDescripcionCodigo[]>(
            `${ this.FIND_BY_CODTABLA_IN_AND_COD_GENERICO_LIKE }?codTablas=${ codTablas }&codGenerico=${ codGenerico }&sort=${ sort }`,
            {
                observe: 'response'
            }
        );
    }

    findBreakdownStatuses(): Observable<EntityArrayResponseType> {
        return this.http.get<IDescripcionCodigo[]>(
            `${ this.FIND_BREAKDOWN_STATUSES }`,
            {
                observe: 'response'
            }
        );
    }

    findAllByCodTablaAndCodGenericoLikeAndCodGenericoNot(
        codTablas: string,
        codGenerico: string,
        codGenerico1: String
    ): Observable<EntityArrayResponseType> {
        return this.http.get<IDescripcionCodigo[]>(
            `${ this.FIND_BY_CODTABLAANDCODGENERICOLIKEANDCODGENERICONOT }/${ codTablas }/${ codGenerico }/${ codGenerico1 }`,
            {
                observe: 'response'
            }
        );
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDescripcionCodigo[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(codTabla: string, codGenerico: string, codRegStatus: number,glsGenericacorta:string,glsGenericalarga:string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ codTabla }/${ codGenerico }/${ codRegStatus }/${ glsGenericacorta }/${ glsGenericalarga }`, { observe: 'response' });
    }
}
