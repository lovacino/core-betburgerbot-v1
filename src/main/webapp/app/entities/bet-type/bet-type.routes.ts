import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BetTypeComponent } from './list/bet-type.component';
import { BetTypeDetailComponent } from './detail/bet-type-detail.component';
import { BetTypeUpdateComponent } from './update/bet-type-update.component';
import BetTypeResolve from './route/bet-type-routing-resolve.service';

const betTypeRoute: Routes = [
  {
    path: '',
    component: BetTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BetTypeDetailComponent,
    resolve: {
      betType: BetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BetTypeUpdateComponent,
    resolve: {
      betType: BetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BetTypeUpdateComponent,
    resolve: {
      betType: BetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default betTypeRoute;
