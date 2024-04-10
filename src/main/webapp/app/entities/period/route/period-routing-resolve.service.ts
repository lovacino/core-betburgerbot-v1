import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeriod } from '../period.model';
import { PeriodService } from '../service/period.service';

export const periodResolve = (route: ActivatedRouteSnapshot): Observable<null | IPeriod> => {
  const id = route.params['id'];
  if (id) {
    return inject(PeriodService)
      .find(id)
      .pipe(
        mergeMap((period: HttpResponse<IPeriod>) => {
          if (period.body) {
            return of(period.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default periodResolve;
