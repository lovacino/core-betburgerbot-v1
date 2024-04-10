import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IBookmaker } from 'app/entities/bookmaker/bookmaker.model';
import { BookmakerService } from 'app/entities/bookmaker/service/bookmaker.service';
import { AccountBetService } from '../service/account-bet.service';
import { IAccountBet } from '../account-bet.model';
import { AccountBetFormService } from './account-bet-form.service';

import { AccountBetUpdateComponent } from './account-bet-update.component';

describe('AccountBet Management Update Component', () => {
  let comp: AccountBetUpdateComponent;
  let fixture: ComponentFixture<AccountBetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountBetFormService: AccountBetFormService;
  let accountBetService: AccountBetService;
  let bookmakerService: BookmakerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AccountBetUpdateComponent],
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
      .overrideTemplate(AccountBetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountBetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountBetFormService = TestBed.inject(AccountBetFormService);
    accountBetService = TestBed.inject(AccountBetService);
    bookmakerService = TestBed.inject(BookmakerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bookmaker query and add missing value', () => {
      const accountBet: IAccountBet = { id: 456 };
      const bookmaker: IBookmaker = { id: 5354 };
      accountBet.bookmaker = bookmaker;

      const bookmakerCollection: IBookmaker[] = [{ id: 21260 }];
      jest.spyOn(bookmakerService, 'query').mockReturnValue(of(new HttpResponse({ body: bookmakerCollection })));
      const additionalBookmakers = [bookmaker];
      const expectedCollection: IBookmaker[] = [...additionalBookmakers, ...bookmakerCollection];
      jest.spyOn(bookmakerService, 'addBookmakerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountBet });
      comp.ngOnInit();

      expect(bookmakerService.query).toHaveBeenCalled();
      expect(bookmakerService.addBookmakerToCollectionIfMissing).toHaveBeenCalledWith(
        bookmakerCollection,
        ...additionalBookmakers.map(expect.objectContaining),
      );
      expect(comp.bookmakersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountBet: IAccountBet = { id: 456 };
      const bookmaker: IBookmaker = { id: 27125 };
      accountBet.bookmaker = bookmaker;

      activatedRoute.data = of({ accountBet });
      comp.ngOnInit();

      expect(comp.bookmakersSharedCollection).toContain(bookmaker);
      expect(comp.accountBet).toEqual(accountBet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBet>>();
      const accountBet = { id: 123 };
      jest.spyOn(accountBetFormService, 'getAccountBet').mockReturnValue(accountBet);
      jest.spyOn(accountBetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountBet }));
      saveSubject.complete();

      // THEN
      expect(accountBetFormService.getAccountBet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountBetService.update).toHaveBeenCalledWith(expect.objectContaining(accountBet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBet>>();
      const accountBet = { id: 123 };
      jest.spyOn(accountBetFormService, 'getAccountBet').mockReturnValue({ id: null });
      jest.spyOn(accountBetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountBet }));
      saveSubject.complete();

      // THEN
      expect(accountBetFormService.getAccountBet).toHaveBeenCalled();
      expect(accountBetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBet>>();
      const accountBet = { id: 123 };
      jest.spyOn(accountBetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountBetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBookmaker', () => {
      it('Should forward to bookmakerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bookmakerService, 'compareBookmaker');
        comp.compareBookmaker(entity, entity2);
        expect(bookmakerService.compareBookmaker).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
