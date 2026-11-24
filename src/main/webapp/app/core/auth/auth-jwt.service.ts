import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

import {
    SERVER_API_URL,
    SESSION_REEMPLAZO_AUTENTICATE,
    SESSION_REEMPLAZO_MENSAJE,
    SESSION_REEMPLAZO_PARAMS, SESSION_VARIABLE_CLOSE,
    SESSION_VARIABLE_EN_DESGLOSE,
    SESSION_VARIABLE_USUARIO, SESSION_VARIABLE_USUARIO_NAME
} from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class AuthServerProvider {
    constructor(private http: HttpClient, private $localStorage: LocalStorageService, private $sessionStorage: SessionStorageService) {
    }

    getToken() {
        return this.$localStorage.retrieve('authenticationToken') || this.$sessionStorage.retrieve('authenticationToken');
    }

    login(credentials): Observable<any> {
        const data = {
            username: credentials.username,
            password: credentials.password,
            rememberMe: credentials.rememberMe
        };
        return this.http.post(SERVER_API_URL + 'api/authenticate', data, { observe: 'response' }).pipe(map(authenticateSuccess.bind(this)));

        function authenticateSuccess(resp) {
            const bearerToken = resp.headers.get('Authorization');
            if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
                const jwt = bearerToken.slice(7, bearerToken.length);
                this.storeAuthenticationToken(jwt, credentials.rememberMe);
                return jwt;
            }
        }
    }

    loginWithToken(jwt, rememberMe) {
        if (jwt) {
            this.storeAuthenticationToken(jwt, rememberMe);
            return Promise.resolve(jwt);
        } else {
            return Promise.reject('auth-jwt-service Promise reject'); // Put appropriate error message here
        }
    }

    storeAuthenticationToken(jwt, rememberMe) {
        if (rememberMe) {
            this.$localStorage.store('authenticationToken', jwt);
        } else {
            this.$sessionStorage.store('authenticationToken', jwt);
        }
    }

    logout(): Observable<any> {
        return new Observable(observer => {
            this.$localStorage.clear('authenticationToken');
            this.$sessionStorage.clear('authenticationToken');
            this.$sessionStorage.clear(SESSION_VARIABLE_USUARIO);
            this.$localStorage.clear(SESSION_VARIABLE_USUARIO);
            this.$sessionStorage.clear(SESSION_VARIABLE_USUARIO_NAME);
            this.$localStorage.clear(SESSION_VARIABLE_USUARIO_NAME);
            this.$sessionStorage.clear(SESSION_VARIABLE_EN_DESGLOSE);
            this.$localStorage.clear(SESSION_VARIABLE_EN_DESGLOSE);
            this.$sessionStorage.clear(SESSION_VARIABLE_CLOSE);
            this.$localStorage.clear(SESSION_VARIABLE_CLOSE);
            this.$sessionStorage.clear(SESSION_REEMPLAZO_AUTENTICATE);
            this.$localStorage.clear(SESSION_REEMPLAZO_AUTENTICATE);
            this.$sessionStorage.clear(SESSION_REEMPLAZO_MENSAJE);
            this.$localStorage.clear(SESSION_REEMPLAZO_MENSAJE);
            this.$sessionStorage.clear(SESSION_REEMPLAZO_PARAMS);
            this.$localStorage.clear(SESSION_REEMPLAZO_PARAMS);
            observer.complete();
        });
    }
}
