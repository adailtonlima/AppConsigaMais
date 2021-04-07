import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMensalidadePedido } from '../mensalidade-pedido.model';
import { MensalidadePedidoService } from '../service/mensalidade-pedido.service';

@Component({
  templateUrl: './mensalidade-pedido-delete-dialog.component.html',
})
export class MensalidadePedidoDeleteDialogComponent {
  mensalidadePedido?: IMensalidadePedido;

  constructor(protected mensalidadePedidoService: MensalidadePedidoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mensalidadePedidoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
