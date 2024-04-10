import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConfigParam } from '../config-param.model';
import { ConfigParamService } from '../service/config-param.service';

export const configParamResolve = (route: ActivatedRouteSnapshot): Observable<null | IConfigParam> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConfigParamService)
      .find(id)
      .pipe(
        mergeMap((configParam: HttpResponse<IConfigParam>) => {
          if (configParam.body) {
            return of(configParam.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default configParamResolve;
