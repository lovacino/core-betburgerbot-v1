import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConfigParam } from '../config-param.model';
import { ConfigParamService } from '../service/config-param.service';

@Component({
  standalone: true,
  templateUrl: './config-param-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConfigParamDeleteDialogComponent {
  configParam?: IConfigParam;

  constructor(
    protected configParamService: ConfigParamService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configParamService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
