import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccountBet, NewAccountBet } from '../account-bet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountBet for edit and NewAccountBetFormGroupInput for create.
 */
type AccountBetFormGroupInput = IAccountBet | PartialWithRequiredKeyOf<NewAccountBet>;

type AccountBetFormDefaults = Pick<
  NewAccountBet,
  'id' | 'flgActiveLun' | 'flgActiveMar' | 'flgActiveMer' | 'flgActiveGio' | 'flgActiveVen' | 'flgActiveSab' | 'flgActiveDom'
>;

type AccountBetFormGroupContent = {
  id: FormControl<IAccountBet['id'] | NewAccountBet['id']>;
  name: FormControl<IAccountBet['name']>;
  state: FormControl<IAccountBet['state']>;
  type: FormControl<IAccountBet['type']>;
  bettingRoleType: FormControl<IAccountBet['bettingRoleType']>;
  bettingRoleAmount: FormControl<IAccountBet['bettingRoleAmount']>;
  hourActiveStart: FormControl<IAccountBet['hourActiveStart']>;
  hourActiveEnd: FormControl<IAccountBet['hourActiveEnd']>;
  flgActiveLun: FormControl<IAccountBet['flgActiveLun']>;
  flgActiveMar: FormControl<IAccountBet['flgActiveMar']>;
  flgActiveMer: FormControl<IAccountBet['flgActiveMer']>;
  flgActiveGio: FormControl<IAccountBet['flgActiveGio']>;
  flgActiveVen: FormControl<IAccountBet['flgActiveVen']>;
  flgActiveSab: FormControl<IAccountBet['flgActiveSab']>;
  flgActiveDom: FormControl<IAccountBet['flgActiveDom']>;
  whatsAppNumber: FormControl<IAccountBet['whatsAppNumber']>;
  userAccount: FormControl<IAccountBet['userAccount']>;
  passwordAccount: FormControl<IAccountBet['passwordAccount']>;
  bookmaker: FormControl<IAccountBet['bookmaker']>;
};

export type AccountBetFormGroup = FormGroup<AccountBetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountBetFormService {
  createAccountBetFormGroup(accountBet: AccountBetFormGroupInput = { id: null }): AccountBetFormGroup {
    const accountBetRawValue = {
      ...this.getFormDefaults(),
      ...accountBet,
    };
    return new FormGroup<AccountBetFormGroupContent>({
      id: new FormControl(
        { value: accountBetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(accountBetRawValue.name),
      state: new FormControl(accountBetRawValue.state),
      type: new FormControl(accountBetRawValue.type),
      bettingRoleType: new FormControl(accountBetRawValue.bettingRoleType),
      bettingRoleAmount: new FormControl(accountBetRawValue.bettingRoleAmount),
      hourActiveStart: new FormControl(accountBetRawValue.hourActiveStart),
      hourActiveEnd: new FormControl(accountBetRawValue.hourActiveEnd),
      flgActiveLun: new FormControl(accountBetRawValue.flgActiveLun),
      flgActiveMar: new FormControl(accountBetRawValue.flgActiveMar),
      flgActiveMer: new FormControl(accountBetRawValue.flgActiveMer),
      flgActiveGio: new FormControl(accountBetRawValue.flgActiveGio),
      flgActiveVen: new FormControl(accountBetRawValue.flgActiveVen),
      flgActiveSab: new FormControl(accountBetRawValue.flgActiveSab),
      flgActiveDom: new FormControl(accountBetRawValue.flgActiveDom),
      whatsAppNumber: new FormControl(accountBetRawValue.whatsAppNumber),
      userAccount: new FormControl(accountBetRawValue.userAccount),
      passwordAccount: new FormControl(accountBetRawValue.passwordAccount),
      bookmaker: new FormControl(accountBetRawValue.bookmaker),
    });
  }

  getAccountBet(form: AccountBetFormGroup): IAccountBet | NewAccountBet {
    return form.getRawValue() as IAccountBet | NewAccountBet;
  }

  resetForm(form: AccountBetFormGroup, accountBet: AccountBetFormGroupInput): void {
    const accountBetRawValue = { ...this.getFormDefaults(), ...accountBet };
    form.reset(
      {
        ...accountBetRawValue,
        id: { value: accountBetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountBetFormDefaults {
    return {
      id: null,
      flgActiveLun: false,
      flgActiveMar: false,
      flgActiveMer: false,
      flgActiveGio: false,
      flgActiveVen: false,
      flgActiveSab: false,
      flgActiveDom: false,
    };
  }
}
