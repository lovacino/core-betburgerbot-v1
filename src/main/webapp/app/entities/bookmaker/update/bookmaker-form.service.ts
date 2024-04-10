import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBookmaker, NewBookmaker } from '../bookmaker.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBookmaker for edit and NewBookmakerFormGroupInput for create.
 */
type BookmakerFormGroupInput = IBookmaker | PartialWithRequiredKeyOf<NewBookmaker>;

type BookmakerFormDefaults = Pick<NewBookmaker, 'id'>;

type BookmakerFormGroupContent = {
  id: FormControl<IBookmaker['id'] | NewBookmaker['id']>;
  name: FormControl<IBookmaker['name']>;
  state: FormControl<IBookmaker['state']>;
};

export type BookmakerFormGroup = FormGroup<BookmakerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookmakerFormService {
  createBookmakerFormGroup(bookmaker: BookmakerFormGroupInput = { id: null }): BookmakerFormGroup {
    const bookmakerRawValue = {
      ...this.getFormDefaults(),
      ...bookmaker,
    };
    return new FormGroup<BookmakerFormGroupContent>({
      id: new FormControl(
        { value: bookmakerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(bookmakerRawValue.name),
      state: new FormControl(bookmakerRawValue.state),
    });
  }

  getBookmaker(form: BookmakerFormGroup): IBookmaker | NewBookmaker {
    return form.getRawValue() as IBookmaker | NewBookmaker;
  }

  resetForm(form: BookmakerFormGroup, bookmaker: BookmakerFormGroupInput): void {
    const bookmakerRawValue = { ...this.getFormDefaults(), ...bookmaker };
    form.reset(
      {
        ...bookmakerRawValue,
        id: { value: bookmakerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookmakerFormDefaults {
    return {
      id: null,
    };
  }
}
