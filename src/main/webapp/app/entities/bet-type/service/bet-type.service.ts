import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBetType, NewBetType } from '../bet-type.model';

export type PartialUpdateBetType = Partial<IBetType> & Pick<IBetType, 'id'>;

export type EntityResponseType = HttpResponse<IBetType>;
export type EntityArrayResponseType = HttpResponse<IBetType[]>;

@Injectable({ providedIn: 'root' })
export class BetTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bet-types');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(betType: NewBetType): Observable<EntityResponseType> {
    return this.http.post<IBetType>(this.resourceUrl, betType, { observe: 'response' });
  }

  update(betType: IBetType): Observable<EntityResponseType> {
    return this.http.put<IBetType>(`${this.resourceUrl}/${this.getBetTypeIdentifier(betType)}`, betType, { observe: 'response' });
  }

  partialUpdate(betType: PartialUpdateBetType): Observable<EntityResponseType> {
    return this.http.patch<IBetType>(`${this.resourceUrl}/${this.getBetTypeIdentifier(betType)}`, betType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBetType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBetType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBetTypeIdentifier(betType: Pick<IBetType, 'id'>): number {
    return betType.id;
  }

  compareBetType(o1: Pick<IBetType, 'id'> | null, o2: Pick<IBetType, 'id'> | null): boolean {
    return o1 && o2 ? this.getBetTypeIdentifier(o1) === this.getBetTypeIdentifier(o2) : o1 === o2;
  }

  addBetTypeToCollectionIfMissing<Type extends Pick<IBetType, 'id'>>(
    betTypeCollection: Type[],
    ...betTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const betTypes: Type[] = betTypesToCheck.filter(isPresent);
    if (betTypes.length > 0) {
      const betTypeCollectionIdentifiers = betTypeCollection.map(betTypeItem => this.getBetTypeIdentifier(betTypeItem)!);
      const betTypesToAdd = betTypes.filter(betTypeItem => {
        const betTypeIdentifier = this.getBetTypeIdentifier(betTypeItem);
        if (betTypeCollectionIdentifiers.includes(betTypeIdentifier)) {
          return false;
        }
        betTypeCollectionIdentifiers.push(betTypeIdentifier);
        return true;
      });
      return [...betTypesToAdd, ...betTypeCollection];
    }
    return betTypeCollection;
  }
}
