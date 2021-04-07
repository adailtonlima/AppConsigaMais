import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFaturaMensal } from '../fatura-mensal.model';
import { FaturaMensalService } from '../service/fatura-mensal.service';

@Component({
  templateUrl: './fatura-mensal-delete-dialog.component.html',
})
export class FaturaMensalDeleteDialogComponent {
  faturaMensal?: IFaturaMensal;

  constructor(protected faturaMensalService: FaturaMensalService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.faturaMensalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
