import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventSource } from '../event-source.model';
import { EventSourceService } from '../service/event-source.service';

export const eventSourceResolve = (route: ActivatedRouteSnapshot): Observable<null | IEventSource> => {
  const id = route.params['id'];
  if (id) {
    return inject(EventSourceService)
      .find(id)
      .pipe(
        mergeMap((eventSource: HttpResponse<IEventSource>) => {
          if (eventSource.body) {
            return of(eventSource.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default eventSourceResolve;
