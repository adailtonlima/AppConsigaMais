import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFaturaMensal, FaturaMensal } from '../fatura-mensal.model';
import { FaturaMensalService } from '../service/fatura-mensal.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';

@Component({
  selector: 'jhi-fatura-mensal-update',
  templateUrl: './fatura-mensal-update.component.html',
})
export class FaturaMensalUpdateComponent implements OnInit {
  isSaving = false;

  empresasSharedCollection: IEmpresa[] = [];
  filialsSharedCollection: IFilial[] = [];

  editForm = this.fb.group({
    id: [],
    mes: [],
    criado: [],
    boletoUrl: [],
    dataPago: [],
    empresa: [],
    filial: [],
  });

  constructor(
    protected faturaMensalService: FaturaMensalService,
    protected empresaService: EmpresaService,
    protected filialService: FilialService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ faturaMensal }) => {
      if (faturaMensal.id === undefined) {
        const today = dayjs().startOf('day');
        faturaMensal.criado = today;
        faturaMensal.dataPago = today;
      }

      this.updateForm(faturaMensal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const faturaMensal = this.createFromForm();
    if (faturaMensal.id !== undefined) {
      this.subscribeToSaveResponse(this.faturaMensalService.update(faturaMensal));
    } else {
      this.subscribeToSaveResponse(this.faturaMensalService.create(faturaMensal));
    }
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackFilialById(index: number, item: IFilial): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFaturaMensal>>): void {
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

  protected updateForm(faturaMensal: IFaturaMensal): void {
    this.editForm.patchValue({
      id: faturaMensal.id,
      mes: faturaMensal.mes,
      criado: faturaMensal.criado ? faturaMensal.criado.format(DATE_TIME_FORMAT) : null,
      boletoUrl: faturaMensal.boletoUrl,
      dataPago: faturaMensal.dataPago ? faturaMensal.dataPago.format(DATE_TIME_FORMAT) : null,
      empresa: faturaMensal.empresa,
      filial: faturaMensal.filial,
    });

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(
      this.empresasSharedCollection,
      faturaMensal.empresa
    );
    this.filialsSharedCollection = this.filialService.addFilialToCollectionIfMissing(this.filialsSharedCollection, faturaMensal.filial);
  }

  protected loadRelationshipsOptions(): void {
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
      .pipe(map((filials: IFilial[]) => this.filialService.addFilialToCollectionIfMissing(filials, this.editForm.get('filial')!.value)))
      .subscribe((filials: IFilial[]) => (this.filialsSharedCollection = filials));
  }

  protected createFromForm(): IFaturaMensal {
    return {
      ...new FaturaMensal(),
      id: this.editForm.get(['id'])!.value,
      mes: this.editForm.get(['mes'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      boletoUrl: this.editForm.get(['boletoUrl'])!.value,
      dataPago: this.editForm.get(['dataPago'])!.value ? dayjs(this.editForm.get(['dataPago'])!.value, DATE_TIME_FORMAT) : undefined,
      empresa: this.editForm.get(['empresa'])!.value,
      filial: this.editForm.get(['filial'])!.value,
    };
  }
}
