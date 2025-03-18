import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';


/**
 * Application wide Module
 */
@NgModule({
  imports: [AlertComponent, AlertErrorComponent,NgxMaskDirective, NgxMaskPipe],
  exports: [CommonModule, NgbModule, FontAwesomeModule, AlertComponent, AlertErrorComponent, NgxMaskDirective, NgxMaskPipe,],
  providers: [
    provideNgxMask({
      validation: true,
    }),
  ],
})
export default class SharedModule {}
