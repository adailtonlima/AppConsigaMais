import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFilial, Filial } from '../filial.model';
import { FilialService } from '../service/filial.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

@Component({
  selector: 'jhi-filial-update',
  templateUrl: './filial-update.component.html',
})
export class FilialUpdateComponent implements OnInit {
  isSaving = false;

  empresasSharedCollection: IEmpresa[] = [];
  administradorsSharedCollection: IAdministrador[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    codigo: [],
    cnpj: [],
    empresa: [],
    administradores: [],
  });

  constructor(
    protected filialService: FilialService,
    protected empresaService: EmpresaService,
    protected administradorService: AdministradorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filial }) => {
      this.updateForm(filial);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filial = this.createFromForm();
    if (filial.id !== undefined) {
      this.subscribeToSaveResponse(this.filialService.update(filial));
    } else {
      this.subscribeToSaveResponse(this.filialService.create(filial));
    }
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackAdministradorById(index: number, item: IAdministrador): number {
    return item.id!;
  }

  getSelectedAdministrador(option: IAdministrador, selectedVals?: IAdministrador[]): IAdministrador {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFilial>>): void {
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

  protected updateForm(filial: IFilial): void {
    this.editForm.patchValue({
      id: filial.id,
      nome: filial.nome,
      codigo: filial.codigo,
      cnpj: filial.cnpj,
      empresa: filial.empresa,
      administradores: filial.administradores,
    });

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, filial.empresa);
    this.administradorsSharedCollection = this.administradorService.addAdministradorToCollectionIfMissing(
      this.administradorsSharedCollection,
      ...(filial.administradores ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));

    this.administradorService
      .query()
      .pipe(map((res: HttpResponse<IAdministrador[]>) => res.body ?? []))
      .pipe(
        map((administradors: IAdministrador[]) =>
          this.administradorService.addAdministradorToCollectionIfMissing(
            administradors,
            ...(this.editForm.get('administradores')!.value ?? [])
          )
        )
      )
      .subscribe((administradors: IAdministrador[]) => (this.administradorsSharedCollection = administradors));
  }

  protected createFromForm(): IFilial {
    return {
      ...new Filial(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      cnpj: this.editForm.get(['cnpj'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      administradores: this.editForm.get(['administradores'])!.value,
    };
  }
}
