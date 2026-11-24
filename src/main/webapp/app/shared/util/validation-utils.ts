import { Injectable } from '@angular/core';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { isNullOrUndefined } from 'util';

@Injectable({ providedIn: 'root' })
export class ValidationUtils {

    public getDescripcionCodigoByCodGenerico(descripcionCodigos: IDescripcionCodigo[], codGenerico?: string): IDescripcionCodigo {
        let descripcionCodigo: IDescripcionCodigo = null;
        if (!isNullOrUndefined(descripcionCodigos) && descripcionCodigos.length > 0) {
            const iDescripcionCodigos = descripcionCodigos.filter(el => el.codGenerico === codGenerico);
            if (!isNullOrUndefined(iDescripcionCodigos) && iDescripcionCodigos.length === 1) {
                descripcionCodigo = iDescripcionCodigos [0];
            } else {
                throw new Error('ERROR ID DUPLICADO');
            }
        }
        return descripcionCodigo;
    }

    private rutClean(value) {
        return typeof value === 'string' ? value.replace(/[^0-9kK]+/g, '').toUpperCase() : '';
    }

    /**
     * Obtiene el dígito verificador de un número de RUT
     * @param rutDigits
     */
    public getRutVerificationNumber(rutDigits: number): string {
        let m = 0;
        let s = 1;
        while (rutDigits > 0) {
            s = (s + (rutDigits % 10) * (9 - m++ % 6)) % 11;
            rutDigits = Math.floor(rutDigits / 10);
        }
        const checkDigit = s > 0 ? String(s - 1) : 'K';
        return checkDigit;
    }

    public rutValidate(value): boolean {
        if (typeof value !== 'string') {
            return false;
        }
        const rut = this.rutClean(value);
        let rutDigits = parseInt(rut.slice(0, -1), 10);
        let m = 0;
        let s = 1;
        while (rutDigits > 0) {
            s = (s + (rutDigits % 10) * (9 - m++ % 6)) % 11;
            rutDigits = Math.floor(rutDigits / 10);
        }
        const checkDigit = s > 0 ? String(s - 1) : 'K';
        return checkDigit === rut.slice(-1);
    }

    public rutFormat(value): string {
        const rut = this.rutClean(value);
        if (rut.length <= 1) {
            return rut;
        }
        let result = rut.slice(-4, -1) + '-' + rut.substr(rut.length - 1);
        for (let i = 4; i < rut.length; i += 3) {
            result = rut.slice(-3 - i, -i) + '.' + result;
        }
        return result;
    }

    /**
     * Filtro que verifica la validez de formato de un rut dado
     * @param rut
     */
    public checkFormatFilter(rut) {
        const regexFormatosValidos = /^(\d{7,9}\-(\d|k))$|^(\d{1,3}\.\d{3}\.\d{3}\-(\d|k))$|^(\d{7,9})(\d|k)$/i;
        return typeof rut === 'string' && regexFormatosValidos.test(rut);
    }

    /**
     * Filtro que formatea el rut a un formato válido
     * @param rut
     */
    public rutFormatter(rut) {
        if (!rut) {
            return '';
        }
        rut = rut.match(/[0-9Kk]+/g).join('');
        return rut.slice(0, -1).replace(/[0-9](?=(?:[0-9]{3})+(?![0-9]))/g, '$&.') + '-' + rut.slice(-1).toLowerCase();
    }

    public cleanFormatFilter(rut) {
        return rut ? rut.match(/[0-9Kk]+/g).join('') : '';
    }

