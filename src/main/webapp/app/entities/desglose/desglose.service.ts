import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesglose } from 'app/shared/model/desglose.model';
import { IBreakdownFilterResponseDTO } from 'app/entities/desglose/filter-breakdown-response.model';
import { IBreakdownFilterRequestDTO } from 'app/entities/desglose/filter-breakdown-request.model';
import { IBreakdownSearchResponseDTO } from 'app/entities/desglose/search-breakdown-response.model';
import { IBreakdownSearchRequestDTO } from 'app/entities/desglose/search-breakdown-request.model';
import { IBreakdownEditRequestDTO } from 'app/entities/desglose/edit-breakdown-request.model';
import { IExtraDataDesglose } from 'app/shared/model/extra-data-desglose.model';
import { BreakdownSaveRequestDTO, IBreakdownSaveRequestDTO } from 'app/entities/desglose/save-breakdown-request.model';
import { IBreakdownRecoverResponseDTO } from 'app/entities/desglose/recover-breakdown-response.model';
import { IPeriodo } from 'app/shared/model/periodo.model';
import { isNullOrUndefined } from 'util';
import { IBreakdownCloneRequestDTO } from 'app/entities/desglose/clone-breakdown-request.model';
import { IBreakdownValidationError } from 'app/entities/desglose/breakdown-validation-error.model';
import { BreakdownSendRequest, IBreakdownSendRequest } from 'app/entities/desglose/send-breakdown-request.model';
import { IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';
import { IDesgloseRevision } from 'app/shared/model/desglose-revision.model';
import { IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';
import { ConceptValidationBreakdownRequestDTO, IConceptValidationBreakdownRequestDTO } from 'app/entities/desglose/concept-validation-breakdown-request.model';

type EntityBreakdownValidationError = HttpResponse<IBreakdownValidationError>;
type EntityArrayBreakdownValidationError = HttpResponse<IBreakdownValidationError[]>;
type EntityResponseType = HttpResponse<IDesglose>;
type EntityRecoverResponseType = HttpResponse<IBreakdownRecoverResponseDTO>;
type EntityArrayResponseType = HttpResponse<IDesglose[]>;
type EntityFilteredArrayResponseType = HttpResponse<IBreakdownFilterResponseDTO[]>;
type EntitySearchArrayResponseType = HttpResponse<IBreakdownSearchResponseDTO[]>;
type EntityBreakdownAprove = HttpResponse<IBreakdownSendRequest>;
type EntityArrayBreakdownAprove = HttpResponse<IBreakdownSendRequest[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseService {
    private resourceUrl = SERVER_API_URL + 'api/desgloses';
    private resourceUrlSave = SERVER_API_URL + 'api/desgloses-save';
    private resourceUrlSaveProy = SERVER_API_URL + 'api/desgloses-save-pry';
    private resourceUrlClone = SERVER_API_URL + 'api/desgloses-clone';
    private resourceFilterUrl = SERVER_API_URL + 'api/desgloses-filter';
    private resourceSearchUrl = SERVER_API_URL + 'api/desgloses-search';
    private resourceUrlEdit = SERVER_API_URL + 'api/desgloses-edit';
    private resourceUrlAprove = SERVER_API_URL + 'api/desgloses-approve';
    private resourceUrlReview = SERVER_API_URL + 'api/desgloses-review';
    private resourceUrlExecutor = SERVER_API_URL + 'api/desgloses-executor';
    private resourceUrlRecover = SERVER_API_URL + 'api/desgloses-recover';
    private resourceUrlDelete = SERVER_API_URL + 'api/desgloses-delete';
    private resourceUrlFree = SERVER_API_URL + 'api/desgloses-free';
    private resourceUrlFreeTemporal = SERVER_API_URL + 'api/desgloses-free-temporal';
    private resourceUrlValidations = SERVER_API_URL + 'api/desgloses-validations';

    constructor(private http: HttpClient) {
    }

    create(desglose: IDesglose): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desglose);
        return this.http.post<IDesglose>(this.resourceUrl, copy, { observe: 'response' }).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    save(desglose: IDesglose, extraDataDesglose: IExtraDataDesglose, activePeriodsIndexes: number[],activePeriodsEdit: number[], skipNotRequiredValidations?: boolean,isRapid?:boolean): Observable<EntityResponseType> {
        const copyDesglose = this.convertDateFromClient(desglose);
        const copyExtraDataDesglose = this.convertDateFromClientExtraData(extraDataDesglose, copyDesglose);
        const request: IBreakdownSaveRequestDTO = new BreakdownSaveRequestDTO(copyDesglose, copyExtraDataDesglose, activePeriodsIndexes,activePeriodsEdit,
            !isNullOrUndefined(skipNotRequiredValidations) ? skipNotRequiredValidations : false,!isNullOrUndefined(isRapid) ? isRapid : false
        );
        return this.http.post<IDesglose>(this.resourceUrlSave, request, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    saveProy(desglose: IDesglose, extraDataDesglose: IExtraDataDesglose): Observable<EntityResponseType> {
        const copyDesglose = this.convertDateFromClient(desglose);
        const copyExtraDataDesglose = this.convertDateFromClientExtraData(extraDataDesglose, copyDesglose);
        const request: IBreakdownSaveRequestDTO = new BreakdownSaveRequestDTO(copyDesglose, copyExtraDataDesglose);
        return this.http.post<IDesglose>(this.resourceUrlSaveProy, request, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    validate(desglose: IDesglose, extraDataDesglose: IExtraDataDesglose, activePeriodsIndexes: number[], skipNotRequiredValidations?: boolean): Observable<EntityArrayBreakdownValidationError> {
        const copyDesglose = this.convertDateFromClient(desglose);
        const copyExtraDataDesglose = this.convertDateFromClientExtraData(extraDataDesglose, copyDesglose);
        const request: IConceptValidationBreakdownRequestDTO = new ConceptValidationBreakdownRequestDTO(copyExtraDataDesglose, activePeriodsIndexes,
            !isNullOrUndefined(skipNotRequiredValidations) ? skipNotRequiredValidations : false);
        return this.http.post<IBreakdownValidationError[]>(this.resourceUrlValidations, request, { observe: 'response' }).pipe(
            map((res: EntityArrayBreakdownValidationError) => this.convertDateArrayBreakdownValidationErrorFromServer(res)));
    }

    sendForApproval(breakdownAproveRequest: IBreakdownSendRequest): Observable<EntityResponseType> {
        const copyDesglose = this.convertDateFromClient(breakdownAproveRequest.desglose);
        const copyExtraDataDesglose = this.convertDateFromClientExtraData(breakdownAproveRequest.extraDataDesglose, copyDesglose);
        const copyAuditoria = this.convertAuditoriaDateFromClient(breakdownAproveRequest.desgloseAuditoria);
        const copyRevision = this.convertRevisionDateFromClient(breakdownAproveRequest.desgloseRevision);
        const copyMensaje = this.convertMessageDateFromClient(breakdownAproveRequest.desgloseMensaje);
        const request: IBreakdownSendRequest = new BreakdownSendRequest(copyDesglose,
            copyAuditoria, copyRevision, copyMensaje, copyExtraDataDesglose);
        return this.http.post<IDesglose>(this.resourceUrlAprove, request, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    sendReview(breakdownAproveRequest: IBreakdownSendRequest): Observable<EntityResponseType> {
        const copyDesglose = this.convertDateFromClient(breakdownAproveRequest.desglose);
        const copyExtraDataDesglose = this.convertDateFromClientExtraData(breakdownAproveRequest.extraDataDesglose, copyDesglose);
        const copyAuditoria = this.convertAuditoriaDateFromClient(breakdownAproveRequest.desgloseAuditoria);
        const copyRevision = this.convertRevisionDateFromClient(breakdownAproveRequest.desgloseRevision);
        const copyMensaje = this.convertMessageDateFromClient(breakdownAproveRequest.desgloseMensaje);
        const request: IBreakdownSendRequest = new BreakdownSendRequest(copyDesglose, copyAuditoria, copyRevision, copyMensaje, copyExtraDataDesglose);
        return this.http.post<IDesglose>(this.resourceUrlReview, request, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    sendExecutor(breakdownAproveRequest: IBreakdownSendRequest): Observable<EntityResponseType> {
        const copyDesglose = this.convertDateFromClient(breakdownAproveRequest.desglose);
        const copyExtraDataDesglose = this.convertDateFromClientExtraData(breakdownAproveRequest.extraDataDesglose, copyDesglose);
        const copyAuditoria = this.convertAuditoriaDateFromClient(breakdownAproveRequest.desgloseAuditoria);
        const copyRevision = this.convertRevisionDateFromClient(breakdownAproveRequest.desgloseRevision);
        const copyMensaje = this.convertMessageDateFromClient(breakdownAproveRequest.desgloseMensaje);
        const request: IBreakdownSendRequest = new BreakdownSendRequest(copyDesglose,
            copyAuditoria, copyRevision, copyMensaje, copyExtraDataDesglose);
        return this.http.post<IDesglose>(this.resourceUrlExecutor, request, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    // ejemplo de respuesta esperada para un método void Observable<HttpResponse<any>> {
    clone(breakdownCloneRequestDTO: IBreakdownCloneRequestDTO): Observable<EntityRecoverResponseType> {
        return this.http.post<IBreakdownRecoverResponseDTO>(this.resourceUrlClone, breakdownCloneRequestDTO, { observe: 'response' }).pipe(
            map((res: EntityRecoverResponseType) => this.convertDateFromServerBreakdownRecover(res)));
    }

    deleteDesglo(desglose: IDesglose): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceUrlDelete, desglose, { observe: 'response' });
    }

    recover(breakdownSearchRequestDTO: IBreakdownSearchRequestDTO): Observable<EntityRecoverResponseType> {
        return this.http.post<IBreakdownRecoverResponseDTO>(this.resourceUrlRecover, breakdownSearchRequestDTO, { observe: 'response' }).pipe(
            map((res: EntityRecoverResponseType) => this.convertDateFromServerBreakdownRecover(res)));
    }

    update(desglose: IDesglose): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desglose);
        return this.http.put<IDesglose>(this.resourceUrl, copy, { observe: 'response' }).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    clearBreakdownUserOpenAndSetStatusInProcess(desglose: IDesglose): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(desglose);
        return this.http.post<IDesglose>(this.resourceUrlFree, copy, { observe: 'response' }).pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    clearBreakdownUserOpenAndSetStatusInProcessTemporal(): Observable<HttpResponse<any>> {
        return this.http.post<IDesglose>(this.resourceUrlFreeTemporal, null,{ observe: 'response' });
    }

    editBreakdown(breakdownEditRequestDTO: IBreakdownEditRequestDTO): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(breakdownEditRequestDTO.desglose);
        breakdownEditRequestDTO.desglose = copy;
        return this.http.put<IDesglose>(this.resourceUrlEdit, breakdownEditRequestDTO, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(codCliente: string, codTipoDes: string, codSubtipoDes: string, codMoneda: string, codPlantilla: string): Observable<EntityResponseType> {
        return this.http.get<IDesglose>(`${ this.resourceUrl }/${ codCliente }/${ codTipoDes }/${ codSubtipoDes }/${ codMoneda }/${ codPlantilla }`, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDesglose[]>(this.resourceUrl, { params: options, observe: 'response' }).pipe(
            map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    breakdownFilter(breakdownFilterRequestDTO: IBreakdownFilterRequestDTO): Observable<EntityFilteredArrayResponseType> {
        const copy = this.convertFilterDateFromClient(breakdownFilterRequestDTO);
        return this.http.post<IBreakdownFilterResponseDTO[]>(this.resourceFilterUrl, copy, { observe: 'response' }).pipe(
            map((res: EntityFilteredArrayResponseType) => this.convertDateArrayFilteredFromServer(res)));
    }

    breakdownSearch(breakdownSearchRequestDTO: IBreakdownSearchRequestDTO): Observable<EntitySearchArrayResponseType> {
        return this.http.post<IBreakdownSearchResponseDTO[]>(this.resourceSearchUrl, breakdownSearchRequestDTO, { observe: 'response' }).pipe(
            map((res: EntitySearchArrayResponseType) => this.convertDateArraySearchedFromServer(res)));
    }

    delete(codCliente: string, codTipoDes: string, codSubtipoDes: string, codMoneda: string, codPlantilla: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ codCliente }/${ codTipoDes }/${ codSubtipoDes }/${ codMoneda }/${ codPlantilla }`, {
            observe: 'response'
        });
    }

    private convertMessageDateFromClient(desgloseMensaje: IDesgloseMensaje): IDesgloseMensaje {
        const copy: IDesgloseMensaje = Object.assign({}, desgloseMensaje, {
            fecEvent: desgloseMensaje.fecEvent != null && desgloseMensaje.fecEvent.isValid() ? desgloseMensaje.fecEvent.toJSON() : null
        });
        return copy;
    }

    private convertRevisionDateFromClient(desgloseRevision: IDesgloseRevision): IDesgloseRevision {
        const copy: IDesgloseRevision = Object.assign({}, desgloseRevision, {
            fecEnvioRevision:
                desgloseRevision.fecEnvioRevision != null && desgloseRevision.fecEnvioRevision.isValid() ? desgloseRevision.fecEnvioRevision.toJSON() : null,
            fecEnRevision:
                desgloseRevision.fecEnRevision != null && desgloseRevision.fecEnRevision.isValid() ? desgloseRevision.fecEnRevision.format(DATE_FORMAT) : null,
            fecTerminoRevision:
                desgloseRevision.fecTerminoRevision != null && desgloseRevision.fecTerminoRevision.isValid() ? desgloseRevision.fecTerminoRevision.toJSON() : null
        });
        return copy;
    }

    private convertAuditoriaDateFromClient(desgloseAuditoria: IDesgloseAuditoria): IDesgloseAuditoria {
        const copy: IDesgloseAuditoria = Object.assign({}, desgloseAuditoria, {
            fecEvento: desgloseAuditoria.fecEvento != null && desgloseAuditoria.fecEvento.isValid() ? desgloseAuditoria.fecEvento.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertFilterDateFromClient(filtroDesgloseDTO: IBreakdownFilterRequestDTO): IBreakdownFilterRequestDTO {
        const copy: IBreakdownFilterRequestDTO = Object.assign({}, filtroDesgloseDTO, {
            fecCrea1: filtroDesgloseDTO.fecCrea1 != null && filtroDesgloseDTO.fecCrea1.isValid() ? filtroDesgloseDTO.fecCrea1.format(DATE_FORMAT) : null,
            fecCrea2: filtroDesgloseDTO.fecCrea2 != null && filtroDesgloseDTO.fecCrea2.isValid() ? filtroDesgloseDTO.fecCrea2.format(DATE_FORMAT) : null,
            fecDef1: filtroDesgloseDTO.fecDef1 != null && filtroDesgloseDTO.fecDef1.isValid() ? filtroDesgloseDTO.fecDef1.format(DATE_FORMAT) : null,
            fecDef2: filtroDesgloseDTO.fecDef2 != null && filtroDesgloseDTO.fecDef2.isValid() ? filtroDesgloseDTO.fecDef2.format(DATE_FORMAT) : null,
            fecRevi1: filtroDesgloseDTO.fecRevi1 != null && filtroDesgloseDTO.fecRevi1.isValid() ? filtroDesgloseDTO.fecRevi1.format(DATE_FORMAT) : null,
            fecRevi2: filtroDesgloseDTO.fecRevi2 != null && filtroDesgloseDTO.fecRevi2.isValid() ? filtroDesgloseDTO.fecRevi2.format(DATE_FORMAT) : null,
            fecEnvRevi1: filtroDesgloseDTO.fecEnvRevi1 != null && filtroDesgloseDTO.fecEnvRevi1.isValid() ? filtroDesgloseDTO.fecEnvRevi1.format(DATE_FORMAT) : null,
            fecEnvRevi2: filtroDesgloseDTO.fecEnvRevi2 != null && filtroDesgloseDTO.fecEnvRevi2.isValid() ? filtroDesgloseDTO.fecEnvRevi2.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromClient(desglose: IDesglose): IDesglose {
        const copy: IDesglose = Object.assign({}, desglose, {
            fecUserCrea: desglose.fecUserCrea != null && desglose.fecUserCrea.isValid() ? desglose.fecUserCrea.toJSON() : null,
            fecUserModif: desglose.fecUserModif != null && desglose.fecUserModif.isValid() ? desglose.fecUserModif.toJSON() : null,
            fecDeflacBase: desglose.fecDeflacBase != null && desglose.fecDeflacBase.isValid() ? desglose.fecDeflacBase.format(DATE_FORMAT) : null,
            fecHraOpen: desglose.fecHraOpen != null && desglose.fecHraOpen.isValid() ? desglose.fecHraOpen.toJSON() : null,
            fecDesgloDef: desglose.fecDesgloDef != null && desglose.fecDesgloDef.isValid() ? desglose.fecDesgloDef.toJSON() : null,
            fecDesgloEnvPdf: desglose.fecDesgloEnvPdf != null && desglose.fecDesgloEnvPdf.isValid() ? desglose.fecDesgloEnvPdf.format(DATE_FORMAT) : null,
            fecPeriodo1Rpt: desglose.fecPeriodo1Rpt != null && desglose.fecPeriodo1Rpt.isValid() ? desglose.fecPeriodo1Rpt.format(DATE_FORMAT) : null,
            fecPeriodo2Rpt: desglose.fecPeriodo2Rpt != null && desglose.fecPeriodo2Rpt.isValid() ? desglose.fecPeriodo2Rpt.format(DATE_FORMAT) : null,
            fecPeriodo3Rpt: desglose.fecPeriodo3Rpt != null && desglose.fecPeriodo3Rpt.isValid() ? desglose.fecPeriodo3Rpt.format(DATE_FORMAT) : null,
            fecPeriodo4Rpt: desglose.fecPeriodo4Rpt != null && desglose.fecPeriodo4Rpt.isValid() ? desglose.fecPeriodo4Rpt.format(DATE_FORMAT) : null,
            fecPerConsol1Rpt: desglose.fecPerConsol1Rpt != null && desglose.fecPerConsol1Rpt.isValid() ? desglose.fecPerConsol1Rpt.format(DATE_FORMAT) : null,
            fecPerConsol2Rpt: desglose.fecPerConsol2Rpt != null && desglose.fecPerConsol2Rpt.isValid() ? desglose.fecPerConsol2Rpt.format(DATE_FORMAT) : null,
            fecUlt1Rpt: desglose.fecUlt1Rpt != null && desglose.fecUlt1Rpt.isValid() ? desglose.fecUlt1Rpt.format(DATE_FORMAT) : null,
            fecUlt2Rpt: desglose.fecUlt2Rpt != null && desglose.fecUlt2Rpt.isValid() ? desglose.fecUlt2Rpt.format(DATE_FORMAT) : null,
            fecUserModifRpt: desglose.fecUserModifRpt != null && desglose.fecUserModifRpt.isValid() ? desglose.fecUserModifRpt.format(DATE_FORMAT) : null,
            fecGeneracionRpt: desglose.fecGeneracionRpt != null && desglose.fecGeneracionRpt.isValid() ? desglose.fecGeneracionRpt.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromClientExtraData(extraDataDesglose: IExtraDataDesglose, desglose: IDesglose): IExtraDataDesglose {
        const fecPeriodoCopy = [];
        const periodosCopy = [];
        if (extraDataDesglose.fecPeriodo != null && extraDataDesglose.fecPeriodo.length > 0) {
            extraDataDesglose.fecPeriodo.forEach((periodDate, i) => {
                const fechaPeriodo = periodDate != null && periodDate.isValid() ? periodDate.format(DATE_FORMAT) : null;
                fecPeriodoCopy.push(fechaPeriodo);
                const periodo = {
                    codCliente: desglose.codCliente,
                    codTipoDes: desglose.codTipoDes,
                    codSubtipoDes: desglose.codSubtipoDes,
                    codMoneda: desglose.codMoneda,
                    fecPeriodo: fechaPeriodo,
                    numMeses: !isNullOrUndefined(extraDataDesglose.monthsPeriod[ i ]) ? extraDataDesglose.monthsPeriod[ i ] : null,
                    valDeflacPrd: !isNullOrUndefined(extraDataDesglose.deflatorPeriod[ i ]) ? extraDataDesglose.deflatorPeriod[ i ] : null,
                    codAuditor: !isNullOrUndefined(extraDataDesglose.auditors[ i ]) && !isNullOrUndefined(extraDataDesglose.auditors[ i ].auditor) ?
                        extraDataDesglose.auditors[ i ].auditor.codGenerico : null,
                    codEstadoAudi: !isNullOrUndefined(extraDataDesglose.periodRating[ i ]) ? extraDataDesglose.periodRating[ i ] : null,
                    glsOtrosAudit: !isNullOrUndefined(extraDataDesglose.auditors[ i ]) ? extraDataDesglose.auditors[ i ].otherAuditorName : null
                };
                periodosCopy.push(periodo);
            });
        }

        const copy: IExtraDataDesglose = Object.assign({}, extraDataDesglose, {
            fecPeriodo: fecPeriodoCopy,
            periodos: periodosCopy
        });

        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecUserCrea = res.body.fecUserCrea != null ? moment(res.body.fecUserCrea) : null;
        res.body.fecUserModif = res.body.fecUserModif != null ? moment(res.body.fecUserModif) : null;
        res.body.fecDeflacBase = res.body.fecDeflacBase != null ? moment(res.body.fecDeflacBase) : null;
        res.body.fecHraOpen = res.body.fecHraOpen != null ? moment(res.body.fecHraOpen) : null;
        res.body.fecDesgloDef = res.body.fecDesgloDef != null ? moment(res.body.fecDesgloDef) : null;
        res.body.fecDesgloEnvPdf = res.body.fecDesgloEnvPdf != null ? moment(res.body.fecDesgloEnvPdf) : null;
        res.body.fecPeriodo1Rpt = res.body.fecPeriodo1Rpt != null ? moment(res.body.fecPeriodo1Rpt) : null;
        res.body.fecPeriodo2Rpt = res.body.fecPeriodo2Rpt != null ? moment(res.body.fecPeriodo2Rpt) : null;
        res.body.fecPeriodo3Rpt = res.body.fecPeriodo3Rpt != null ? moment(res.body.fecPeriodo3Rpt) : null;
        res.body.fecPeriodo4Rpt = res.body.fecPeriodo4Rpt != null ? moment(res.body.fecPeriodo4Rpt) : null;
        res.body.fecPerConsol1Rpt = res.body.fecPerConsol1Rpt != null ? moment(res.body.fecPerConsol1Rpt) : null;
        res.body.fecPerConsol2Rpt = res.body.fecPerConsol2Rpt != null ? moment(res.body.fecPerConsol2Rpt) : null;
        res.body.fecUlt1Rpt = res.body.fecUlt1Rpt != null ? moment(res.body.fecUlt1Rpt) : null;
        res.body.fecUlt2Rpt = res.body.fecUlt2Rpt != null ? moment(res.body.fecUlt2Rpt) : null;
        res.body.fecUserModifRpt = res.body.fecUserModifRpt != null ? moment(res.body.fecUserModifRpt) : null;
        res.body.fecGeneracionRpt = res.body.fecGeneracionRpt != null ? moment(res.body.fecGeneracionRpt) : null;
        return res;
    }

    private convertDateFromServerBreakdownRecover(res: EntityRecoverResponseType): EntityRecoverResponseType {
        if (!isNullOrUndefined(res.body.extraDataDTO.periodos)) {
            res.body.extraDataDTO.periodos.forEach((periodo: IPeriodo) => {
                periodo.fecPeriodo = periodo.fecPeriodo != null ? moment(periodo.fecPeriodo) : null;
                periodo.fecUserCrea = periodo.fecUserCrea != null ? moment(periodo.fecUserCrea) : null;
                periodo.fecUserModif = periodo.fecUserModif != null ? moment(periodo.fecUserModif) : null;
                periodo.fecInformSbif = periodo.fecInformSbif != null ? moment(periodo.fecInformSbif) : null;
            });
        }
        if (!isNullOrUndefined(res.body.extraDataDTO.fecPeriodo)) {
            for (let i = 0; i < res.body.extraDataDTO.fecPeriodo.length; i++) {
                res.body.extraDataDTO.fecPeriodo[ i ] = res.body.extraDataDTO.fecPeriodo[ i ] != null ? moment(res.body.extraDataDTO.fecPeriodo[ i ]) : null;
            }
        }
        if (!isNullOrUndefined(res.body.extraDataDTO.deflatorPeriod) && res.body.extraDataDTO.deflatorPeriod.length > 0) {
            res.body.extraDataDTO.deflatorPeriodStr = [];
            for (let i = 0; i < res.body.extraDataDTO.deflatorPeriod.length; i++) {
                res.body.extraDataDTO.deflatorPeriodStr[ i ] = !isNullOrUndefined(res.body.extraDataDTO.deflatorPeriod[ i ] != null) ?
                    res.body.extraDataDTO.deflatorPeriod[ i ].toString(10).replace('.', ',') : null;
            }
        }
        if (!isNullOrUndefined(res.body.extraDataDTO.empConsolPeriodoDesgConsolList)) {
            for (let i = 0; i < res.body.extraDataDTO.empConsolPeriodoDesgConsolList.length; i++) {
                res.body.extraDataDTO.empConsolPeriodoDesgConsolList[ i ].fecPeriodo = res.body.extraDataDTO.empConsolPeriodoDesgConsolList[ i ].fecPeriodo != null ?
                    moment(res.body.extraDataDTO.empConsolPeriodoDesgConsolList[ i ].fecPeriodo) : null;
            }
        }
        if (!isNullOrUndefined(res.body.extraDataDTO.periodoDesgConsols)) {
            for (let i = 0; i < res.body.extraDataDTO.periodoDesgConsols.length; i++) {
                res.body.extraDataDTO.periodoDesgConsols[ i ].fecPeriodo = res.body.extraDataDTO.periodoDesgConsols[ i ].fecPeriodo != null ?
                    moment(res.body.extraDataDTO.periodoDesgConsols[ i ].fecPeriodo) : null;
            }
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((desglose: IDesglose) => {
            desglose.fecUserCrea = desglose.fecUserCrea != null ? moment(desglose.fecUserCrea) : null;
            desglose.fecUserModif = desglose.fecUserModif != null ? moment(desglose.fecUserModif) : null;
            desglose.fecDeflacBase = desglose.fecDeflacBase != null ? moment(desglose.fecDeflacBase) : null;
            desglose.fecHraOpen = desglose.fecHraOpen != null ? moment(desglose.fecHraOpen) : null;
            desglose.fecDesgloDef = desglose.fecDesgloDef != null ? moment(desglose.fecDesgloDef) : null;
            desglose.fecDesgloEnvPdf = desglose.fecDesgloEnvPdf != null ? moment(desglose.fecDesgloEnvPdf) : null;
            desglose.fecPeriodo1Rpt = desglose.fecPeriodo1Rpt != null ? moment(desglose.fecPeriodo1Rpt) : null;
            desglose.fecPeriodo2Rpt = desglose.fecPeriodo2Rpt != null ? moment(desglose.fecPeriodo2Rpt) : null;
            desglose.fecPeriodo3Rpt = desglose.fecPeriodo3Rpt != null ? moment(desglose.fecPeriodo3Rpt) : null;
            desglose.fecPeriodo4Rpt = desglose.fecPeriodo4Rpt != null ? moment(desglose.fecPeriodo4Rpt) : null;
            desglose.fecPerConsol1Rpt = desglose.fecPerConsol1Rpt != null ? moment(desglose.fecPerConsol1Rpt) : null;
            desglose.fecPerConsol2Rpt = desglose.fecPerConsol2Rpt != null ? moment(desglose.fecPerConsol2Rpt) : null;
            desglose.fecUlt1Rpt = desglose.fecUlt1Rpt != null ? moment(desglose.fecUlt1Rpt) : null;
            desglose.fecUlt2Rpt = desglose.fecUlt2Rpt != null ? moment(desglose.fecUlt2Rpt) : null;
            desglose.fecUserModifRpt = desglose.fecUserModifRpt != null ? moment(desglose.fecUserModifRpt) : null;
            desglose.fecGeneracionRpt = desglose.fecGeneracionRpt != null ? moment(desglose.fecGeneracionRpt) : null;
        });
        return res;
    }

    private convertDateArrayBreakdownValidationErrorFromServer(res: EntityArrayBreakdownValidationError): EntityArrayBreakdownValidationError {
        res.body.forEach((breakdownValidationError: IBreakdownValidationError) => {
            breakdownValidationError.period = breakdownValidationError.period != null ? moment(breakdownValidationError.period) : null;
        });
        return res;
    }

    private convertDateArrayFilteredFromServer(res: EntityFilteredArrayResponseType): EntityFilteredArrayResponseType {
        res.body.forEach((desglose: IBreakdownFilterResponseDTO) => {
            desglose.fechaDesglose = desglose.fechaDesglose != null ? moment(desglose.fechaDesglose) : null;
            desglose.fechaLastUpdate = desglose.fechaLastUpdate != null ? moment(desglose.fechaLastUpdate) : null;
            desglose.fechaDesglosefechaDesglose = desglose.fechaDesglosefechaDesglose != null ? moment(desglose.fechaDesglosefechaDesglose) : null;
            desglose.fechaDesgloDef = desglose.fechaDesgloDef != null ? moment(desglose.fechaDesgloDef) : null;
            desglose.fechaRevision = desglose.fechaRevision != null ? moment(desglose.fechaRevision) : null;
            desglose.fechaEnvioRevision = desglose.fechaEnvioRevision != null ? moment(desglose.fechaEnvioRevision) : null;
            if (desglose.fechaPeriodos != null && desglose.fechaPeriodos.length > 0) {
                for (let i = 0; i < desglose.fechaPeriodos.length; i++) {
                    desglose.fechaPeriodos[ i ] = desglose.fechaPeriodos[ i ] != null ? moment(desglose.fechaPeriodos[ i ]) : null;
                }
            }
        });
        return res;
    }

    private convertDateArraySearchedFromServer(res: EntitySearchArrayResponseType): EntitySearchArrayResponseType {
        res.body.forEach((desglose: IBreakdownSearchResponseDTO) => {
            desglose.fecDeflacBase = desglose.fecDeflacBase != null ? moment(desglose.fecDeflacBase) : null;
            desglose.fechaModif = desglose.fechaModif != null ? moment(desglose.fechaModif) : null;
            desglose.fecPeriodo = desglose.fecPeriodo != null ? moment(desglose.fecPeriodo) : null;
        });
        return res;
    }
}
