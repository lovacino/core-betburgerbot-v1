import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPeriod } from '../period.model';
import { PeriodService } from '../service/period.service';

@Component({
  standalone: true,
  templateUrl: './period-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PeriodDeleteDialogComponent {
  period?: IPeriod;

  constructor(
    protected periodService: PeriodService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.periodService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
