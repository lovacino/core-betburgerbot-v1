import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEventSource } from '../event-source.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../event-source.test-samples';

import { EventSourceService, RestEventSource } from './event-source.service';

const requireRestSample: RestEventSource = {
  ...sampleWithRequiredData,
  koefLastModifiedAt: sampleWithRequiredData.koefLastModifiedAt?.toJSON(),
  scannedAt: sampleWithRequiredData.scannedAt?.toJSON(),
  startedAt: sampleWithRequiredData.startedAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('EventSource Service', () => {
  let service: EventSourceService;
  let httpMock: HttpTestingController;
  let expectedResult: IEventSource | IEventSource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventSourceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EventSource', () => {
      const eventSource = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eventSource).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EventSource', () => {
      const eventSource = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eventSource).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EventSource', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EventSource', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EventSource', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEventSourceToCollectionIfMissing', () => {
      it('should add a EventSource to an empty array', () => {
        const eventSource: IEventSource = sampleWithRequiredData;
        expectedResult = service.addEventSourceToCollectionIfMissing([], eventSource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventSource);
      });

      it('should not add a EventSource to an array that contains it', () => {
        const eventSource: IEventSource = sampleWithRequiredData;
        const eventSourceCollection: IEventSource[] = [
          {
            ...eventSource,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEventSourceToCollectionIfMissing(eventSourceCollection, eventSource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EventSource to an array that doesn't contain it", () => {
        const eventSource: IEventSource = sampleWithRequiredData;
        const eventSourceCollection: IEventSource[] = [sampleWithPartialData];
        expectedResult = service.addEventSourceToCollectionIfMissing(eventSourceCollection, eventSource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventSource);
      });

      it('should add only unique EventSource to an array', () => {
        const eventSourceArray: IEventSource[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eventSourceCollection: IEventSource[] = [sampleWithRequiredData];
        expectedResult = service.addEventSourceToCollectionIfMissing(eventSourceCollection, ...eventSourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eventSource: IEventSource = sampleWithRequiredData;
        const eventSource2: IEventSource = sampleWithPartialData;
        expectedResult = service.addEventSourceToCollectionIfMissing([], eventSource, eventSource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventSource);
        expect(expectedResult).toContain(eventSource2);
      });

      it('should accept null and undefined values', () => {
        const eventSource: IEventSource = sampleWithRequiredData;
        expectedResult = service.addEventSourceToCollectionIfMissing([], null, eventSource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventSource);
      });

      it('should return initial array if no EventSource is added', () => {
        const eventSourceCollection: IEventSource[] = [sampleWithRequiredData];
        expectedResult = service.addEventSourceToCollectionIfMissing(eventSourceCollection, undefined, null);
        expect(expectedResult).toEqual(eventSourceCollection);
      });
    });

    describe('compareEventSource', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEventSource(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEventSource(entity1, entity2);
        const compareResult2 = service.compareEventSource(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEventSource(entity1, entity2);
        const compareResult2 = service.compareEventSource(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEventSource(entity1, entity2);
        const compareResult2 = service.compareEventSource(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
