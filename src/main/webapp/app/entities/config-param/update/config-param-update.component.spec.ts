import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConfigParamService } from '../service/config-param.service';
import { IConfigParam } from '../config-param.model';
import { ConfigParamFormService } from './config-param-form.service';

import { ConfigParamUpdateComponent } from './config-param-update.component';

describe('ConfigParam Management Update Component', () => {
  let comp: ConfigParamUpdateComponent;
  let fixture: ComponentFixture<ConfigParamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let configParamFormService: ConfigParamFormService;
  let configParamService: ConfigParamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ConfigParamUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ConfigParamUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfigParamUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    configParamFormService = TestBed.inject(ConfigParamFormService);
    configParamService = TestBed.inject(ConfigParamService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const configParam: IConfigParam = { id: 456 };

      activatedRoute.data = of({ configParam });
      comp.ngOnInit();

      expect(comp.configParam).toEqual(configParam);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfigParam>>();
      const configParam = { id: 123 };
      jest.spyOn(configParamFormService, 'getConfigParam').mockReturnValue(configParam);
      jest.spyOn(configParamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configParam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configParam }));
      saveSubject.complete();

      // THEN
      expect(configParamFormService.getConfigParam).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(configParamService.update).toHaveBeenCalledWith(expect.objectContaining(configParam));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfigParam>>();
      const configParam = { id: 123 };
      jest.spyOn(configParamFormService, 'getConfigParam').mockReturnValue({ id: null });
      jest.spyOn(configParamService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configParam: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configParam }));
      saveSubject.complete();

      // THEN
      expect(configParamFormService.getConfigParam).toHaveBeenCalled();
      expect(configParamService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfigParam>>();
      const configParam = { id: 123 };
      jest.spyOn(configParamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configParam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(configParamService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
