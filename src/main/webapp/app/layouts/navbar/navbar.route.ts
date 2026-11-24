import { Route } from '@angular/router';

import { NavbarComponent } from './navbar.component';
import { CloseSessionGuard } from 'app/shared/guards/close-session-guard';

export const navBarRoute: Route = {
    path: '',
    component: NavbarComponent,
    outlet: 'navbar',
    canDeactivate: [ CloseSessionGuard ]
};
