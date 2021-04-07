import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FaturaMensalComponent } from './list/fatura-mensal.component';
import { FaturaMensalDetailComponent } from './detail/fatura-mensal-detail.component';
import { FaturaMensalUpdateComponent } from './update/fatura-mensal-update.component';
import { FaturaMensalDeleteDialogComponent } from './delete/fatura-mensal-delete-dialog.component';
import { FaturaMensalRoutingModule } from './route/fatura-mensal-routing.module';

@NgModule({
  imports: [SharedModule, FaturaMensalRoutingModule],
  declarations: [FaturaMensalComponent, FaturaMensalDetailComponent, FaturaMensalUpdateComponent, FaturaMensalDeleteDialogComponent],
  entryComponents: [FaturaMensalDeleteDialogComponent],
})
export class FaturaMensalModule {}
