import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccountBet, NewAccountBet } from '../account-bet.model';

export type PartialUpdateAccountBet = Partial<IAccountBet> & Pick<IAccountBet, 'id'>;

export type EntityResponseType = HttpResponse<IAccountBet>;
export type EntityArrayResponseType = HttpResponse<IAccountBet[]>;

@Injectable({ providedIn: 'root' })
export class AccountBetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-bets');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(accountBet: NewAccountBet): Observable<EntityResponseType> {
    return this.http.post<IAccountBet>(this.resourceUrl, accountBet, { observe: 'response' });
  }

  update(accountBet: IAccountBet): Observable<EntityResponseType> {
    return this.http.put<IAccountBet>(`${this.resourceUrl}/${this.getAccountBetIdentifier(accountBet)}`, accountBet, {
      observe: 'response',
    });
  }

  partialUpdate(accountBet: PartialUpdateAccountBet): Observable<EntityResponseType> {
    return this.http.patch<IAccountBet>(`${this.resourceUrl}/${this.getAccountBetIdentifier(accountBet)}`, accountBet, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountBet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountBet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccountBetIdentifier(accountBet: Pick<IAccountBet, 'id'>): number {
    return accountBet.id;
  }

  compareAccountBet(o1: Pick<IAccountBet, 'id'> | null, o2: Pick<IAccountBet, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountBetIdentifier(o1) === this.getAccountBetIdentifier(o2) : o1 === o2;
  }

  addAccountBetToCollectionIfMissing<Type extends Pick<IAccountBet, 'id'>>(
    accountBetCollection: Type[],
    ...accountBetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountBets: Type[] = accountBetsToCheck.filter(isPresent);
    if (accountBets.length > 0) {
      const accountBetCollectionIdentifiers = accountBetCollection.map(accountBetItem => this.getAccountBetIdentifier(accountBetItem)!);
      const accountBetsToAdd = accountBets.filter(accountBetItem => {
        const accountBetIdentifier = this.getAccountBetIdentifier(accountBetItem);
        if (accountBetCollectionIdentifiers.includes(accountBetIdentifier)) {
          return false;
        }
        accountBetCollectionIdentifiers.push(accountBetIdentifier);
        return true;
      });
      return [...accountBetsToAdd, ...accountBetCollection];
    }
    return accountBetCollection;
  }
}
