import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { ICliente } from 'app/shared/model/cliente.model';
import { isNullOrUndefined } from 'util';
import * as moment from 'moment';

type EntityResponseType = HttpResponse<ICliente>;
type EntityArrayResponseType = HttpResponse<ICliente[]>;

@Injectable({ providedIn: 'root' })
export class ClienteService {
    private findByRutResourceUrl = SERVER_API_URL + 'api/cliente/findByRut';
    private findByNameResourceUrl = SERVER_API_URL + 'api/cliente/findByName';
    private saveClientResourceUrl = SERVER_API_URL + 'api/cliente/save';

    constructor(private http: HttpClient) {
    }

    create(cliente: ICliente): Observable<EntityResponseType> {
        // const copy = this.convertRutFromClient(cliente);
        return this.http
        .post<ICliente>(this.saveClientResourceUrl, cliente, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertRutFromServer(res)));
    }

    findByRut(rut: string, codTipoDes: string, codSubTipoDes: string, codMoneda: string, codTipoPlantillaDes: string): Observable<EntityArrayResponseType> {
        return this.http.get<ICliente[]>(
            `${this.findByRutResourceUrl}?rut=${rut}&codTipoDes=${codTipoDes}&codSubTipoDes=${codSubTipoDes}&codMoneda=${codMoneda}&codTipoPlantillaDes=${codTipoPlantillaDes}`,
            { observe: 'response' }).pipe(map((res: EntityArrayResponseType) => this.convertRutArrayFromServer(res)));
    }

    findByNameContains(name: string, codTipoDes: string, codSubTipoDes: string, codMoneda: string, codTipoPlantillaDes: string): Observable<EntityArrayResponseType> {
        return this.http.get<ICliente[]>(
            `${this.findByNameResourceUrl}?name=${name}&codTipoDes=${codTipoDes}&codSubTipoDes=${codSubTipoDes}&codMoneda=${codMoneda}&codTipoPlantillaDes=${codTipoPlantillaDes}`,
            { observe: 'response' }).pipe(map((res: EntityArrayResponseType) => this.convertRutArrayFromServer(res)));
    }

    private convertRutArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((cliente: ICliente) => {
            if (!isNullOrUndefined(cliente.rut) && cliente.rut.length > 0) {
                cliente.rutVerificationNumber = cliente.rut.slice(-1);
                cliente.rut = cliente.rut.slice(0, -1);
            }
            if (!isNullOrUndefined(cliente.breakdownSearchResponseDTO)) {
                cliente.breakdownSearchResponseDTO.fechaModif =
                    !isNullOrUndefined(cliente.breakdownSearchResponseDTO.fechaModif) ? moment(cliente.breakdownSearchResponseDTO.fechaModif) : null;
                cliente.breakdownSearchResponseDTO.fecDeflacBase =
                    !isNullOrUndefined(cliente.breakdownSearchResponseDTO.fecDeflacBase) ? moment(cliente.breakdownSearchResponseDTO.fecDeflacBase) : null;
            }
        });
        return res;
    }

    private convertRutFromServer(res: EntityResponseType): EntityResponseType {
        res.body.rut = !isNullOrUndefined(res.body.rut) && res.body.rut.length > 0 ? res.body.rut.slice(0, -1) : null;
        res.body.rutVerificationNumber = !isNullOrUndefined(res.body.rutVerificationNumber) && res.body.rutVerificationNumber.length > 0 ? res.body.rutVerificationNumber : null;
        return res;
    }

    private convertRutFromClient(cliente: ICliente): ICliente {
        const copy: ICliente = Object.assign({}, cliente, {
            rut:
                !isNullOrUndefined(cliente.rut) &&
                cliente.rut.trim().length > 0 &&
                !isNullOrUndefined(cliente.rutVerificationNumber) &&
                cliente.rutVerificationNumber.trim().length > 0
                    ? cliente.rut + '' + cliente.rutVerificationNumber
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.breakdownSearchResponseDTO.fechaModif = res.body.breakdownSearchResponseDTO.fechaModif != null ? moment(res.body.breakdownSearchResponseDTO.fechaModif) : null;
        res.body.breakdownSearchResponseDTO.fecDeflacBase =
            res.body.breakdownSearchResponseDTO.fecDeflacBase != null ? moment(res.body.breakdownSearchResponseDTO.fecDeflacBase) : null;
        return res;
    }
}
