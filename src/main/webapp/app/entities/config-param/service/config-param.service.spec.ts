import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConfigParam } from '../config-param.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../config-param.test-samples';

import { ConfigParamService } from './config-param.service';

const requireRestSample: IConfigParam = {
  ...sampleWithRequiredData,
};

describe('ConfigParam Service', () => {
  let service: ConfigParamService;
  let httpMock: HttpTestingController;
  let expectedResult: IConfigParam | IConfigParam[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConfigParamService);
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

    it('should create a ConfigParam', () => {
      const configParam = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(configParam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConfigParam', () => {
      const configParam = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(configParam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConfigParam', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConfigParam', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConfigParam', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConfigParamToCollectionIfMissing', () => {
      it('should add a ConfigParam to an empty array', () => {
        const configParam: IConfigParam = sampleWithRequiredData;
        expectedResult = service.addConfigParamToCollectionIfMissing([], configParam);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configParam);
      });

      it('should not add a ConfigParam to an array that contains it', () => {
        const configParam: IConfigParam = sampleWithRequiredData;
        const configParamCollection: IConfigParam[] = [
          {
            ...configParam,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConfigParamToCollectionIfMissing(configParamCollection, configParam);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConfigParam to an array that doesn't contain it", () => {
        const configParam: IConfigParam = sampleWithRequiredData;
        const configParamCollection: IConfigParam[] = [sampleWithPartialData];
        expectedResult = service.addConfigParamToCollectionIfMissing(configParamCollection, configParam);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configParam);
      });

      it('should add only unique ConfigParam to an array', () => {
        const configParamArray: IConfigParam[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const configParamCollection: IConfigParam[] = [sampleWithRequiredData];
        expectedResult = service.addConfigParamToCollectionIfMissing(configParamCollection, ...configParamArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const configParam: IConfigParam = sampleWithRequiredData;
        const configParam2: IConfigParam = sampleWithPartialData;
        expectedResult = service.addConfigParamToCollectionIfMissing([], configParam, configParam2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configParam);
        expect(expectedResult).toContain(configParam2);
      });

      it('should accept null and undefined values', () => {
        const configParam: IConfigParam = sampleWithRequiredData;
        expectedResult = service.addConfigParamToCollectionIfMissing([], null, configParam, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configParam);
      });

      it('should return initial array if no ConfigParam is added', () => {
        const configParamCollection: IConfigParam[] = [sampleWithRequiredData];
        expectedResult = service.addConfigParamToCollectionIfMissing(configParamCollection, undefined, null);
        expect(expectedResult).toEqual(configParamCollection);
      });
    });

    describe('compareConfigParam', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConfigParam(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConfigParam(entity1, entity2);
        const compareResult2 = service.compareConfigParam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConfigParam(entity1, entity2);
        const compareResult2 = service.compareConfigParam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConfigParam(entity1, entity2);
        const compareResult2 = service.compareConfigParam(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
