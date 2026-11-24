import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConcepto } from 'app/shared/model/concepto.model';
import { IConceptRequestDTO } from 'app/entities/concepto/filter-concept-request.model';
import { IBreakdownFilterConceptDto } from 'app/shared/model/breakdown-filter-concept-dto';
import {IBreakdownValidationError} from 'app/entities/desglose/breakdown-validation-error.model';

type EntityResponseType = HttpResponse<IConcepto>;
type EntityArrayResponseType = HttpResponse<IConcepto[]>;
type EntityArrayResponse1Type = HttpResponse<IBreakdownFilterConceptDto>;
type EntityMatrixResponseType = HttpResponse<IConcepto[][]>;
type EntityBreakdownValidationError = HttpResponse<IBreakdownValidationError>;
type EntityArrayBreakdownValidationError = HttpResponse<IBreakdownValidationError[]>;

@Injectable({ providedIn: 'root' })
export class ConceptoService {
    private resourceUrl = SERVER_API_URL + 'api/conceptos';
    private resourceUrlFindByTemplateConceptNotLike = this.resourceUrl + '/find-by-template-concept-not-like';
    private resourceUrlFindByTemplateConceptsCodesIn = this.resourceUrl + '/find-by-template-cod-concepts-in';
    private resourceUrlValidations = this.resourceUrl + '-validations';
    private resourceUrlValidationsAll = this.resourceUrl + '-validations-all';

    constructor(private http: HttpClient) {
    }

    create(concepto: IConcepto): Observable<EntityResponseType> {
        return this.http.post<IConcepto>(this.resourceUrl, concepto, { observe: 'response' });
    }

    update(concepto: IConcepto): Observable<EntityResponseType> {
        return this.http.put<IConcepto>(this.resourceUrl, concepto, { observe: 'response' });
    }
    resecuenciar(codTipoPlantilla:number): Observable<any> {
       return  this.http.put<any>(`${ this.resourceUrl }/resecuenciar/${ codTipoPlantilla }`, { observe: 'response' });
    }

    find(codTipoConcep: string, corConcepto: number, codTipoPlantilla: number): Observable<EntityResponseType> {
        return this.http.get<IConcepto>(`${ this.resourceUrl }/${ codTipoConcep }/${ corConcepto }/${ codTipoPlantilla }`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConcepto[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryConcepts(conceptRequestDTO: IConceptRequestDTO): Observable<EntityArrayResponseType> {
        return this.http.post<IConcepto[]>(this.resourceUrlFindByTemplateConceptNotLike, conceptRequestDTO, { observe: 'response' });
    }

    findByTemplateConceptsCodesIn(conceptRequestDTO: IConceptRequestDTO): Observable<EntityArrayResponse1Type> {
        return this.http.post<IBreakdownFilterConceptDto>(this.resourceUrlFindByTemplateConceptsCodesIn, conceptRequestDTO, { observe: 'response' });
    }
    validations(concepto: IConcepto): Observable<EntityArrayBreakdownValidationError> {
        return this.http.post<IBreakdownValidationError[]>(this.resourceUrlValidations, concepto, { observe: 'response' });
    }
    validationsAll(concepto: IConcepto): Observable<EntityArrayBreakdownValidationError> {
        return this.http.post<IBreakdownValidationError[]>(this.resourceUrlValidationsAll, concepto, { observe: 'response' });
    }

    queryTipoPlantillalike(tipoPlantilla: number, codTipoConcepto: string, corConcepto: string, flgConActivo: number): Observable<EntityArrayResponseType> {
        return this.http.get<IConcepto[]>(`${ this.resourceUrl }/byplantillalike/${ tipoPlantilla }/${ codTipoConcepto }/${ corConcepto }/${ flgConActivo }`, { observe: 'response' });
    }

    delete(codTipoConcep: string, corConcepto: number, codTipoPlantilla: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ codTipoConcep }/${ corConcepto }/${ codTipoPlantilla }`, { observe: 'response' });
    }
}
