import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../account-bet.test-samples';

import { AccountBetFormService } from './account-bet-form.service';

describe('AccountBet Form Service', () => {
  let service: AccountBetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountBetFormService);
  });

  describe('Service methods', () => {
    describe('createAccountBetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccountBetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            state: expect.any(Object),
            type: expect.any(Object),
            bettingRoleType: expect.any(Object),
            bettingRoleAmount: expect.any(Object),
            hourActiveStart: expect.any(Object),
            hourActiveEnd: expect.any(Object),
            flgActiveLun: expect.any(Object),
            flgActiveMar: expect.any(Object),
            flgActiveMer: expect.any(Object),
            flgActiveGio: expect.any(Object),
            flgActiveVen: expect.any(Object),
            flgActiveSab: expect.any(Object),
            flgActiveDom: expect.any(Object),
            whatsAppNumber: expect.any(Object),
            userAccount: expect.any(Object),
            passwordAccount: expect.any(Object),
            bookmaker: expect.any(Object),
          }),
        );
      });

      it('passing IAccountBet should create a new form with FormGroup', () => {
        const formGroup = service.createAccountBetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            state: expect.any(Object),
            type: expect.any(Object),
            bettingRoleType: expect.any(Object),
            bettingRoleAmount: expect.any(Object),
            hourActiveStart: expect.any(Object),
            hourActiveEnd: expect.any(Object),
            flgActiveLun: expect.any(Object),
            flgActiveMar: expect.any(Object),
            flgActiveMer: expect.any(Object),
            flgActiveGio: expect.any(Object),
            flgActiveVen: expect.any(Object),
            flgActiveSab: expect.any(Object),
            flgActiveDom: expect.any(Object),
            whatsAppNumber: expect.any(Object),
            userAccount: expect.any(Object),
            passwordAccount: expect.any(Object),
            bookmaker: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccountBet', () => {
      it('should return NewAccountBet for default AccountBet initial value', () => {
        const formGroup = service.createAccountBetFormGroup(sampleWithNewData);

        const accountBet = service.getAccountBet(formGroup) as any;

        expect(accountBet).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccountBet for empty AccountBet initial value', () => {
        const formGroup = service.createAccountBetFormGroup();

        const accountBet = service.getAccountBet(formGroup) as any;

        expect(accountBet).toMatchObject({});
      });

      it('should return IAccountBet', () => {
        const formGroup = service.createAccountBetFormGroup(sampleWithRequiredData);

        const accountBet = service.getAccountBet(formGroup) as any;

        expect(accountBet).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccountBet should not enable id FormControl', () => {
        const formGroup = service.createAccountBetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccountBet should disable id FormControl', () => {
        const formGroup = service.createAccountBetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
