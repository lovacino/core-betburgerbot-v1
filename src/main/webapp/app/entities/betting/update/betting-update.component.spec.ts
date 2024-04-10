import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAccountBet } from 'app/entities/account-bet/account-bet.model';
import { AccountBetService } from 'app/entities/account-bet/service/account-bet.service';
import { ISport } from 'app/entities/sport/sport.model';
import { SportService } from 'app/entities/sport/service/sport.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IBetType } from 'app/entities/bet-type/bet-type.model';
import { BetTypeService } from 'app/entities/bet-type/service/bet-type.service';
import { IBetting } from '../betting.model';
import { BettingService } from '../service/betting.service';
import { BettingFormService } from './betting-form.service';

import { BettingUpdateComponent } from './betting-update.component';

describe('Betting Management Update Component', () => {
  let comp: BettingUpdateComponent;
  let fixture: ComponentFixture<BettingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bettingFormService: BettingFormService;
  let bettingService: BettingService;
  let accountBetService: AccountBetService;
  let sportService: SportService;
  let periodService: PeriodService;
  let betTypeService: BetTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BettingUpdateComponent],
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
      .overrideTemplate(BettingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BettingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bettingFormService = TestBed.inject(BettingFormService);
    bettingService = TestBed.inject(BettingService);
    accountBetService = TestBed.inject(AccountBetService);
    sportService = TestBed.inject(SportService);
    periodService = TestBed.inject(PeriodService);
    betTypeService = TestBed.inject(BetTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AccountBet query and add missing value', () => {
      const betting: IBetting = { id: 456 };
      const account: IAccountBet = { id: 30825 };
      betting.account = account;

      const accountBetCollection: IAccountBet[] = [{ id: 20123 }];
      jest.spyOn(accountBetService, 'query').mockReturnValue(of(new HttpResponse({ body: accountBetCollection })));
      const additionalAccountBets = [account];
      const expectedCollection: IAccountBet[] = [...additionalAccountBets, ...accountBetCollection];
      jest.spyOn(accountBetService, 'addAccountBetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ betting });
      comp.ngOnInit();

      expect(accountBetService.query).toHaveBeenCalled();
      expect(accountBetService.addAccountBetToCollectionIfMissing).toHaveBeenCalledWith(
        accountBetCollection,
        ...additionalAccountBets.map(expect.objectContaining),
      );
      expect(comp.accountBetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Sport query and add missing value', () => {
      const betting: IBetting = { id: 456 };
      const sport: ISport = { id: 17400 };
      betting.sport = sport;

      const sportCollection: ISport[] = [{ id: 9267 }];
      jest.spyOn(sportService, 'query').mockReturnValue(of(new HttpResponse({ body: sportCollection })));
      const additionalSports = [sport];
      const expectedCollection: ISport[] = [...additionalSports, ...sportCollection];
      jest.spyOn(sportService, 'addSportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ betting });
      comp.ngOnInit();

      expect(sportService.query).toHaveBeenCalled();
      expect(sportService.addSportToCollectionIfMissing).toHaveBeenCalledWith(
        sportCollection,
        ...additionalSports.map(expect.objectContaining),
      );
      expect(comp.sportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Period query and add missing value', () => {
      const betting: IBetting = { id: 456 };
      const period: IPeriod = { id: 23101 };
      betting.period = period;

      const periodCollection: IPeriod[] = [{ id: 24201 }];
      jest.spyOn(periodService, 'query').mockReturnValue(of(new HttpResponse({ body: periodCollection })));
      const additionalPeriods = [period];
      const expectedCollection: IPeriod[] = [...additionalPeriods, ...periodCollection];
      jest.spyOn(periodService, 'addPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ betting });
      comp.ngOnInit();

      expect(periodService.query).toHaveBeenCalled();
      expect(periodService.addPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        periodCollection,
        ...additionalPeriods.map(expect.objectContaining),
      );
      expect(comp.periodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BetType query and add missing value', () => {
      const betting: IBetting = { id: 456 };
      const betType: IBetType = { id: 21978 };
      betting.betType = betType;

      const betTypeCollection: IBetType[] = [{ id: 25567 }];
      jest.spyOn(betTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: betTypeCollection })));
      const additionalBetTypes = [betType];
      const expectedCollection: IBetType[] = [...additionalBetTypes, ...betTypeCollection];
      jest.spyOn(betTypeService, 'addBetTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ betting });
      comp.ngOnInit();

      expect(betTypeService.query).toHaveBeenCalled();
      expect(betTypeService.addBetTypeToCollectionIfMissing).toHaveBeenCalledWith(
        betTypeCollection,
        ...additionalBetTypes.map(expect.objectContaining),
      );
      expect(comp.betTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const betting: IBetting = { id: 456 };
      const account: IAccountBet = { id: 11533 };
      betting.account = account;
      const sport: ISport = { id: 27048 };
      betting.sport = sport;
      const period: IPeriod = { id: 23205 };
      betting.period = period;
      const betType: IBetType = { id: 30493 };
      betting.betType = betType;

      activatedRoute.data = of({ betting });
      comp.ngOnInit();

      expect(comp.accountBetsSharedCollection).toContain(account);
      expect(comp.sportsSharedCollection).toContain(sport);
      expect(comp.periodsSharedCollection).toContain(period);
      expect(comp.betTypesSharedCollection).toContain(betType);
      expect(comp.betting).toEqual(betting);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBetting>>();
      const betting = { id: 123 };
      jest.spyOn(bettingFormService, 'getBetting').mockReturnValue(betting);
      jest.spyOn(bettingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ betting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: betting }));
      saveSubject.complete();

      // THEN
      expect(bettingFormService.getBetting).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bettingService.update).toHaveBeenCalledWith(expect.objectContaining(betting));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBetting>>();
      const betting = { id: 123 };
      jest.spyOn(bettingFormService, 'getBetting').mockReturnValue({ id: null });
      jest.spyOn(bettingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ betting: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: betting }));
      saveSubject.complete();

      // THEN
      expect(bettingFormService.getBetting).toHaveBeenCalled();
      expect(bettingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBetting>>();
      const betting = { id: 123 };
      jest.spyOn(bettingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ betting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bettingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAccountBet', () => {
      it('Should forward to accountBetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(accountBetService, 'compareAccountBet');
        comp.compareAccountBet(entity, entity2);
        expect(accountBetService.compareAccountBet).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSport', () => {
      it('Should forward to sportService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sportService, 'compareSport');
        comp.compareSport(entity, entity2);
        expect(sportService.compareSport).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePeriod', () => {
      it('Should forward to periodService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(periodService, 'comparePeriod');
        comp.comparePeriod(entity, entity2);
        expect(periodService.comparePeriod).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBetType', () => {
      it('Should forward to betTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(betTypeService, 'compareBetType');
        comp.compareBetType(entity, entity2);
        expect(betTypeService.compareBetType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
