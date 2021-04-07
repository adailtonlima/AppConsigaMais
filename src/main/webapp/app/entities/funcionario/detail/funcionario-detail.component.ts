import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFuncionario } from '../funcionario.model';

@Component({
  selector: 'jhi-funcionario-detail',
  templateUrl: './funcionario-detail.component.html',
})
export class FuncionarioDetailComponent implements OnInit {
  funcionario: IFuncionario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionario }) => {
      this.funcionario = funcionario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
