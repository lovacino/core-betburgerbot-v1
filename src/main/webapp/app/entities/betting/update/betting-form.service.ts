import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBetting, NewBetting } from '../betting.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBetting for edit and NewBettingFormGroupInput for create.
 */
type BettingFormGroupInput = IBetting | PartialWithRequiredKeyOf<NewBetting>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBetting | NewBetting> = Omit<T, 'startedAt' | 'updatedAt'> & {
  startedAt?: string | null;
  updatedAt?: string | null;
};

type BettingFormRawValue = FormValueOf<IBetting>;

type NewBettingFormRawValue = FormValueOf<NewBetting>;

type BettingFormDefaults = Pick<NewBetting, 'id' | 'startedAt' | 'updatedAt'>;

type BettingFormGroupContent = {
  id: FormControl<BettingFormRawValue['id'] | NewBetting['id']>;
  eventSourceId: FormControl<BettingFormRawValue['eventSourceId']>;
  betTypeParam: FormControl<BettingFormRawValue['betTypeParam']>;
  koef: FormControl<BettingFormRawValue['koef']>;
  home: FormControl<BettingFormRawValue['home']>;
  away: FormControl<BettingFormRawValue['away']>;
  league: FormControl<BettingFormRawValue['league']>;
  eventName: FormControl<BettingFormRawValue['eventName']>;
  startedAt: FormControl<BettingFormRawValue['startedAt']>;
  updatedAt: FormControl<BettingFormRawValue['updatedAt']>;
  betBurgerId: FormControl<BettingFormRawValue['betBurgerId']>;
  state: FormControl<BettingFormRawValue['state']>;
  amountBet: FormControl<BettingFormRawValue['amountBet']>;
  amountBetWin: FormControl<BettingFormRawValue['amountBetWin']>;
  betResultType: FormControl<BettingFormRawValue['betResultType']>;
  account: FormControl<BettingFormRawValue['account']>;
  sport: FormControl<BettingFormRawValue['sport']>;
  period: FormControl<BettingFormRawValue['period']>;
  betType: FormControl<BettingFormRawValue['betType']>;
};

export type BettingFormGroup = FormGroup<BettingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BettingFormService {
  createBettingFormGroup(betting: BettingFormGroupInput = { id: null }): BettingFormGroup {
    const bettingRawValue = this.convertBettingToBettingRawValue({
      ...this.getFormDefaults(),
      ...betting,
    });
    return new FormGroup<BettingFormGroupContent>({
      id: new FormControl(
        { value: bettingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventSourceId: new FormControl(bettingRawValue.eventSourceId),
      betTypeParam: new FormControl(bettingRawValue.betTypeParam),
      koef: new FormControl(bettingRawValue.koef),
      home: new FormControl(bettingRawValue.home),
      away: new FormControl(bettingRawValue.away),
      league: new FormControl(bettingRawValue.league),
      eventName: new FormControl(bettingRawValue.eventName),
      startedAt: new FormControl(bettingRawValue.startedAt),
      updatedAt: new FormControl(bettingRawValue.updatedAt),
      betBurgerId: new FormControl(bettingRawValue.betBurgerId),
      state: new FormControl(bettingRawValue.state),
      amountBet: new FormControl(bettingRawValue.amountBet),
      amountBetWin: new FormControl(bettingRawValue.amountBetWin),
      betResultType: new FormControl(bettingRawValue.betResultType),
      account: new FormControl(bettingRawValue.account),
      sport: new FormControl(bettingRawValue.sport),
      period: new FormControl(bettingRawValue.period),
      betType: new FormControl(bettingRawValue.betType),
    });
  }

  getBetting(form: BettingFormGroup): IBetting | NewBetting {
    return this.convertBettingRawValueToBetting(form.getRawValue() as BettingFormRawValue | NewBettingFormRawValue);
  }

  resetForm(form: BettingFormGroup, betting: BettingFormGroupInput): void {
    const bettingRawValue = this.convertBettingToBettingRawValue({ ...this.getFormDefaults(), ...betting });
    form.reset(
      {
        ...bettingRawValue,
        id: { value: bettingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BettingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startedAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertBettingRawValueToBetting(rawBetting: BettingFormRawValue | NewBettingFormRawValue): IBetting | NewBetting {
    return {
      ...rawBetting,
      startedAt: dayjs(rawBetting.startedAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawBetting.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertBettingToBettingRawValue(
    betting: IBetting | (Partial<NewBetting> & BettingFormDefaults),
  ): BettingFormRawValue | PartialWithRequiredKeyOf<NewBettingFormRawValue> {
    return {
      ...betting,
      startedAt: betting.startedAt ? betting.startedAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: betting.updatedAt ? betting.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
