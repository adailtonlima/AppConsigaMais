import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MensalidadePedidoComponent } from './list/mensalidade-pedido.component';
import { MensalidadePedidoDetailComponent } from './detail/mensalidade-pedido-detail.component';
import { MensalidadePedidoUpdateComponent } from './update/mensalidade-pedido-update.component';
import { MensalidadePedidoDeleteDialogComponent } from './delete/mensalidade-pedido-delete-dialog.component';
import { MensalidadePedidoRoutingModule } from './route/mensalidade-pedido-routing.module';

@NgModule({
  imports: [SharedModule, MensalidadePedidoRoutingModule],
  declarations: [
    MensalidadePedidoComponent,
    MensalidadePedidoDetailComponent,
    MensalidadePedidoUpdateComponent,
    MensalidadePedidoDeleteDialogComponent,
  ],
  entryComponents: [MensalidadePedidoDeleteDialogComponent],
})
export class MensalidadePedidoModule {}
