import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBetting } from '../betting.model';
import { BettingService } from '../service/betting.service';

export const bettingResolve = (route: ActivatedRouteSnapshot): Observable<null | IBetting> => {
  const id = route.params['id'];
  if (id) {
    return inject(BettingService)
      .find(id)
      .pipe(
        mergeMap((betting: HttpResponse<IBetting>) => {
          if (betting.body) {
            return of(betting.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default bettingResolve;
