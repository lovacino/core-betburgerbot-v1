import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPeriod } from '../period.model';
import { PeriodService } from '../service/period.service';
import { PeriodFormService, PeriodFormGroup } from './period-form.service';

@Component({
  standalone: true,
  selector: 'jhi-period-update',
  templateUrl: './period-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PeriodUpdateComponent implements OnInit {
  isSaving = false;
  period: IPeriod | null = null;

  editForm: PeriodFormGroup = this.periodFormService.createPeriodFormGroup();

  constructor(
    protected periodService: PeriodService,
    protected periodFormService: PeriodFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ period }) => {
      this.period = period;
      if (period) {
        this.updateForm(period);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const period = this.periodFormService.getPeriod(this.editForm);
    if (period.id !== null) {
      this.subscribeToSaveResponse(this.periodService.update(period));
    } else {
      this.subscribeToSaveResponse(this.periodService.create(period));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriod>>): void {
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

  protected updateForm(period: IPeriod): void {
    this.period = period;
    this.periodFormService.resetForm(this.editForm, period);
  }
}
