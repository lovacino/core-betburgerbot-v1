import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BettingComponent } from './list/betting.component';
import { BettingDetailComponent } from './detail/betting-detail.component';
import { BettingUpdateComponent } from './update/betting-update.component';
import BettingResolve from './route/betting-routing-resolve.service';

const bettingRoute: Routes = [
  {
    path: '',
    component: BettingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BettingDetailComponent,
    resolve: {
      betting: BettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BettingUpdateComponent,
    resolve: {
      betting: BettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BettingUpdateComponent,
    resolve: {
      betting: BettingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bettingRoute;
