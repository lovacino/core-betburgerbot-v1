import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBookmaker } from 'app/entities/bookmaker/bookmaker.model';
import { BookmakerService } from 'app/entities/bookmaker/service/bookmaker.service';
import { ISport } from 'app/entities/sport/sport.model';
import { SportService } from 'app/entities/sport/service/sport.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IBetType } from 'app/entities/bet-type/bet-type.model';
import { BetTypeService } from 'app/entities/bet-type/service/bet-type.service';
import { EventSourceService } from '../service/event-source.service';
import { IEventSource } from '../event-source.model';
import { EventSourceFormService, EventSourceFormGroup } from './event-source-form.service';

@Component({
  standalone: true,
  selector: 'jhi-event-source-update',
  templateUrl: './event-source-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EventSourceUpdateComponent implements OnInit {
  isSaving = false;
  eventSource: IEventSource | null = null;

  bookmakersSharedCollection: IBookmaker[] = [];
  sportsSharedCollection: ISport[] = [];
  periodsSharedCollection: IPeriod[] = [];
  betTypesSharedCollection: IBetType[] = [];

  editForm: EventSourceFormGroup = this.eventSourceFormService.createEventSourceFormGroup();

  constructor(
    protected eventSourceService: EventSourceService,
    protected eventSourceFormService: EventSourceFormService,
    protected bookmakerService: BookmakerService,
    protected sportService: SportService,
    protected periodService: PeriodService,
    protected betTypeService: BetTypeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareBookmaker = (o1: IBookmaker | null, o2: IBookmaker | null): boolean => this.bookmakerService.compareBookmaker(o1, o2);

  compareSport = (o1: ISport | null, o2: ISport | null): boolean => this.sportService.compareSport(o1, o2);

  comparePeriod = (o1: IPeriod | null, o2: IPeriod | null): boolean => this.periodService.comparePeriod(o1, o2);

  compareBetType = (o1: IBetType | null, o2: IBetType | null): boolean => this.betTypeService.compareBetType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventSource }) => {
      this.eventSource = eventSource;
      if (eventSource) {
        this.updateForm(eventSource);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventSource = this.eventSourceFormService.getEventSource(this.editForm);
    if (eventSource.id !== null) {
      this.subscribeToSaveResponse(this.eventSourceService.update(eventSource));
    } else {
      this.subscribeToSaveResponse(this.eventSourceService.create(eventSource));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventSource>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(eventSource: IEventSource): void {
    this.eventSource = eventSource;
    this.eventSourceFormService.resetForm(this.editForm, eventSource);

    this.bookmakersSharedCollection = this.bookmakerService.addBookmakerToCollectionIfMissing<IBookmaker>(
      this.bookmakersSharedCollection,
      eventSource.bookmaker,
    );
    this.sportsSharedCollection = this.sportService.addSportToCollectionIfMissing<ISport>(this.sportsSharedCollection, eventSource.sport);
    this.periodsSharedCollection = this.periodService.addPeriodToCollectionIfMissing<IPeriod>(
      this.periodsSharedCollection,
      eventSource.period,
    );
    this.betTypesSharedCollection = this.betTypeService.addBetTypeToCollectionIfMissing<IBetType>(
      this.betTypesSharedCollection,
      eventSource.betType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bookmakerService
      .query()
      .pipe(map((res: HttpResponse<IBookmaker[]>) => res.body ?? []))
      .pipe(
        map((bookmakers: IBookmaker[]) =>
          this.bookmakerService.addBookmakerToCollectionIfMissing<IBookmaker>(bookmakers, this.eventSource?.bookmaker),
        ),
      )
      .subscribe((bookmakers: IBookmaker[]) => (this.bookmakersSharedCollection = bookmakers));

    this.sportService
      .query()
      .pipe(map((res: HttpResponse<ISport[]>) => res.body ?? []))
      .pipe(map((sports: ISport[]) => this.sportService.addSportToCollectionIfMissing<ISport>(sports, this.eventSource?.sport)))
      .subscribe((sports: ISport[]) => (this.sportsSharedCollection = sports));

    this.periodService
      .query()
      .pipe(map((res: HttpResponse<IPeriod[]>) => res.body ?? []))
      .pipe(map((periods: IPeriod[]) => this.periodService.addPeriodToCollectionIfMissing<IPeriod>(periods, this.eventSource?.period)))
      .subscribe((periods: IPeriod[]) => (this.periodsSharedCollection = periods));

    this.betTypeService
      .query()
      .pipe(map((res: HttpResponse<IBetType[]>) => res.body ?? []))
      .pipe(
        map((betTypes: IBetType[]) => this.betTypeService.addBetTypeToCollectionIfMissing<IBetType>(betTypes, this.eventSource?.betType)),
      )
      .subscribe((betTypes: IBetType[]) => (this.betTypesSharedCollection = betTypes));
  }
}
