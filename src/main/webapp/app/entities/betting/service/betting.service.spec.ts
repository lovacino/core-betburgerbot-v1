import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBetting } from '../betting.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../betting.test-samples';

import { BettingService, RestBetting } from './betting.service';

const requireRestSample: RestBetting = {
  ...sampleWithRequiredData,
  startedAt: sampleWithRequiredData.startedAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Betting Service', () => {
  let service: BettingService;
  let httpMock: HttpTestingController;
  let expectedResult: IBetting | IBetting[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BettingService);
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

    it('should create a Betting', () => {
      const betting = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(betting).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Betting', () => {
      const betting = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(betting).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Betting', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Betting', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Betting', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBettingToCollectionIfMissing', () => {
      it('should add a Betting to an empty array', () => {
        const betting: IBetting = sampleWithRequiredData;
        expectedResult = service.addBettingToCollectionIfMissing([], betting);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(betting);
      });

      it('should not add a Betting to an array that contains it', () => {
        const betting: IBetting = sampleWithRequiredData;
        const bettingCollection: IBetting[] = [
          {
            ...betting,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBettingToCollectionIfMissing(bettingCollection, betting);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Betting to an array that doesn't contain it", () => {
        const betting: IBetting = sampleWithRequiredData;
        const bettingCollection: IBetting[] = [sampleWithPartialData];
        expectedResult = service.addBettingToCollectionIfMissing(bettingCollection, betting);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(betting);
      });

      it('should add only unique Betting to an array', () => {
        const bettingArray: IBetting[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bettingCollection: IBetting[] = [sampleWithRequiredData];
        expectedResult = service.addBettingToCollectionIfMissing(bettingCollection, ...bettingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const betting: IBetting = sampleWithRequiredData;
        const betting2: IBetting = sampleWithPartialData;
        expectedResult = service.addBettingToCollectionIfMissing([], betting, betting2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(betting);
        expect(expectedResult).toContain(betting2);
      });

      it('should accept null and undefined values', () => {
        const betting: IBetting = sampleWithRequiredData;
        expectedResult = service.addBettingToCollectionIfMissing([], null, betting, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(betting);
      });

      it('should return initial array if no Betting is added', () => {
        const bettingCollection: IBetting[] = [sampleWithRequiredData];
        expectedResult = service.addBettingToCollectionIfMissing(bettingCollection, undefined, null);
        expect(expectedResult).toEqual(bettingCollection);
      });
    });

    describe('compareBetting', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBetting(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBetting(entity1, entity2);
        const compareResult2 = service.compareBetting(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBetting(entity1, entity2);
        const compareResult2 = service.compareBetting(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBetting(entity1, entity2);
        const compareResult2 = service.compareBetting(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
