import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProcesso } from '../processo.model';
import { ProcessoService } from '../service/processo.service';
import { NpuPipe } from 'app/shared/processos/npu.pipe';

@Component({
  templateUrl: './processo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule, NpuPipe],
})
export class ProcessoDeleteDialogComponent {
  processo?: IProcesso;

  protected processoService = inject(ProcessoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.processoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
