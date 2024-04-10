import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBookmaker, NewBookmaker } from '../bookmaker.model';

export type PartialUpdateBookmaker = Partial<IBookmaker> & Pick<IBookmaker, 'id'>;

export type EntityResponseType = HttpResponse<IBookmaker>;
export type EntityArrayResponseType = HttpResponse<IBookmaker[]>;

@Injectable({ providedIn: 'root' })
export class BookmakerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bookmakers');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(bookmaker: NewBookmaker): Observable<EntityResponseType> {
    return this.http.post<IBookmaker>(this.resourceUrl, bookmaker, { observe: 'response' });
  }

  update(bookmaker: IBookmaker): Observable<EntityResponseType> {
    return this.http.put<IBookmaker>(`${this.resourceUrl}/${this.getBookmakerIdentifier(bookmaker)}`, bookmaker, { observe: 'response' });
  }

  partialUpdate(bookmaker: PartialUpdateBookmaker): Observable<EntityResponseType> {
    return this.http.patch<IBookmaker>(`${this.resourceUrl}/${this.getBookmakerIdentifier(bookmaker)}`, bookmaker, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBookmaker>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBookmaker[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBookmakerIdentifier(bookmaker: Pick<IBookmaker, 'id'>): number {
    return bookmaker.id;
  }

  compareBookmaker(o1: Pick<IBookmaker, 'id'> | null, o2: Pick<IBookmaker, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookmakerIdentifier(o1) === this.getBookmakerIdentifier(o2) : o1 === o2;
  }

  addBookmakerToCollectionIfMissing<Type extends Pick<IBookmaker, 'id'>>(
    bookmakerCollection: Type[],
    ...bookmakersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookmakers: Type[] = bookmakersToCheck.filter(isPresent);
    if (bookmakers.length > 0) {
      const bookmakerCollectionIdentifiers = bookmakerCollection.map(bookmakerItem => this.getBookmakerIdentifier(bookmakerItem)!);
      const bookmakersToAdd = bookmakers.filter(bookmakerItem => {
        const bookmakerIdentifier = this.getBookmakerIdentifier(bookmakerItem);
        if (bookmakerCollectionIdentifiers.includes(bookmakerIdentifier)) {
          return false;
        }
        bookmakerCollectionIdentifiers.push(bookmakerIdentifier);
        return true;
      });
      return [...bookmakersToAdd, ...bookmakerCollection];
    }
    return bookmakerCollection;
  }
}
