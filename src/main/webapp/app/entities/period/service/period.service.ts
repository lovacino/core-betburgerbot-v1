import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeriod, NewPeriod } from '../period.model';

export type PartialUpdatePeriod = Partial<IPeriod> & Pick<IPeriod, 'id'>;

export type EntityResponseType = HttpResponse<IPeriod>;
export type EntityArrayResponseType = HttpResponse<IPeriod[]>;

@Injectable({ providedIn: 'root' })
export class PeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/periods');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(period: NewPeriod): Observable<EntityResponseType> {
    return this.http.post<IPeriod>(this.resourceUrl, period, { observe: 'response' });
  }

  update(period: IPeriod): Observable<EntityResponseType> {
    return this.http.put<IPeriod>(`${this.resourceUrl}/${this.getPeriodIdentifier(period)}`, period, { observe: 'response' });
  }

  partialUpdate(period: PartialUpdatePeriod): Observable<EntityResponseType> {
    return this.http.patch<IPeriod>(`${this.resourceUrl}/${this.getPeriodIdentifier(period)}`, period, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeriod[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPeriodIdentifier(period: Pick<IPeriod, 'id'>): number {
    return period.id;
  }

  comparePeriod(o1: Pick<IPeriod, 'id'> | null, o2: Pick<IPeriod, 'id'> | null): boolean {
    return o1 && o2 ? this.getPeriodIdentifier(o1) === this.getPeriodIdentifier(o2) : o1 === o2;
  }

  addPeriodToCollectionIfMissing<Type extends Pick<IPeriod, 'id'>>(
    periodCollection: Type[],
    ...periodsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const periods: Type[] = periodsToCheck.filter(isPresent);
    if (periods.length > 0) {
      const periodCollectionIdentifiers = periodCollection.map(periodItem => this.getPeriodIdentifier(periodItem)!);
      const periodsToAdd = periods.filter(periodItem => {
        const periodIdentifier = this.getPeriodIdentifier(periodItem);
        if (periodCollectionIdentifiers.includes(periodIdentifier)) {
          return false;
        }
        periodCollectionIdentifiers.push(periodIdentifier);
        return true;
      });
      return [...periodsToAdd, ...periodCollection];
    }
    return periodCollection;
  }
}
