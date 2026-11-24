import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConceptoImpresion } from 'app/shared/model/concepto-impresion.model';
import { IConceptoImpresionRequestDTO } from 'app/entities/concepto-impresion/concepto-impresion-request.model';

type EntityResponseType = HttpResponse<IConceptoImpresion>;
type EntityArrayResponseType = HttpResponse<IConceptoImpresion[]>;

@Injectable({ providedIn: 'root' })
export class ConceptoImpresionService {
    private resourceUrl = SERVER_API_URL + 'api/concepto-impresions';
    private resourceUrlFilter = SERVER_API_URL + 'api/concepto-impresions/filter-c-t-st-m';

    constructor(private http: HttpClient) {
    }

    create(conceptoImpresion: IConceptoImpresion): Observable<EntityResponseType> {
        return this.http.post<IConceptoImpresion>(this.resourceUrl, conceptoImpresion, { observe: 'response' });
    }

    update(conceptoImpresion: IConceptoImpresion): Observable<EntityResponseType> {
        return this.http.put<IConceptoImpresion>(this.resourceUrl, conceptoImpresion, { observe: 'response' });
    }

    find(codCliente: string, codTipoDes: number, codSubtipoDes: number, codMoneda: string, codTipoConcep: string, corConcepto: number): Observable<EntityResponseType> {
        return this.http.get<IConceptoImpresion>(
            `${this.resourceUrl}/${codCliente}/${codTipoDes}/${codSubtipoDes}/${codMoneda}/${codTipoConcep}/${corConcepto}`,
            { observe: 'response' }
        );
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConceptoImpresion[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    filterByConceptoImpresionRequestDTO(conceptoImpresionRequestDTO: IConceptoImpresionRequestDTO): Observable<EntityArrayResponseType> {
        return this.http.post<IConceptoImpresion[]>(this.resourceUrlFilter, conceptoImpresionRequestDTO, { observe: 'response' });
    }

    delete(codCliente: string, codTipoDes: number, codSubtipoDes: number, codMoneda: string, codTipoConcep: string, corConcepto: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(
            `${this.resourceUrl}/${codCliente}/${codTipoDes}/${codSubtipoDes}/${codMoneda}/${codTipoConcep}/${corConcepto}`,
            { observe: 'response' }
        );
    }
}
