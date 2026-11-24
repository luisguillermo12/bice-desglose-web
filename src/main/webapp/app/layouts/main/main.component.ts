import { ChangeDetectorRef, Component, OnInit, TemplateRef } from '@angular/core';
import { ActivatedRouteSnapshot, NavigationEnd, Router } from '@angular/router';

import { JhiLanguageHelper } from 'app/core';
import { ngxLoadingAnimationTypes } from 'ngx-loading';
import { LoaderService } from 'app/core/load/loader.service';
import { PRIMARY_WHITE, SECONDARY_GREY } from 'app/app.constants';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html',
    styleUrls: ['main.component.scss']
})
export class JhiMainComponent implements OnInit {

    public primaryColour = PRIMARY_WHITE;
    public secondaryColour = SECONDARY_GREY;
    public loadingTemplate: TemplateRef<any>;
    public config = {
        animationType: ngxLoadingAnimationTypes.none, primaryColour: this.primaryColour,
        secondaryColour: this.secondaryColour, tertiaryColour: this.primaryColour, backdropBorderRadius: '3px'
    };

    constructor(private jhiLanguageHelper: JhiLanguageHelper, private router: Router, public loaderService: LoaderService, private changeDetectorRef: ChangeDetectorRef) {
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data[ 'pageTitle' ] ? routeSnapshot.data[ 'pageTitle' ] : 'desgloseApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
    }
}
