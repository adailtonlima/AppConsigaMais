import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFuncionario } from '../funcionario.model';
import { FuncionarioService } from '../service/funcionario.service';

@Component({
  templateUrl: './funcionario-delete-dialog.component.html',
})
export class FuncionarioDeleteDialogComponent {
  funcionario?: IFuncionario;

  constructor(protected funcionarioService: FuncionarioService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.funcionarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
