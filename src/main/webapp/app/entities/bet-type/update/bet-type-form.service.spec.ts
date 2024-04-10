import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bet-type.test-samples';

import { BetTypeFormService } from './bet-type-form.service';

describe('BetType Form Service', () => {
  let service: BetTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BetTypeFormService);
  });

  describe('Service methods', () => {
    describe('createBetTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBetTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IBetType should create a new form with FormGroup', () => {
        const formGroup = service.createBetTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getBetType', () => {
      it('should return NewBetType for default BetType initial value', () => {
        const formGroup = service.createBetTypeFormGroup(sampleWithNewData);

        const betType = service.getBetType(formGroup) as any;

        expect(betType).toMatchObject(sampleWithNewData);
      });

      it('should return NewBetType for empty BetType initial value', () => {
        const formGroup = service.createBetTypeFormGroup();

        const betType = service.getBetType(formGroup) as any;

        expect(betType).toMatchObject({});
      });

      it('should return IBetType', () => {
        const formGroup = service.createBetTypeFormGroup(sampleWithRequiredData);

        const betType = service.getBetType(formGroup) as any;

        expect(betType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBetType should not enable id FormControl', () => {
        const formGroup = service.createBetTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBetType should disable id FormControl', () => {
        const formGroup = service.createBetTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
