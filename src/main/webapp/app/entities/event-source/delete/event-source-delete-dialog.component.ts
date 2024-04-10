import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEventSource } from '../event-source.model';
import { EventSourceService } from '../service/event-source.service';

@Component({
  standalone: true,
  templateUrl: './event-source-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EventSourceDeleteDialogComponent {
  eventSource?: IEventSource;

  constructor(
    protected eventSourceService: EventSourceService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventSourceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
