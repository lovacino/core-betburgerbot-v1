import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountBet } from '../account-bet.model';
import { AccountBetService } from '../service/account-bet.service';

export const accountBetResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccountBet> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccountBetService)
      .find(id)
      .pipe(
        mergeMap((accountBet: HttpResponse<IAccountBet>) => {
          if (accountBet.body) {
            return of(accountBet.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accountBetResolve;
