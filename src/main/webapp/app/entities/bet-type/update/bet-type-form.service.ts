import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBetType, NewBetType } from '../bet-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBetType for edit and NewBetTypeFormGroupInput for create.
 */
type BetTypeFormGroupInput = IBetType | PartialWithRequiredKeyOf<NewBetType>;

type BetTypeFormDefaults = Pick<NewBetType, 'id'>;

type BetTypeFormGroupContent = {
  id: FormControl<IBetType['id'] | NewBetType['id']>;
  name: FormControl<IBetType['name']>;
};

export type BetTypeFormGroup = FormGroup<BetTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BetTypeFormService {
  createBetTypeFormGroup(betType: BetTypeFormGroupInput = { id: null }): BetTypeFormGroup {
    const betTypeRawValue = {
      ...this.getFormDefaults(),
      ...betType,
    };
    return new FormGroup<BetTypeFormGroupContent>({
      id: new FormControl(
        { value: betTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(betTypeRawValue.name),
    });
  }

  getBetType(form: BetTypeFormGroup): IBetType | NewBetType {
    return form.getRawValue() as IBetType | NewBetType;
  }

  resetForm(form: BetTypeFormGroup, betType: BetTypeFormGroupInput): void {
    const betTypeRawValue = { ...this.getFormDefaults(), ...betType };
    form.reset(
      {
        ...betTypeRawValue,
        id: { value: betTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BetTypeFormDefaults {
    return {
      id: null,
    };
  }
}
