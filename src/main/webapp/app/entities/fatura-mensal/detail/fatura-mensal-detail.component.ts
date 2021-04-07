import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFaturaMensal } from '../fatura-mensal.model';

@Component({
  selector: 'jhi-fatura-mensal-detail',
  templateUrl: './fatura-mensal-detail.component.html',
})
export class FaturaMensalDetailComponent implements OnInit {
  faturaMensal: IFaturaMensal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ faturaMensal }) => {
      this.faturaMensal = faturaMensal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
