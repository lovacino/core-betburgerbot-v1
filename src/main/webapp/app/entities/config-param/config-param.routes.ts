import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConfigParamComponent } from './list/config-param.component';
import { ConfigParamDetailComponent } from './detail/config-param-detail.component';
import { ConfigParamUpdateComponent } from './update/config-param-update.component';
import ConfigParamResolve from './route/config-param-routing-resolve.service';

const configParamRoute: Routes = [
  {
    path: '',
    component: ConfigParamComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfigParamDetailComponent,
    resolve: {
      configParam: ConfigParamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfigParamUpdateComponent,
    resolve: {
      configParam: ConfigParamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConfigParamUpdateComponent,
    resolve: {
      configParam: ConfigParamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default configParamRoute;
