import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArquivoImportacao } from '../arquivo-importacao.model';
import { ArquivoImportacaoService } from '../service/arquivo-importacao.service';

@Component({
  templateUrl: './arquivo-importacao-delete-dialog.component.html',
})
export class ArquivoImportacaoDeleteDialogComponent {
  arquivoImportacao?: IArquivoImportacao;

  constructor(protected arquivoImportacaoService: ArquivoImportacaoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arquivoImportacaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
