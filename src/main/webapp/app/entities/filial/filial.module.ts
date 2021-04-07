import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FilialComponent } from './list/filial.component';
import { FilialDetailComponent } from './detail/filial-detail.component';
import { FilialUpdateComponent } from './update/filial-update.component';
import { FilialDeleteDialogComponent } from './delete/filial-delete-dialog.component';
import { FilialRoutingModule } from './route/filial-routing.module';

@NgModule({
  imports: [SharedModule, FilialRoutingModule],
  declarations: [FilialComponent, FilialDetailComponent, FilialUpdateComponent, FilialDeleteDialogComponent],
  entryComponents: [FilialDeleteDialogComponent],
})
export class FilialModule {}
