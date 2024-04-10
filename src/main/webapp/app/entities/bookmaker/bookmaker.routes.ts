import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BookmakerComponent } from './list/bookmaker.component';
import { BookmakerDetailComponent } from './detail/bookmaker-detail.component';
import { BookmakerUpdateComponent } from './update/bookmaker-update.component';
import BookmakerResolve from './route/bookmaker-routing-resolve.service';

const bookmakerRoute: Routes = [
  {
    path: '',
    component: BookmakerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BookmakerDetailComponent,
    resolve: {
      bookmaker: BookmakerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BookmakerUpdateComponent,
    resolve: {
      bookmaker: BookmakerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BookmakerUpdateComponent,
    resolve: {
      bookmaker: BookmakerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bookmakerRoute;
