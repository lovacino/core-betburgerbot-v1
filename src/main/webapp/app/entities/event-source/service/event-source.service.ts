import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventSource, NewEventSource } from '../event-source.model';

export type PartialUpdateEventSource = Partial<IEventSource> & Pick<IEventSource, 'id'>;

type RestOf<T extends IEventSource | NewEventSource> = Omit<T, 'koefLastModifiedAt' | 'scannedAt' | 'startedAt' | 'updatedAt'> & {
  koefLastModifiedAt?: string | null;
  scannedAt?: string | null;
  startedAt?: string | null;
  updatedAt?: string | null;
};

export type RestEventSource = RestOf<IEventSource>;

export type NewRestEventSource = RestOf<NewEventSource>;

export type PartialUpdateRestEventSource = RestOf<PartialUpdateEventSource>;

export type EntityResponseType = HttpResponse<IEventSource>;
export type EntityArrayResponseType = HttpResponse<IEventSource[]>;

@Injectable({ providedIn: 'root' })
export class EventSourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-sources');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(eventSource: NewEventSource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventSource);
    return this.http
      .post<RestEventSource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(eventSource: IEventSource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventSource);
    return this.http
      .put<RestEventSource>(`${this.resourceUrl}/${this.getEventSourceIdentifier(eventSource)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(eventSource: PartialUpdateEventSource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventSource);
    return this.http
      .patch<RestEventSource>(`${this.resourceUrl}/${this.getEventSourceIdentifier(eventSource)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEventSource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEventSource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEventSourceIdentifier(eventSource: Pick<IEventSource, 'id'>): number {
    return eventSource.id;
  }

  compareEventSource(o1: Pick<IEventSource, 'id'> | null, o2: Pick<IEventSource, 'id'> | null): boolean {
    return o1 && o2 ? this.getEventSourceIdentifier(o1) === this.getEventSourceIdentifier(o2) : o1 === o2;
  }

  addEventSourceToCollectionIfMissing<Type extends Pick<IEventSource, 'id'>>(
    eventSourceCollection: Type[],
    ...eventSourcesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eventSources: Type[] = eventSourcesToCheck.filter(isPresent);
    if (eventSources.length > 0) {
      const eventSourceCollectionIdentifiers = eventSourceCollection.map(
        eventSourceItem => this.getEventSourceIdentifier(eventSourceItem)!,
      );
      const eventSourcesToAdd = eventSources.filter(eventSourceItem => {
        const eventSourceIdentifier = this.getEventSourceIdentifier(eventSourceItem);
        if (eventSourceCollectionIdentifiers.includes(eventSourceIdentifier)) {
          return false;
        }
        eventSourceCollectionIdentifiers.push(eventSourceIdentifier);
        return true;
      });
      return [...eventSourcesToAdd, ...eventSourceCollection];
    }
    return eventSourceCollection;
  }

  protected convertDateFromClient<T extends IEventSource | NewEventSource | PartialUpdateEventSource>(eventSource: T): RestOf<T> {
    return {
      ...eventSource,
      koefLastModifiedAt: eventSource.koefLastModifiedAt?.toJSON() ?? null,
      scannedAt: eventSource.scannedAt?.toJSON() ?? null,
      startedAt: eventSource.startedAt?.toJSON() ?? null,
      updatedAt: eventSource.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEventSource: RestEventSource): IEventSource {
    return {
      ...restEventSource,
      koefLastModifiedAt: restEventSource.koefLastModifiedAt ? dayjs(restEventSource.koefLastModifiedAt) : undefined,
      scannedAt: restEventSource.scannedAt ? dayjs(restEventSource.scannedAt) : undefined,
      startedAt: restEventSource.startedAt ? dayjs(restEventSource.startedAt) : undefined,
      updatedAt: restEventSource.updatedAt ? dayjs(restEventSource.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEventSource>): HttpResponse<IEventSource> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEventSource[]>): HttpResponse<IEventSource[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
