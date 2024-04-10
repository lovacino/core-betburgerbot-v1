import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConfigParam, NewConfigParam } from '../config-param.model';

export type PartialUpdateConfigParam = Partial<IConfigParam> & Pick<IConfigParam, 'id'>;

export type EntityResponseType = HttpResponse<IConfigParam>;
export type EntityArrayResponseType = HttpResponse<IConfigParam[]>;

@Injectable({ providedIn: 'root' })
export class ConfigParamService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/config-params');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(configParam: NewConfigParam): Observable<EntityResponseType> {
    return this.http.post<IConfigParam>(this.resourceUrl, configParam, { observe: 'response' });
  }

  update(configParam: IConfigParam): Observable<EntityResponseType> {
    return this.http.put<IConfigParam>(`${this.resourceUrl}/${this.getConfigParamIdentifier(configParam)}`, configParam, {
      observe: 'response',
    });
  }

  partialUpdate(configParam: PartialUpdateConfigParam): Observable<EntityResponseType> {
    return this.http.patch<IConfigParam>(`${this.resourceUrl}/${this.getConfigParamIdentifier(configParam)}`, configParam, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfigParam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfigParam[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConfigParamIdentifier(configParam: Pick<IConfigParam, 'id'>): number {
    return configParam.id;
  }

  compareConfigParam(o1: Pick<IConfigParam, 'id'> | null, o2: Pick<IConfigParam, 'id'> | null): boolean {
    return o1 && o2 ? this.getConfigParamIdentifier(o1) === this.getConfigParamIdentifier(o2) : o1 === o2;
  }

  addConfigParamToCollectionIfMissing<Type extends Pick<IConfigParam, 'id'>>(
    configParamCollection: Type[],
    ...configParamsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const configParams: Type[] = configParamsToCheck.filter(isPresent);
    if (configParams.length > 0) {
      const configParamCollectionIdentifiers = configParamCollection.map(
        configParamItem => this.getConfigParamIdentifier(configParamItem)!,
      );
      const configParamsToAdd = configParams.filter(configParamItem => {
        const configParamIdentifier = this.getConfigParamIdentifier(configParamItem);
        if (configParamCollectionIdentifiers.includes(configParamIdentifier)) {
          return false;
        }
        configParamCollectionIdentifiers.push(configParamIdentifier);
        return true;
      });
      return [...configParamsToAdd, ...configParamCollection];
    }
    return configParamCollection;
  }
}
