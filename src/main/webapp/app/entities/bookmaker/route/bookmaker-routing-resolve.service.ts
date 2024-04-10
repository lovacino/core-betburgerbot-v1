import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBookmaker } from '../bookmaker.model';
import { BookmakerService } from '../service/bookmaker.service';

export const bookmakerResolve = (route: ActivatedRouteSnapshot): Observable<null | IBookmaker> => {
  const id = route.params['id'];
  if (id) {
    return inject(BookmakerService)
      .find(id)
      .pipe(
        mergeMap((bookmaker: HttpResponse<IBookmaker>) => {
          if (bookmaker.body) {
            return of(bookmaker.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default bookmakerResolve;