    /**
     * Filtro que verifica el digito verificador de un rut dado
     * @param rut
     */
    public rutVerifierFilter(rut): boolean {
        // Quitamos espacios en blanco en caso de traer
        rut = rut.trim();
        // Verificamos si el rut dado tiene un formato válido
        if (!this.checkFormatFilter(rut)) {
            return false;
        }
        // Extraemos solo lo que necesitamos del rut, los números y el digito verificador
        rut = this.cleanFormatFilter(rut);

        /*
          Definimos una variable que irá aumentando en cada vuelta del ciclo
          representando la serie 2,3,4,5,6,7. En caso que este factor llegue
          al número final de esta serie, se reinicia a 2.
        */
        let factor = 2;
        /*
          La variable suma irá guardando la suma parcial de la multiplicación entre el factor y el digito
          del rut correspondiente
        */
        let suma = 0;
        /*
          Ya que nos interesa sólo calcular lo que está a la izquierda del dígito verificador,
        nuestro iterador partirá desde el digito anterior al digito verificador en reversa (link del cálculo del rut).
        */
        let l = rut.length - 1;
        /*
          La función slice (link acá -> https://goo.gl/7KgPVV) permite utilizar como argumento un número negativo.
          Al hacer esto definimos cuantos caracteres desde el final del array queremos abarcar. En nuestro
          caso queremos solo abarcar el último dígito. En caso se resultar ser una 'K', no aseguramos que siempre sea minúscula
        */
        const digitoOriginal = rut.slice(-1).toLowerCase();
        // variable a la cuál asignaremos el dígito final calculado
        let digitoCalculado;

        /*En javascript los operadores se evalúan después de las aserciones como while o if
         considerando que queremos recorrer hasta el dígito 0 y que este es considerado un falsy value (revisar este link-> https://goo.gl/uX5yf4)
         utilizamos el siguiente ciclo while
        */
        while (l--) {
            suma += +rut[l] * factor;
            factor = factor === 7 ? 2 : ++factor;
        }
        // si 11 menos el modulo de la suma obtenida es 11, digito verificador es 0, si
        digitoCalculado = 11 - suma % 11;
        digitoCalculado = digitoCalculado === 11 ? 0 : digitoCalculado === 10 ? 'k' : digitoCalculado;
        // convertimos en string el digito calculado y retornamos el resultado de la comparación con el digito verificador
        return '' + digitoCalculado === digitoOriginal;
    }

    /**
     * Filtro que verifica el digito verificador de un rut dado
     * @param rut
     */
    public getDigVerifOther(rut): string {
        // Quitamos espacios en blanco en caso de traer
        rut = rut.trim();
        // Verificamos si el rut dado tiene un formato válido
        /*if (!this.checkFormatFilter(rut)) {
            return false;
        }*/
        // Extraemos solo lo que necesitamos del rut, los números y el digito verificador
        rut = this.cleanFormatFilter(rut);

        /*
          Definimos una variable que irá aumentando en cada vuelta del ciclo
          representando la serie 2,3,4,5,6,7. En caso que este factor llegue
          al número final de esta serie, se reinicia a 2.
        */
        let factor = 2;
        /*
          La variable suma irá guardando la suma parcial de la multiplicación entre el factor y el digito
          del rut correspondiente
        */
        let suma = 0;
        /*
          Ya que nos interesa sólo calcular lo que está a la izquierda del dígito verificador,
        nuestro iterador partirá desde el digito anterior al digito verificador en reversa (link del cálculo del rut).
        */
        let l = rut.length - 1;
        /*
          La función slice (link acá -> https://goo.gl/7KgPVV) permite utilizar como argumento un número negativo.
          Al hacer esto definimos cuantos caracteres desde el final del array queremos abarcar. En nuestro
          caso queremos solo abarcar el último dígito. En caso se resultar ser una 'K', no aseguramos que siempre sea minúscula
        */
        const digitoOriginal = rut.slice(-1).toLowerCase();
        // variable a la cuál asignaremos el dígito final calculado
        let digitoCalculado;

        /*En javascript los operadores se evalúan después de las aserciones como while o if
         considerando que queremos recorrer hasta el dígito 0 y que este es considerado un falsy value (revisar este link-> https://goo.gl/uX5yf4)
         utilizamos el siguiente ciclo while
        */
        while (l--) {
            suma += +rut[l] * factor;
            factor = factor === 7 ? 2 : ++factor;
        }
        // si 11 menos el modulo de la suma obtenida es 11, digito verificador es 0, si
        digitoCalculado = 11 - suma % 11;
        digitoCalculado = digitoCalculado === 11 ? 0 : digitoCalculado === 10 ? 'k' : digitoCalculado;
        // convertimos en string el digito calculado y retornamos
        return '' + digitoCalculado;
    }
}
