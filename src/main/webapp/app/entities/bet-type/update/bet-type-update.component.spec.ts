import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BetTypeService } from '../service/bet-type.service';
import { IBetType } from '../bet-type.model';
import { BetTypeFormService } from './bet-type-form.service';

import { BetTypeUpdateComponent } from './bet-type-update.component';

describe('BetType Management Update Component', () => {
  let comp: BetTypeUpdateComponent;
  let fixture: ComponentFixture<BetTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let betTypeFormService: BetTypeFormService;
  let betTypeService: BetTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BetTypeUpdateComponent],
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
      .overrideTemplate(BetTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BetTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    betTypeFormService = TestBed.inject(BetTypeFormService);
    betTypeService = TestBed.inject(BetTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const betType: IBetType = { id: 456 };

      activatedRoute.data = of({ betType });
      comp.ngOnInit();

      expect(comp.betType).toEqual(betType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBetType>>();
      const betType = { id: 123 };
      jest.spyOn(betTypeFormService, 'getBetType').mockReturnValue(betType);
      jest.spyOn(betTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ betType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: betType }));
      saveSubject.complete();

      // THEN
      expect(betTypeFormService.getBetType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(betTypeService.update).toHaveBeenCalledWith(expect.objectContaining(betType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBetType>>();
      const betType = { id: 123 };
      jest.spyOn(betTypeFormService, 'getBetType').mockReturnValue({ id: null });
      jest.spyOn(betTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ betType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: betType }));
      saveSubject.complete();

      // THEN
      expect(betTypeFormService.getBetType).toHaveBeenCalled();
      expect(betTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBetType>>();
      const betType = { id: 123 };
      jest.spyOn(betTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ betType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(betTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
