import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMensalidadePedido, MensalidadePedido } from '../mensalidade-pedido.model';
import { MensalidadePedidoService } from '../service/mensalidade-pedido.service';
import { IPedido } from 'app/entities/pedido/pedido.model';
import { PedidoService } from 'app/entities/pedido/service/pedido.service';
import { IFaturaMensal } from 'app/entities/fatura-mensal/fatura-mensal.model';
import { FaturaMensalService } from 'app/entities/fatura-mensal/service/fatura-mensal.service';

@Component({
  selector: 'jhi-mensalidade-pedido-update',
  templateUrl: './mensalidade-pedido-update.component.html',
})
export class MensalidadePedidoUpdateComponent implements OnInit {
  isSaving = false;

  pedidosSharedCollection: IPedido[] = [];
  faturaMensalsSharedCollection: IFaturaMensal[] = [];

  editForm = this.fb.group({
    id: [],
    nParcela: [],
    valor: [],
    criado: [],
    valorParcial: [],
    pedido: [],
    fatura: [],
  });

  constructor(
    protected mensalidadePedidoService: MensalidadePedidoService,
    protected pedidoService: PedidoService,
    protected faturaMensalService: FaturaMensalService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mensalidadePedido }) => {
      if (mensalidadePedido.id === undefined) {
        const today = dayjs().startOf('day');
        mensalidadePedido.criado = today;
      }

      this.updateForm(mensalidadePedido);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mensalidadePedido = this.createFromForm();
    if (mensalidadePedido.id !== undefined) {
      this.subscribeToSaveResponse(this.mensalidadePedidoService.update(mensalidadePedido));
    } else {
      this.subscribeToSaveResponse(this.mensalidadePedidoService.create(mensalidadePedido));
    }
  }

  trackPedidoById(index: number, item: IPedido): number {
    return item.id!;
  }

  trackFaturaMensalById(index: number, item: IFaturaMensal): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMensalidadePedido>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mensalidadePedido: IMensalidadePedido): void {
    this.editForm.patchValue({
      id: mensalidadePedido.id,
      nParcela: mensalidadePedido.nParcela,
      valor: mensalidadePedido.valor,
      criado: mensalidadePedido.criado ? mensalidadePedido.criado.format(DATE_TIME_FORMAT) : null,
      valorParcial: mensalidadePedido.valorParcial,
      pedido: mensalidadePedido.pedido,
      fatura: mensalidadePedido.fatura,
    });

    this.pedidosSharedCollection = this.pedidoService.addPedidoToCollectionIfMissing(
      this.pedidosSharedCollection,
      mensalidadePedido.pedido
    );
    this.faturaMensalsSharedCollection = this.faturaMensalService.addFaturaMensalToCollectionIfMissing(
      this.faturaMensalsSharedCollection,
      mensalidadePedido.fatura
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pedidoService
      .query()
      .pipe(map((res: HttpResponse<IPedido[]>) => res.body ?? []))
      .pipe(map((pedidos: IPedido[]) => this.pedidoService.addPedidoToCollectionIfMissing(pedidos, this.editForm.get('pedido')!.value)))
      .subscribe((pedidos: IPedido[]) => (this.pedidosSharedCollection = pedidos));

    this.faturaMensalService
      .query()
      .pipe(map((res: HttpResponse<IFaturaMensal[]>) => res.body ?? []))
      .pipe(
        map((faturaMensals: IFaturaMensal[]) =>
          this.faturaMensalService.addFaturaMensalToCollectionIfMissing(faturaMensals, this.editForm.get('fatura')!.value)
        )
      )
      .subscribe((faturaMensals: IFaturaMensal[]) => (this.faturaMensalsSharedCollection = faturaMensals));
  }

  protected createFromForm(): IMensalidadePedido {
    return {
      ...new MensalidadePedido(),
      id: this.editForm.get(['id'])!.value,
      nParcela: this.editForm.get(['nParcela'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      valorParcial: this.editForm.get(['valorParcial'])!.value,
      pedido: this.editForm.get(['pedido'])!.value,
      fatura: this.editForm.get(['fatura'])!.value,
    };
  }
}
