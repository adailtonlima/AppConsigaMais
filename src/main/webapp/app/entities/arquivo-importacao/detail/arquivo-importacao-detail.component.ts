import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArquivoImportacao } from '../arquivo-importacao.model';

@Component({
  selector: 'jhi-arquivo-importacao-detail',
  templateUrl: './arquivo-importacao-detail.component.html',
})
export class ArquivoImportacaoDetailComponent implements OnInit {
  arquivoImportacao: IArquivoImportacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arquivoImportacao }) => {
      this.arquivoImportacao = arquivoImportacao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
