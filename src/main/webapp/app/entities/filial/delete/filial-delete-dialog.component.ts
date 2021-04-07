import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFilial } from '../filial.model';
import { FilialService } from '../service/filial.service';

@Component({
  templateUrl: './filial-delete-dialog.component.html',
})
export class FilialDeleteDialogComponent {
  filial?: IFilial;

  constructor(protected filialService: FilialService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.filialService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
