import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBookmaker } from 'app/entities/bookmaker/bookmaker.model';
import { BookmakerService } from 'app/entities/bookmaker/service/bookmaker.service';
import { AccountBetState } from 'app/entities/enumerations/account-bet-state.model';
import { AccountBetType } from 'app/entities/enumerations/account-bet-type.model';
import { BettingRoleType } from 'app/entities/enumerations/betting-role-type.model';
import { AccountBetService } from '../service/account-bet.service';
import { IAccountBet } from '../account-bet.model';
import { AccountBetFormService, AccountBetFormGroup } from './account-bet-form.service';

@Component({
  standalone: true,
  selector: 'jhi-account-bet-update',
  templateUrl: './account-bet-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountBetUpdateComponent implements OnInit {
  isSaving = false;
  accountBet: IAccountBet | null = null;
  accountBetStateValues = Object.keys(AccountBetState);
  accountBetTypeValues = Object.keys(AccountBetType);
  bettingRoleTypeValues = Object.keys(BettingRoleType);

  bookmakersSharedCollection: IBookmaker[] = [];

  editForm: AccountBetFormGroup = this.accountBetFormService.createAccountBetFormGroup();

  constructor(
    protected accountBetService: AccountBetService,
    protected accountBetFormService: AccountBetFormService,
    protected bookmakerService: BookmakerService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareBookmaker = (o1: IBookmaker | null, o2: IBookmaker | null): boolean => this.bookmakerService.compareBookmaker(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountBet }) => {
      this.accountBet = accountBet;
      if (accountBet) {
        this.updateForm(accountBet);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountBet = this.accountBetFormService.getAccountBet(this.editForm);
    if (accountBet.id !== null) {
      this.subscribeToSaveResponse(this.accountBetService.update(accountBet));
    } else {
      this.subscribeToSaveResponse(this.accountBetService.create(accountBet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountBet>>): void {
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

  protected updateForm(accountBet: IAccountBet): void {
    this.accountBet = accountBet;
    this.accountBetFormService.resetForm(this.editForm, accountBet);

    this.bookmakersSharedCollection = this.bookmakerService.addBookmakerToCollectionIfMissing<IBookmaker>(
      this.bookmakersSharedCollection,
      accountBet.bookmaker,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bookmakerService
      .query()
      .pipe(map((res: HttpResponse<IBookmaker[]>) => res.body ?? []))
      .pipe(
        map((bookmakers: IBookmaker[]) =>
          this.bookmakerService.addBookmakerToCollectionIfMissing<IBookmaker>(bookmakers, this.accountBet?.bookmaker),
        ),
      )
      .subscribe((bookmakers: IBookmaker[]) => (this.bookmakersSharedCollection = bookmakers));
  }
}
