import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBetting } from '../betting.model';
import { BettingService } from '../service/betting.service';

@Component({
  standalone: true,
  templateUrl: './betting-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BettingDeleteDialogComponent {
  betting?: IBetting;

  constructor(
    protected bettingService: BettingService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bettingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
