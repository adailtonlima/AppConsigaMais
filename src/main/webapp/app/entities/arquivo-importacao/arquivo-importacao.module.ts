import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ArquivoImportacaoComponent } from './list/arquivo-importacao.component';
import { ArquivoImportacaoDetailComponent } from './detail/arquivo-importacao-detail.component';
import { ArquivoImportacaoUpdateComponent } from './update/arquivo-importacao-update.component';
import { ArquivoImportacaoDeleteDialogComponent } from './delete/arquivo-importacao-delete-dialog.component';
import { ArquivoImportacaoRoutingModule } from './route/arquivo-importacao-routing.module';

@NgModule({
  imports: [SharedModule, ArquivoImportacaoRoutingModule],
  declarations: [
    ArquivoImportacaoComponent,
    ArquivoImportacaoDetailComponent,
    ArquivoImportacaoUpdateComponent,
    ArquivoImportacaoDeleteDialogComponent,
  ],
  entryComponents: [ArquivoImportacaoDeleteDialogComponent],
})
export class ArquivoImportacaoModule {}
