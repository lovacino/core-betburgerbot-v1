import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccountBetComponent } from './list/account-bet.component';
import { AccountBetDetailComponent } from './detail/account-bet-detail.component';
import { AccountBetUpdateComponent } from './update/account-bet-update.component';
import AccountBetResolve from './route/account-bet-routing-resolve.service';

const accountBetRoute: Routes = [
  {
    path: '',
    component: AccountBetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountBetDetailComponent,
    resolve: {
      accountBet: AccountBetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountBetUpdateComponent,
    resolve: {
      accountBet: AccountBetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountBetUpdateComponent,
    resolve: {
      accountBet: AccountBetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountBetRoute;
