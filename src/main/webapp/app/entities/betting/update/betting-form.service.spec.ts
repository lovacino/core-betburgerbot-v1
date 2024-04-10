import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../betting.test-samples';

import { BettingFormService } from './betting-form.service';

describe('Betting Form Service', () => {
  let service: BettingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BettingFormService);
  });

  describe('Service methods', () => {
    describe('createBettingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBettingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventSourceId: expect.any(Object),
            betTypeParam: expect.any(Object),
            koef: expect.any(Object),
            home: expect.any(Object),
            away: expect.any(Object),
            league: expect.any(Object),
            eventName: expect.any(Object),
            startedAt: expect.any(Object),
            updatedAt: expect.any(Object),
            betBurgerId: expect.any(Object),
            state: expect.any(Object),
            amountBet: expect.any(Object),
            amountBetWin: expect.any(Object),
            betResultType: expect.any(Object),
            account: expect.any(Object),
            sport: expect.any(Object),
            period: expect.any(Object),
            betType: expect.any(Object),
          }),
        );
      });

      it('passing IBetting should create a new form with FormGroup', () => {
        const formGroup = service.createBettingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventSourceId: expect.any(Object),
            betTypeParam: expect.any(Object),
            koef: expect.any(Object),
            home: expect.any(Object),
            away: expect.any(Object),
            league: expect.any(Object),
            eventName: expect.any(Object),
            startedAt: expect.any(Object),
            updatedAt: expect.any(Object),
            betBurgerId: expect.any(Object),
            state: expect.any(Object),
            amountBet: expect.any(Object),
            amountBetWin: expect.any(Object),
            betResultType: expect.any(Object),
            account: expect.any(Object),
            sport: expect.any(Object),
            period: expect.any(Object),
            betType: expect.any(Object),
          }),
        );
      });
    });

    describe('getBetting', () => {
      it('should return NewBetting for default Betting initial value', () => {
        const formGroup = service.createBettingFormGroup(sampleWithNewData);

        const betting = service.getBetting(formGroup) as any;

        expect(betting).toMatchObject(sampleWithNewData);
      });

      it('should return NewBetting for empty Betting initial value', () => {
        const formGroup = service.createBettingFormGroup();

        const betting = service.getBetting(formGroup) as any;

        expect(betting).toMatchObject({});
      });

      it('should return IBetting', () => {
        const formGroup = service.createBettingFormGroup(sampleWithRequiredData);

        const betting = service.getBetting(formGroup) as any;

        expect(betting).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBetting should not enable id FormControl', () => {
        const formGroup = service.createBettingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBetting should disable id FormControl', () => {
        const formGroup = service.createBettingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
