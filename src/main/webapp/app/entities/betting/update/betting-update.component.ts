import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAccountBet } from 'app/entities/account-bet/account-bet.model';
import { AccountBetService } from 'app/entities/account-bet/service/account-bet.service';
import { ISport } from 'app/entities/sport/sport.model';
import { SportService } from 'app/entities/sport/service/sport.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IBetType } from 'app/entities/bet-type/bet-type.model';
import { BetTypeService } from 'app/entities/bet-type/service/bet-type.service';
import { BettingState } from 'app/entities/enumerations/betting-state.model';
import { BetResultType } from 'app/entities/enumerations/bet-result-type.model';
import { BettingService } from '../service/betting.service';
import { IBetting } from '../betting.model';
import { BettingFormService, BettingFormGroup } from './betting-form.service';

@Component({
  standalone: true,
  selector: 'jhi-betting-update',
  templateUrl: './betting-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BettingUpdateComponent implements OnInit {
  isSaving = false;
  betting: IBetting | null = null;
  bettingStateValues = Object.keys(BettingState);
  betResultTypeValues = Object.keys(BetResultType);

  accountBetsSharedCollection: IAccountBet[] = [];
  sportsSharedCollection: ISport[] = [];
  periodsSharedCollection: IPeriod[] = [];
  betTypesSharedCollection: IBetType[] = [];

  editForm: BettingFormGroup = this.bettingFormService.createBettingFormGroup();

  constructor(
    protected bettingService: BettingService,
    protected bettingFormService: BettingFormService,
    protected accountBetService: AccountBetService,
    protected sportService: SportService,
    protected periodService: PeriodService,
    protected betTypeService: BetTypeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAccountBet = (o1: IAccountBet | null, o2: IAccountBet | null): boolean => this.accountBetService.compareAccountBet(o1, o2);

  compareSport = (o1: ISport | null, o2: ISport | null): boolean => this.sportService.compareSport(o1, o2);

  comparePeriod = (o1: IPeriod | null, o2: IPeriod | null): boolean => this.periodService.comparePeriod(o1, o2);

  compareBetType = (o1: IBetType | null, o2: IBetType | null): boolean => this.betTypeService.compareBetType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ betting }) => {
      this.betting = betting;
      if (betting) {
        this.updateForm(betting);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const betting = this.bettingFormService.getBetting(this.editForm);
    if (betting.id !== null) {
      this.subscribeToSaveResponse(this.bettingService.update(betting));
    } else {
      this.subscribeToSaveResponse(this.bettingService.create(betting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBetting>>): void {
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

  protected updateForm(betting: IBetting): void {
    this.betting = betting;
    this.bettingFormService.resetForm(this.editForm, betting);

    this.accountBetsSharedCollection = this.accountBetService.addAccountBetToCollectionIfMissing<IAccountBet>(
      this.accountBetsSharedCollection,
      betting.account,
    );
    this.sportsSharedCollection = this.sportService.addSportToCollectionIfMissing<ISport>(this.sportsSharedCollection, betting.sport);
    this.periodsSharedCollection = this.periodService.addPeriodToCollectionIfMissing<IPeriod>(this.periodsSharedCollection, betting.period);
    this.betTypesSharedCollection = this.betTypeService.addBetTypeToCollectionIfMissing<IBetType>(
      this.betTypesSharedCollection,
      betting.betType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.accountBetService
      .query()
      .pipe(map((res: HttpResponse<IAccountBet[]>) => res.body ?? []))
      .pipe(
        map((accountBets: IAccountBet[]) =>
          this.accountBetService.addAccountBetToCollectionIfMissing<IAccountBet>(accountBets, this.betting?.account),
        ),
      )
      .subscribe((accountBets: IAccountBet[]) => (this.accountBetsSharedCollection = accountBets));

    this.sportService
      .query()
      .pipe(map((res: HttpResponse<ISport[]>) => res.body ?? []))
      .pipe(map((sports: ISport[]) => this.sportService.addSportToCollectionIfMissing<ISport>(sports, this.betting?.sport)))
      .subscribe((sports: ISport[]) => (this.sportsSharedCollection = sports));

    this.periodService
      .query()
      .pipe(map((res: HttpResponse<IPeriod[]>) => res.body ?? []))
      .pipe(map((periods: IPeriod[]) => this.periodService.addPeriodToCollectionIfMissing<IPeriod>(periods, this.betting?.period)))
      .subscribe((periods: IPeriod[]) => (this.periodsSharedCollection = periods));

    this.betTypeService
      .query()
      .pipe(map((res: HttpResponse<IBetType[]>) => res.body ?? []))
      .pipe(map((betTypes: IBetType[]) => this.betTypeService.addBetTypeToCollectionIfMissing<IBetType>(betTypes, this.betting?.betType)))
      .subscribe((betTypes: IBetType[]) => (this.betTypesSharedCollection = betTypes));
  }
}
