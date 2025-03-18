import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IProcesso } from '../processo.model';
import { NpuPipe } from 'app/shared/processos/npu.pipe';

@Component({
  selector: 'jhi-processo-detail',
  templateUrl: './processo-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe, NpuPipe],
})
export class ProcessoDetailComponent {
  processo = input<IProcesso | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  openPDF(base64String: string | null | undefined): void {
    if(base64String){
      this.dataUtils.openFile(base64String , 'application/pdf');
    }
   
  }
}
