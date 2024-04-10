import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../config-param.test-samples';

import { ConfigParamFormService } from './config-param-form.service';

describe('ConfigParam Form Service', () => {
  let service: ConfigParamFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfigParamFormService);
  });

  describe('Service methods', () => {
    describe('createConfigParamFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConfigParamFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paramName: expect.any(Object),
            paramValue: expect.any(Object),
          }),
        );
      });

      it('passing IConfigParam should create a new form with FormGroup', () => {
        const formGroup = service.createConfigParamFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paramName: expect.any(Object),
            paramValue: expect.any(Object),
          }),
        );
      });
    });

    describe('getConfigParam', () => {
      it('should return NewConfigParam for default ConfigParam initial value', () => {
        const formGroup = service.createConfigParamFormGroup(sampleWithNewData);

        const configParam = service.getConfigParam(formGroup) as any;

        expect(configParam).toMatchObject(sampleWithNewData);
      });

      it('should return NewConfigParam for empty ConfigParam initial value', () => {
        const formGroup = service.createConfigParamFormGroup();

        const configParam = service.getConfigParam(formGroup) as any;

        expect(configParam).toMatchObject({});
      });

      it('should return IConfigParam', () => {
        const formGroup = service.createConfigParamFormGroup(sampleWithRequiredData);

        const configParam = service.getConfigParam(formGroup) as any;

        expect(configParam).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConfigParam should not enable id FormControl', () => {
        const formGroup = service.createConfigParamFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConfigParam should disable id FormControl', () => {
        const formGroup = service.createConfigParamFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
