import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBetting, NewBetting } from '../betting.model';

export type PartialUpdateBetting = Partial<IBetting> & Pick<IBetting, 'id'>;

type RestOf<T extends IBetting | NewBetting> = Omit<T, 'startedAt' | 'updatedAt'> & {
  startedAt?: string | null;
  updatedAt?: string | null;
};

export type RestBetting = RestOf<IBetting>;

export type NewRestBetting = RestOf<NewBetting>;

export type PartialUpdateRestBetting = RestOf<PartialUpdateBetting>;

export type EntityResponseType = HttpResponse<IBetting>;
export type EntityArrayResponseType = HttpResponse<IBetting[]>;

@Injectable({ providedIn: 'root' })
export class BettingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bettings');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(betting: NewBetting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(betting);
    return this.http
      .post<RestBetting>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(betting: IBetting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(betting);
    return this.http
      .put<RestBetting>(`${this.resourceUrl}/${this.getBettingIdentifier(betting)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(betting: PartialUpdateBetting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(betting);
    return this.http
      .patch<RestBetting>(`${this.resourceUrl}/${this.getBettingIdentifier(betting)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBetting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBetting[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBettingIdentifier(betting: Pick<IBetting, 'id'>): number {
    return betting.id;
  }

  compareBetting(o1: Pick<IBetting, 'id'> | null, o2: Pick<IBetting, 'id'> | null): boolean {
    return o1 && o2 ? this.getBettingIdentifier(o1) === this.getBettingIdentifier(o2) : o1 === o2;
  }

  addBettingToCollectionIfMissing<Type extends Pick<IBetting, 'id'>>(
    bettingCollection: Type[],
    ...bettingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bettings: Type[] = bettingsToCheck.filter(isPresent);
    if (bettings.length > 0) {
      const bettingCollectionIdentifiers = bettingCollection.map(bettingItem => this.getBettingIdentifier(bettingItem)!);
      const bettingsToAdd = bettings.filter(bettingItem => {
        const bettingIdentifier = this.getBettingIdentifier(bettingItem);
        if (bettingCollectionIdentifiers.includes(bettingIdentifier)) {
          return false;
        }
        bettingCollectionIdentifiers.push(bettingIdentifier);
        return true;
      });
      return [...bettingsToAdd, ...bettingCollection];
    }
    return bettingCollection;
  }

  protected convertDateFromClient<T extends IBetting | NewBetting | PartialUpdateBetting>(betting: T): RestOf<T> {
    return {
      ...betting,
      startedAt: betting.startedAt?.toJSON() ?? null,
      updatedAt: betting.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBetting: RestBetting): IBetting {
    return {
      ...restBetting,
      startedAt: restBetting.startedAt ? dayjs(restBetting.startedAt) : undefined,
      updatedAt: restBetting.updatedAt ? dayjs(restBetting.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBetting>): HttpResponse<IBetting> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBetting[]>): HttpResponse<IBetting[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
