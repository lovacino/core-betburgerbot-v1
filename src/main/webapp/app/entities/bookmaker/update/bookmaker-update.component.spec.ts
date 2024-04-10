import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BookmakerService } from '../service/bookmaker.service';
import { IBookmaker } from '../bookmaker.model';
import { BookmakerFormService } from './bookmaker-form.service';

import { BookmakerUpdateComponent } from './bookmaker-update.component';

describe('Bookmaker Management Update Component', () => {
  let comp: BookmakerUpdateComponent;
  let fixture: ComponentFixture<BookmakerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookmakerFormService: BookmakerFormService;
  let bookmakerService: BookmakerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BookmakerUpdateComponent],
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
      .overrideTemplate(BookmakerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookmakerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookmakerFormService = TestBed.inject(BookmakerFormService);
    bookmakerService = TestBed.inject(BookmakerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bookmaker: IBookmaker = { id: 456 };

      activatedRoute.data = of({ bookmaker });
      comp.ngOnInit();

      expect(comp.bookmaker).toEqual(bookmaker);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookmaker>>();
      const bookmaker = { id: 123 };
      jest.spyOn(bookmakerFormService, 'getBookmaker').mockReturnValue(bookmaker);
      jest.spyOn(bookmakerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookmaker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookmaker }));
      saveSubject.complete();

      // THEN
      expect(bookmakerFormService.getBookmaker).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookmakerService.update).toHaveBeenCalledWith(expect.objectContaining(bookmaker));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookmaker>>();
      const bookmaker = { id: 123 };
      jest.spyOn(bookmakerFormService, 'getBookmaker').mockReturnValue({ id: null });
      jest.spyOn(bookmakerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookmaker: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookmaker }));
      saveSubject.complete();

      // THEN
      expect(bookmakerFormService.getBookmaker).toHaveBeenCalled();
      expect(bookmakerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookmaker>>();
      const bookmaker = { id: 123 };
      jest.spyOn(bookmakerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookmaker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookmakerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
