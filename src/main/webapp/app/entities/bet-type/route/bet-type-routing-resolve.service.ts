import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBetType } from '../bet-type.model';
import { BetTypeService } from '../service/bet-type.service';

export const betTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IBetType> => {
  const id = route.params['id'];
  if (id) {
    return inject(BetTypeService)
      .find(id)
      .pipe(
        mergeMap((betType: HttpResponse<IBetType>) => {
          if (betType.body) {
            return of(betType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default betTypeResolve;
