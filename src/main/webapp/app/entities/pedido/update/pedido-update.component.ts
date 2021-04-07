import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPedido, Pedido } from '../pedido.model';
import { PedidoService } from '../service/pedido.service';
import { IFuncionario } from 'app/entities/funcionario/funcionario.model';
import { FuncionarioService } from 'app/entities/funcionario/service/funcionario.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

@Component({
  selector: 'jhi-pedido-update',
  templateUrl: './pedido-update.component.html',
})
export class PedidoUpdateComponent implements OnInit {
  isSaving = false;

  funcionariosSharedCollection: IFuncionario[] = [];
  empresasSharedCollection: IEmpresa[] = [];
  filialsSharedCollection: IFilial[] = [];
  administradorsSharedCollection: IAdministrador[] = [];

  editForm = this.fb.group({
    id: [],
    estado: [],
    criado: [],
    dataAprovacao: [],
    dataExpiracao: [],
    renda: [],
    valorSolicitado: [],
    qtParcelasSolicitado: [],
    valorAprovado: [],
    valorParcelaAprovado: [],
    qtParcelasAprovado: [],
    dataPrimeiroVencimento: [],
    dataUltimoVencimento: [],
    funcionario: [],
    empresa: [],
    filia: [],
    quemAprovou: [],
  });

  constructor(
    protected pedidoService: PedidoService,
    protected funcionarioService: FuncionarioService,
    protected empresaService: EmpresaService,
    protected filialService: FilialService,
    protected administradorService: AdministradorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedido }) => {
      if (pedido.id === undefined) {
        const today = dayjs().startOf('day');
        pedido.criado = today;
        pedido.dataAprovacao = today;
      }

      this.updateForm(pedido);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pedido = this.createFromForm();
    if (pedido.id !== undefined) {
      this.subscribeToSaveResponse(this.pedidoService.update(pedido));
    } else {
      this.subscribeToSaveResponse(this.pedidoService.create(pedido));
    }
  }

  trackFuncionarioById(index: number, item: IFuncionario): number {
    return item.id!;
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackFilialById(index: number, item: IFilial): number {
    return item.id!;
  }

  trackAdministradorById(index: number, item: IAdministrador): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedido>>): void {
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

  protected updateForm(pedido: IPedido): void {
    this.editForm.patchValue({
      id: pedido.id,
      estado: pedido.estado,
      criado: pedido.criado ? pedido.criado.format(DATE_TIME_FORMAT) : null,
      dataAprovacao: pedido.dataAprovacao ? pedido.dataAprovacao.format(DATE_TIME_FORMAT) : null,
      dataExpiracao: pedido.dataExpiracao,
      renda: pedido.renda,
      valorSolicitado: pedido.valorSolicitado,
      qtParcelasSolicitado: pedido.qtParcelasSolicitado,
      valorAprovado: pedido.valorAprovado,
      valorParcelaAprovado: pedido.valorParcelaAprovado,
      qtParcelasAprovado: pedido.qtParcelasAprovado,
      dataPrimeiroVencimento: pedido.dataPrimeiroVencimento,
      dataUltimoVencimento: pedido.dataUltimoVencimento,
      funcionario: pedido.funcionario,
      empresa: pedido.empresa,
      filia: pedido.filia,
      quemAprovou: pedido.quemAprovou,
    });

    this.funcionariosSharedCollection = this.funcionarioService.addFuncionarioToCollectionIfMissing(
      this.funcionariosSharedCollection,
      pedido.funcionario
    );
    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, pedido.empresa);
    this.filialsSharedCollection = this.filialService.addFilialToCollectionIfMissing(this.filialsSharedCollection, pedido.filia);
    this.administradorsSharedCollection = this.administradorService.addAdministradorToCollectionIfMissing(
      this.administradorsSharedCollection,
      pedido.quemAprovou
    );
  }

  protected loadRelationshipsOptions(): void {
    this.funcionarioService
      .query()
      .pipe(map((res: HttpResponse<IFuncionario[]>) => res.body ?? []))
      .pipe(
        map((funcionarios: IFuncionario[]) =>
          this.funcionarioService.addFuncionarioToCollectionIfMissing(funcionarios, this.editForm.get('funcionario')!.value)
        )
      )
      .subscribe((funcionarios: IFuncionario[]) => (this.funcionariosSharedCollection = funcionarios));

    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));

    this.filialService
      .query()
      .pipe(map((res: HttpResponse<IFilial[]>) => res.body ?? []))
      .pipe(map((filials: IFilial[]) => this.filialService.addFilialToCollectionIfMissing(filials, this.editForm.get('filia')!.value)))
      .subscribe((filials: IFilial[]) => (this.filialsSharedCollection = filials));

    this.administradorService
      .query()
      .pipe(map((res: HttpResponse<IAdministrador[]>) => res.body ?? []))
      .pipe(
        map((administradors: IAdministrador[]) =>
          this.administradorService.addAdministradorToCollectionIfMissing(administradors, this.editForm.get('quemAprovou')!.value)
        )
      )
      .subscribe((administradors: IAdministrador[]) => (this.administradorsSharedCollection = administradors));
  }

  protected createFromForm(): IPedido {
    return {
      ...new Pedido(),
      id: this.editForm.get(['id'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      dataAprovacao: this.editForm.get(['dataAprovacao'])!.value
        ? dayjs(this.editForm.get(['dataAprovacao'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dataExpiracao: this.editForm.get(['dataExpiracao'])!.value,
      renda: this.editForm.get(['renda'])!.value,
      valorSolicitado: this.editForm.get(['valorSolicitado'])!.value,
      qtParcelasSolicitado: this.editForm.get(['qtParcelasSolicitado'])!.value,
      valorAprovado: this.editForm.get(['valorAprovado'])!.value,
      valorParcelaAprovado: this.editForm.get(['valorParcelaAprovado'])!.value,
      qtParcelasAprovado: this.editForm.get(['qtParcelasAprovado'])!.value,
      dataPrimeiroVencimento: this.editForm.get(['dataPrimeiroVencimento'])!.value,
      dataUltimoVencimento: this.editForm.get(['dataUltimoVencimento'])!.value,
      funcionario: this.editForm.get(['funcionario'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      filia: this.editForm.get(['filia'])!.value,
      quemAprovou: this.editForm.get(['quemAprovou'])!.value,
    };
  }
}
