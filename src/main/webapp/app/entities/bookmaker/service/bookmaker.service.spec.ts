import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBookmaker } from '../bookmaker.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bookmaker.test-samples';

import { BookmakerService } from './bookmaker.service';

const requireRestSample: IBookmaker = {
  ...sampleWithRequiredData,
};

describe('Bookmaker Service', () => {
  let service: BookmakerService;
  let httpMock: HttpTestingController;
  let expectedResult: IBookmaker | IBookmaker[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BookmakerService);
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

    it('should create a Bookmaker', () => {
      const bookmaker = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bookmaker).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bookmaker', () => {
      const bookmaker = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bookmaker).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bookmaker', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bookmaker', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bookmaker', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookmakerToCollectionIfMissing', () => {
      it('should add a Bookmaker to an empty array', () => {
        const bookmaker: IBookmaker = sampleWithRequiredData;
        expectedResult = service.addBookmakerToCollectionIfMissing([], bookmaker);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookmaker);
      });

      it('should not add a Bookmaker to an array that contains it', () => {
        const bookmaker: IBookmaker = sampleWithRequiredData;
        const bookmakerCollection: IBookmaker[] = [
          {
            ...bookmaker,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookmakerToCollectionIfMissing(bookmakerCollection, bookmaker);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bookmaker to an array that doesn't contain it", () => {
        const bookmaker: IBookmaker = sampleWithRequiredData;
        const bookmakerCollection: IBookmaker[] = [sampleWithPartialData];
        expectedResult = service.addBookmakerToCollectionIfMissing(bookmakerCollection, bookmaker);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookmaker);
      });

      it('should add only unique Bookmaker to an array', () => {
        const bookmakerArray: IBookmaker[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookmakerCollection: IBookmaker[] = [sampleWithRequiredData];
        expectedResult = service.addBookmakerToCollectionIfMissing(bookmakerCollection, ...bookmakerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bookmaker: IBookmaker = sampleWithRequiredData;
        const bookmaker2: IBookmaker = sampleWithPartialData;
        expectedResult = service.addBookmakerToCollectionIfMissing([], bookmaker, bookmaker2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookmaker);
        expect(expectedResult).toContain(bookmaker2);
      });

      it('should accept null and undefined values', () => {
        const bookmaker: IBookmaker = sampleWithRequiredData;
        expectedResult = service.addBookmakerToCollectionIfMissing([], null, bookmaker, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookmaker);
      });

      it('should return initial array if no Bookmaker is added', () => {
        const bookmakerCollection: IBookmaker[] = [sampleWithRequiredData];
        expectedResult = service.addBookmakerToCollectionIfMissing(bookmakerCollection, undefined, null);
        expect(expectedResult).toEqual(bookmakerCollection);
      });
    });

    describe('compareBookmaker', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBookmaker(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBookmaker(entity1, entity2);
        const compareResult2 = service.compareBookmaker(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBookmaker(entity1, entity2);
        const compareResult2 = service.compareBookmaker(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBookmaker(entity1, entity2);
        const compareResult2 = service.compareBookmaker(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
