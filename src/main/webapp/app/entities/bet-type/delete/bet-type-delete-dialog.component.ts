import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBetType } from '../bet-type.model';
import { BetTypeService } from '../service/bet-type.service';

@Component({
  standalone: true,
  templateUrl: './bet-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BetTypeDeleteDialogComponent {
  betType?: IBetType;

  constructor(
    protected betTypeService: BetTypeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.betTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
