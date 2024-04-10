import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConfigParam } from '../config-param.model';
import { ConfigParamService } from '../service/config-param.service';
import { ConfigParamFormService, ConfigParamFormGroup } from './config-param-form.service';

@Component({
  standalone: true,
  selector: 'jhi-config-param-update',
  templateUrl: './config-param-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConfigParamUpdateComponent implements OnInit {
  isSaving = false;
  configParam: IConfigParam | null = null;

  editForm: ConfigParamFormGroup = this.configParamFormService.createConfigParamFormGroup();

  constructor(
    protected configParamService: ConfigParamService,
    protected configParamFormService: ConfigParamFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configParam }) => {
      this.configParam = configParam;
      if (configParam) {
        this.updateForm(configParam);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configParam = this.configParamFormService.getConfigParam(this.editForm);
    if (configParam.id !== null) {
      this.subscribeToSaveResponse(this.configParamService.update(configParam));
    } else {
      this.subscribeToSaveResponse(this.configParamService.create(configParam));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigParam>>): void {
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

  protected updateForm(configParam: IConfigParam): void {
    this.configParam = configParam;
    this.configParamFormService.resetForm(this.editForm, configParam);
  }
}
