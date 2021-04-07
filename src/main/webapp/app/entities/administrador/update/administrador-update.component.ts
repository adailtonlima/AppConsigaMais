import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAdministrador, Administrador } from '../administrador.model';
import { AdministradorService } from '../service/administrador.service';

@Component({
  selector: 'jhi-administrador-update',
  templateUrl: './administrador-update.component.html',
})
export class AdministradorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    criado: [],
    atualizado: [],
    ultimoLogin: [],
  });

  constructor(protected administradorService: AdministradorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ administrador }) => {
      if (administrador.id === undefined) {
        const today = dayjs().startOf('day');
        administrador.criado = today;
        administrador.atualizado = today;
        administrador.ultimoLogin = today;
      }

      this.updateForm(administrador);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const administrador = this.createFromForm();
    if (administrador.id !== undefined) {
      this.subscribeToSaveResponse(this.administradorService.update(administrador));
    } else {
      this.subscribeToSaveResponse(this.administradorService.create(administrador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdministrador>>): void {
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

  protected updateForm(administrador: IAdministrador): void {
    this.editForm.patchValue({
      id: administrador.id,
      nome: administrador.nome,
      criado: administrador.criado ? administrador.criado.format(DATE_TIME_FORMAT) : null,
      atualizado: administrador.atualizado ? administrador.atualizado.format(DATE_TIME_FORMAT) : null,
      ultimoLogin: administrador.ultimoLogin ? administrador.ultimoLogin.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IAdministrador {
    return {
      ...new Administrador(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      atualizado: this.editForm.get(['atualizado'])!.value ? dayjs(this.editForm.get(['atualizado'])!.value, DATE_TIME_FORMAT) : undefined,
      ultimoLogin: this.editForm.get(['ultimoLogin'])!.value
        ? dayjs(this.editForm.get(['ultimoLogin'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
