import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BookmakerState } from 'app/entities/enumerations/bookmaker-state.model';
import { IBookmaker } from '../bookmaker.model';
import { BookmakerService } from '../service/bookmaker.service';
import { BookmakerFormService, BookmakerFormGroup } from './bookmaker-form.service';

@Component({
  standalone: true,
  selector: 'jhi-bookmaker-update',
  templateUrl: './bookmaker-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BookmakerUpdateComponent implements OnInit {
  isSaving = false;
  bookmaker: IBookmaker | null = null;
  bookmakerStateValues = Object.keys(BookmakerState);

  editForm: BookmakerFormGroup = this.bookmakerFormService.createBookmakerFormGroup();

  constructor(
    protected bookmakerService: BookmakerService,
    protected bookmakerFormService: BookmakerFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookmaker }) => {
      this.bookmaker = bookmaker;
      if (bookmaker) {
        this.updateForm(bookmaker);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookmaker = this.bookmakerFormService.getBookmaker(this.editForm);
    if (bookmaker.id !== null) {
      this.subscribeToSaveResponse(this.bookmakerService.update(bookmaker));
    } else {
      this.subscribeToSaveResponse(this.bookmakerService.create(bookmaker));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookmaker>>): void {
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

  protected updateForm(bookmaker: IBookmaker): void {
    this.bookmaker = bookmaker;
    this.bookmakerFormService.resetForm(this.editForm, bookmaker);
  }
}
