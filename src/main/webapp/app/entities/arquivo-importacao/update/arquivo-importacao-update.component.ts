import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IArquivoImportacao, ArquivoImportacao } from '../arquivo-importacao.model';
import { ArquivoImportacaoService } from '../service/arquivo-importacao.service';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';

@Component({
  selector: 'jhi-arquivo-importacao-update',
  templateUrl: './arquivo-importacao-update.component.html',
})
export class ArquivoImportacaoUpdateComponent implements OnInit {
  isSaving = false;

  administradorsSharedCollection: IAdministrador[] = [];
  empresasSharedCollection: IEmpresa[] = [];
  filialsSharedCollection: IFilial[] = [];

  editForm = this.fb.group({
    id: [],
    urlArquivo: [],
    urlCriticas: [],
    criado: [],
    estado: [],
    processado: [],
    criador: [],
    empresa: [],
    filial: [],
  });

  constructor(
    protected arquivoImportacaoService: ArquivoImportacaoService,
    protected administradorService: AdministradorService,
    protected empresaService: EmpresaService,
    protected filialService: FilialService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arquivoImportacao }) => {
      if (arquivoImportacao.id === undefined) {
        const today = dayjs().startOf('day');
        arquivoImportacao.criado = today;
        arquivoImportacao.processado = today;
      }

      this.updateForm(arquivoImportacao);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arquivoImportacao = this.createFromForm();
    if (arquivoImportacao.id !== undefined) {
      this.subscribeToSaveResponse(this.arquivoImportacaoService.update(arquivoImportacao));
    } else {
      this.subscribeToSaveResponse(this.arquivoImportacaoService.create(arquivoImportacao));
    }
  }

  trackAdministradorById(index: number, item: IAdministrador): number {
    return item.id!;
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackFilialById(index: number, item: IFilial): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArquivoImportacao>>): void {
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

  protected updateForm(arquivoImportacao: IArquivoImportacao): void {
    this.editForm.patchValue({
      id: arquivoImportacao.id,
      urlArquivo: arquivoImportacao.urlArquivo,
      urlCriticas: arquivoImportacao.urlCriticas,
      criado: arquivoImportacao.criado ? arquivoImportacao.criado.format(DATE_TIME_FORMAT) : null,
      estado: arquivoImportacao.estado,
      processado: arquivoImportacao.processado ? arquivoImportacao.processado.format(DATE_TIME_FORMAT) : null,
      criador: arquivoImportacao.criador,
      empresa: arquivoImportacao.empresa,
      filial: arquivoImportacao.filial,
    });

    this.administradorsSharedCollection = this.administradorService.addAdministradorToCollectionIfMissing(
      this.administradorsSharedCollection,
      arquivoImportacao.criador
    );
    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(
      this.empresasSharedCollection,
      arquivoImportacao.empresa
    );
    this.filialsSharedCollection = this.filialService.addFilialToCollectionIfMissing(
      this.filialsSharedCollection,
      arquivoImportacao.filial
    );
  }

  protected loadRelationshipsOptions(): void {
    this.administradorService
      .query()
      .pipe(map((res: HttpResponse<IAdministrador[]>) => res.body ?? []))
      .pipe(
        map((administradors: IAdministrador[]) =>
          this.administradorService.addAdministradorToCollectionIfMissing(administradors, this.editForm.get('criador')!.value)
        )
      )
      .subscribe((administradors: IAdministrador[]) => (this.administradorsSharedCollection = administradors));

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

  protected createFromForm(): IArquivoImportacao {
    return {
      ...new ArquivoImportacao(),
      id: this.editForm.get(['id'])!.value,
      urlArquivo: this.editForm.get(['urlArquivo'])!.value,
      urlCriticas: this.editForm.get(['urlCriticas'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      estado: this.editForm.get(['estado'])!.value,
      processado: this.editForm.get(['processado'])!.value ? dayjs(this.editForm.get(['processado'])!.value, DATE_TIME_FORMAT) : undefined,
      criador: this.editForm.get(['criador'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      filial: this.editForm.get(['filial'])!.value,
    };
  }
}
