import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FuncionarioComponent } from './list/funcionario.component';
import { FuncionarioDetailComponent } from './detail/funcionario-detail.component';
import { FuncionarioUpdateComponent } from './update/funcionario-update.component';
import { FuncionarioDeleteDialogComponent } from './delete/funcionario-delete-dialog.component';
import { FuncionarioRoutingModule } from './route/funcionario-routing.module';

@NgModule({
  imports: [SharedModule, FuncionarioRoutingModule],
  declarations: [FuncionarioComponent, FuncionarioDetailComponent, FuncionarioUpdateComponent, FuncionarioDeleteDialogComponent],
  entryComponents: [FuncionarioDeleteDialogComponent],
})
export class FuncionarioModule {}
