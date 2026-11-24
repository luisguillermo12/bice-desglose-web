import { Pipe, PipeTransform } from '@angular/core';
import { isNullOrUndefined } from 'util';
import { ICliente } from 'app/shared/model/cliente.model';

export const enum ClientFilterValue {
    RUT_COMPANY = 'RUT_COMPANY',
    RUT_PERSON = 'RUT_PERSON',
    NO_RUT = 'NO_RUT'
}

/*
 * Filtra los valores del array que sean clientes con rut o sin rut dependiendo del valor del parámetro filterValue.
 * Usage:
 *   value | filterClientsRutNoRutPipe:filterValue
 * Example:
 *   {{ [elem1, elem2, elem3] | filterClientsRutNoRutPipe:RUT_COMPANY }}
 *   resulta en : [elem1]
 *
 *  Valid filterValue values: RUT_COMPANY, RUT_PERSON, NO_RUT according to the "ClientFilterValue" enum.
*/
@Pipe({ name: 'filterClientsRutNoRutPipe' })
export class FilterClientsRutNoRutPipe implements PipeTransform {
    transform(value: Array<ICliente>, filterValue: string): Array<ICliente> {
        if (!isNullOrUndefined(value) && !isNullOrUndefined(filterValue)) {
            return value.filter(elem => elem.clientType === filterValue);
        }
    }
}
