import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMensalidadePedido } from '../mensalidade-pedido.model';

@Component({
  selector: 'jhi-mensalidade-pedido-detail',
  templateUrl: './mensalidade-pedido-detail.component.html',
})
export class MensalidadePedidoDetailComponent implements OnInit {
  mensalidadePedido: IMensalidadePedido | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mensalidadePedido }) => {
      this.mensalidadePedido = mensalidadePedido;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
