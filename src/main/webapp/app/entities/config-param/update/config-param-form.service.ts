import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConfigParam, NewConfigParam } from '../config-param.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConfigParam for edit and NewConfigParamFormGroupInput for create.
 */
type ConfigParamFormGroupInput = IConfigParam | PartialWithRequiredKeyOf<NewConfigParam>;

type ConfigParamFormDefaults = Pick<NewConfigParam, 'id'>;

type ConfigParamFormGroupContent = {
  id: FormControl<IConfigParam['id'] | NewConfigParam['id']>;
  paramName: FormControl<IConfigParam['paramName']>;
  paramValue: FormControl<IConfigParam['paramValue']>;
};

export type ConfigParamFormGroup = FormGroup<ConfigParamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConfigParamFormService {
  createConfigParamFormGroup(configParam: ConfigParamFormGroupInput = { id: null }): ConfigParamFormGroup {
    const configParamRawValue = {
      ...this.getFormDefaults(),
      ...configParam,
    };
    return new FormGroup<ConfigParamFormGroupContent>({
      id: new FormControl(
        { value: configParamRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      paramName: new FormControl(configParamRawValue.paramName),
      paramValue: new FormControl(configParamRawValue.paramValue),
    });
  }

  getConfigParam(form: ConfigParamFormGroup): IConfigParam | NewConfigParam {
    return form.getRawValue() as IConfigParam | NewConfigParam;
  }

  resetForm(form: ConfigParamFormGroup, configParam: ConfigParamFormGroupInput): void {
    const configParamRawValue = { ...this.getFormDefaults(), ...configParam };
    form.reset(
      {
        ...configParamRawValue,
        id: { value: configParamRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConfigParamFormDefaults {
    return {
      id: null,
    };
  }
}
