import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBetType } from '../bet-type.model';
import { BetTypeService } from '../service/bet-type.service';
import { BetTypeFormService, BetTypeFormGroup } from './bet-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-bet-type-update',
  templateUrl: './bet-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BetTypeUpdateComponent implements OnInit {
  isSaving = false;
  betType: IBetType | null = null;

  editForm: BetTypeFormGroup = this.betTypeFormService.createBetTypeFormGroup();

  constructor(
    protected betTypeService: BetTypeService,
    protected betTypeFormService: BetTypeFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ betType }) => {
      this.betType = betType;
      if (betType) {
        this.updateForm(betType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const betType = this.betTypeFormService.getBetType(this.editForm);
    if (betType.id !== null) {
      this.subscribeToSaveResponse(this.betTypeService.update(betType));
    } else {
      this.subscribeToSaveResponse(this.betTypeService.create(betType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBetType>>): void {
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

  protected updateForm(betType: IBetType): void {
    this.betType = betType;
    this.betTypeFormService.resetForm(this.editForm, betType);
  }
}
