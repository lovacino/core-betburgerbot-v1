import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bookmaker.test-samples';

import { BookmakerFormService } from './bookmaker-form.service';

describe('Bookmaker Form Service', () => {
  let service: BookmakerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookmakerFormService);
  });

  describe('Service methods', () => {
    describe('createBookmakerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookmakerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            state: expect.any(Object),
          }),
        );
      });

      it('passing IBookmaker should create a new form with FormGroup', () => {
        const formGroup = service.createBookmakerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            state: expect.any(Object),
          }),
        );
      });
    });

    describe('getBookmaker', () => {
      it('should return NewBookmaker for default Bookmaker initial value', () => {
        const formGroup = service.createBookmakerFormGroup(sampleWithNewData);

        const bookmaker = service.getBookmaker(formGroup) as any;

        expect(bookmaker).toMatchObject(sampleWithNewData);
      });

      it('should return NewBookmaker for empty Bookmaker initial value', () => {
        const formGroup = service.createBookmakerFormGroup();

        const bookmaker = service.getBookmaker(formGroup) as any;

        expect(bookmaker).toMatchObject({});
      });

      it('should return IBookmaker', () => {
        const formGroup = service.createBookmakerFormGroup(sampleWithRequiredData);

        const bookmaker = service.getBookmaker(formGroup) as any;

        expect(bookmaker).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBookmaker should not enable id FormControl', () => {
        const formGroup = service.createBookmakerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBookmaker should disable id FormControl', () => {
        const formGroup = service.createBookmakerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
