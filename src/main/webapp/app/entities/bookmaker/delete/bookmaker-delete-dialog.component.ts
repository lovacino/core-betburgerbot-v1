import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBookmaker } from '../bookmaker.model';
import { BookmakerService } from '../service/bookmaker.service';

@Component({
  standalone: true,
  templateUrl: './bookmaker-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BookmakerDeleteDialogComponent {
  bookmaker?: IBookmaker;

  constructor(
    protected bookmakerService: BookmakerService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookmakerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
