import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IBookmaker } from 'app/entities/bookmaker/bookmaker.model';
import { BookmakerService } from 'app/entities/bookmaker/service/bookmaker.service';
import { ISport } from 'app/entities/sport/sport.model';
import { SportService } from 'app/entities/sport/service/sport.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IBetType } from 'app/entities/bet-type/bet-type.model';
import { BetTypeService } from 'app/entities/bet-type/service/bet-type.service';
import { IEventSource } from '../event-source.model';
import { EventSourceService } from '../service/event-source.service';
import { EventSourceFormService } from './event-source-form.service';

import { EventSourceUpdateComponent } from './event-source-update.component';

describe('EventSource Management Update Component', () => {
  let comp: EventSourceUpdateComponent;
  let fixture: ComponentFixture<EventSourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventSourceFormService: EventSourceFormService;
  let eventSourceService: EventSourceService;
  let bookmakerService: BookmakerService;
  let sportService: SportService;
  let periodService: PeriodService;
  let betTypeService: BetTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EventSourceUpdateComponent],
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
      .overrideTemplate(EventSourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventSourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventSourceFormService = TestBed.inject(EventSourceFormService);
    eventSourceService = TestBed.inject(EventSourceService);
    bookmakerService = TestBed.inject(BookmakerService);
    sportService = TestBed.inject(SportService);
    periodService = TestBed.inject(PeriodService);
    betTypeService = TestBed.inject(BetTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bookmaker query and add missing value', () => {
      const eventSource: IEventSource = { id: 456 };
      const bookmaker: IBookmaker = { id: 26400 };
      eventSource.bookmaker = bookmaker;

      const bookmakerCollection: IBookmaker[] = [{ id: 28261 }];
      jest.spyOn(bookmakerService, 'query').mockReturnValue(of(new HttpResponse({ body: bookmakerCollection })));
      const additionalBookmakers = [bookmaker];
      const expectedCollection: IBookmaker[] = [...additionalBookmakers, ...bookmakerCollection];
      jest.spyOn(bookmakerService, 'addBookmakerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventSource });
      comp.ngOnInit();

      expect(bookmakerService.query).toHaveBeenCalled();
      expect(bookmakerService.addBookmakerToCollectionIfMissing).toHaveBeenCalledWith(
        bookmakerCollection,
        ...additionalBookmakers.map(expect.objectContaining),
      );
      expect(comp.bookmakersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Sport query and add missing value', () => {
      const eventSource: IEventSource = { id: 456 };
      const sport: ISport = { id: 29957 };
      eventSource.sport = sport;

      const sportCollection: ISport[] = [{ id: 12400 }];
      jest.spyOn(sportService, 'query').mockReturnValue(of(new HttpResponse({ body: sportCollection })));
      const additionalSports = [sport];
      const expectedCollection: ISport[] = [...additionalSports, ...sportCollection];
      jest.spyOn(sportService, 'addSportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventSource });
      comp.ngOnInit();

      expect(sportService.query).toHaveBeenCalled();
      expect(sportService.addSportToCollectionIfMissing).toHaveBeenCalledWith(
        sportCollection,
        ...additionalSports.map(expect.objectContaining),
      );
      expect(comp.sportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Period query and add missing value', () => {
      const eventSource: IEventSource = { id: 456 };
      const period: IPeriod = { id: 24964 };
      eventSource.period = period;

      const periodCollection: IPeriod[] = [{ id: 15540 }];
      jest.spyOn(periodService, 'query').mockReturnValue(of(new HttpResponse({ body: periodCollection })));
      const additionalPeriods = [period];
      const expectedCollection: IPeriod[] = [...additionalPeriods, ...periodCollection];
      jest.spyOn(periodService, 'addPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventSource });
      comp.ngOnInit();

      expect(periodService.query).toHaveBeenCalled();
      expect(periodService.addPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        periodCollection,
        ...additionalPeriods.map(expect.objectContaining),
      );
      expect(comp.periodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BetType query and add missing value', () => {
      const eventSource: IEventSource = { id: 456 };
      const betType: IBetType = { id: 6981 };
      eventSource.betType = betType;

      const betTypeCollection: IBetType[] = [{ id: 20611 }];
      jest.spyOn(betTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: betTypeCollection })));
      const additionalBetTypes = [betType];
      const expectedCollection: IBetType[] = [...additionalBetTypes, ...betTypeCollection];
      jest.spyOn(betTypeService, 'addBetTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventSource });
      comp.ngOnInit();

      expect(betTypeService.query).toHaveBeenCalled();
      expect(betTypeService.addBetTypeToCollectionIfMissing).toHaveBeenCalledWith(
        betTypeCollection,
        ...additionalBetTypes.map(expect.objectContaining),
      );
      expect(comp.betTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eventSource: IEventSource = { id: 456 };
      const bookmaker: IBookmaker = { id: 25469 };
      eventSource.bookmaker = bookmaker;
      const sport: ISport = { id: 10974 };
      eventSource.sport = sport;
      const period: IPeriod = { id: 755 };
      eventSource.period = period;
      const betType: IBetType = { id: 15035 };
      eventSource.betType = betType;

      activatedRoute.data = of({ eventSource });
      comp.ngOnInit();

      expect(comp.bookmakersSharedCollection).toContain(bookmaker);
      expect(comp.sportsSharedCollection).toContain(sport);
      expect(comp.periodsSharedCollection).toContain(period);
      expect(comp.betTypesSharedCollection).toContain(betType);
      expect(comp.eventSource).toEqual(eventSource);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventSource>>();
      const eventSource = { id: 123 };
      jest.spyOn(eventSourceFormService, 'getEventSource').mockReturnValue(eventSource);
      jest.spyOn(eventSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventSource }));
      saveSubject.complete();

      // THEN
      expect(eventSourceFormService.getEventSource).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventSourceService.update).toHaveBeenCalledWith(expect.objectContaining(eventSource));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventSource>>();
      const eventSource = { id: 123 };
      jest.spyOn(eventSourceFormService, 'getEventSource').mockReturnValue({ id: null });
      jest.spyOn(eventSourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventSource: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventSource }));
      saveSubject.complete();

      // THEN
      expect(eventSourceFormService.getEventSource).toHaveBeenCalled();
      expect(eventSourceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventSource>>();
      const eventSource = { id: 123 };
      jest.spyOn(eventSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventSourceService.update).toHaveBeenCalled();
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
