import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBetType } from '../bet-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bet-type.test-samples';

import { BetTypeService } from './bet-type.service';

const requireRestSample: IBetType = {
  ...sampleWithRequiredData,
};

describe('BetType Service', () => {
  let service: BetTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IBetType | IBetType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BetTypeService);
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

    it('should create a BetType', () => {
      const betType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(betType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BetType', () => {
      const betType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(betType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BetType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BetType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BetType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBetTypeToCollectionIfMissing', () => {
      it('should add a BetType to an empty array', () => {
        const betType: IBetType = sampleWithRequiredData;
        expectedResult = service.addBetTypeToCollectionIfMissing([], betType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(betType);
      });

      it('should not add a BetType to an array that contains it', () => {
        const betType: IBetType = sampleWithRequiredData;
        const betTypeCollection: IBetType[] = [
          {
            ...betType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBetTypeToCollectionIfMissing(betTypeCollection, betType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BetType to an array that doesn't contain it", () => {
        const betType: IBetType = sampleWithRequiredData;
        const betTypeCollection: IBetType[] = [sampleWithPartialData];
        expectedResult = service.addBetTypeToCollectionIfMissing(betTypeCollection, betType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(betType);
      });

      it('should add only unique BetType to an array', () => {
        const betTypeArray: IBetType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const betTypeCollection: IBetType[] = [sampleWithRequiredData];
        expectedResult = service.addBetTypeToCollectionIfMissing(betTypeCollection, ...betTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const betType: IBetType = sampleWithRequiredData;
        const betType2: IBetType = sampleWithPartialData;
        expectedResult = service.addBetTypeToCollectionIfMissing([], betType, betType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(betType);
        expect(expectedResult).toContain(betType2);
      });

      it('should accept null and undefined values', () => {
        const betType: IBetType = sampleWithRequiredData;
        expectedResult = service.addBetTypeToCollectionIfMissing([], null, betType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(betType);
      });

      it('should return initial array if no BetType is added', () => {
        const betTypeCollection: IBetType[] = [sampleWithRequiredData];
        expectedResult = service.addBetTypeToCollectionIfMissing(betTypeCollection, undefined, null);
        expect(expectedResult).toEqual(betTypeCollection);
      });
    });

    describe('compareBetType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBetType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBetType(entity1, entity2);
        const compareResult2 = service.compareBetType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBetType(entity1, entity2);
        const compareResult2 = service.compareBetType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBetType(entity1, entity2);
        const compareResult2 = service.compareBetType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
