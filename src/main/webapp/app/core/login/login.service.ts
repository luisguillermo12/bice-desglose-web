import { Injectable } from '@angular/core';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';
import { SERVER_API_URL, USER_SESSION_AUTO_LOGOUT } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isNullOrUndefined } from 'util';

@Injectable({ providedIn: 'root' })
export class LoginService {
    private resourceUrlLogout = SERVER_API_URL + 'api/logout';
    private resourceUrlUpdate = SERVER_API_URL + 'api/update-session';

    constructor(private languageService: JhiLanguageService, private principal: Principal, private authServerProvider: AuthServerProvider, private eventManager: JhiEventManager,
                private http: HttpClient) {
    }

    login(credentials, callback?) {
        const cb = callback || function() {
        };

        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe(
                data => {
                    this.principal.identity(true).then(account => {
                        // After the login the language will be changed to
                        // the language selected by the user during his registration
                        if (account !== null) {
                            this.languageService.changeLanguage(account.langKey);
                        }
                        resolve(data);
                    });
                    return cb();
                },
                err => {
                    this.logout();
                    reject(err);
                    return cb(err);
                }
            );
        });
    }

    loginWithToken(jwt, rememberMe) {
        return this.authServerProvider.loginWithToken(jwt, rememberMe);
    }

    serverLogout(): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceUrlLogout, { observe: 'response' });
    }

    serverUpdate(login:string): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceUrlUpdate,login, { observe: 'response' });
    }

    udpdateLogin(login:string) {
        this.serverUpdate(login).subscribe(
            () => {},
            dataError => {
                console.log(dataError);
            }
        );
    }

    logout(noBackendLogout?: boolean) {
        // console.log('invocado el cerrar sesión con noBackendLogout = ' + noBackendLogout);
        const token = this.authServerProvider.getToken();
        if (!isNullOrUndefined(token) && token.trim().length > 0 && USER_SESSION_AUTO_LOGOUT) {
            if (!isNullOrUndefined(noBackendLogout) && noBackendLogout) {
                this.sharedLogout();
            } else {
                this.serverLogout().subscribe(
                    () => {
                        this.sharedLogout();
                    },
                    dataError => {
                        console.log(dataError);
                    }
                );
            }
        } else {
            this.sharedLogout();
        }
    }

    private sharedLogout() {
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
        this.eventManager.broadcast({
            name: 'logoutSuccess',
            content: 'Sending Logout Success'
        });
    }
}
