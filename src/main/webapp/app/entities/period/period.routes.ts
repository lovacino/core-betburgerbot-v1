import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PeriodComponent } from './list/period.component';
import { PeriodDetailComponent } from './detail/period-detail.component';
import { PeriodUpdateComponent } from './update/period-update.component';
import PeriodResolve from './route/period-routing-resolve.service';

const periodRoute: Routes = [
  {
    path: '',
    component: PeriodComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeriodDetailComponent,
    resolve: {
      period: PeriodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeriodUpdateComponent,
    resolve: {
      period: PeriodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeriodUpdateComponent,
    resolve: {
      period: PeriodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default periodRoute;
