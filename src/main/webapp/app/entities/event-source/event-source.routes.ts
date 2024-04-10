import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EventSourceComponent } from './list/event-source.component';
import { EventSourceDetailComponent } from './detail/event-source-detail.component';
import { EventSourceUpdateComponent } from './update/event-source-update.component';
import EventSourceResolve from './route/event-source-routing-resolve.service';

const eventSourceRoute: Routes = [
  {
    path: '',
    component: EventSourceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventSourceDetailComponent,
    resolve: {
      eventSource: EventSourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventSourceUpdateComponent,
    resolve: {
      eventSource: EventSourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventSourceUpdateComponent,
    resolve: {
      eventSource: EventSourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default eventSourceRoute;
