import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFuncionario, Funcionario } from '../funcionario.model';
import { FuncionarioService } from '../service/funcionario.service';

@Component({
  selector: 'jhi-funcionario-update',
  templateUrl: './funcionario-update.component.html',
})
export class FuncionarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    cpf: [null, [Validators.required]],
    criado: [],
    atualizado: [],
    ultimoLogin: [],
  });

  constructor(protected funcionarioService: FuncionarioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionario }) => {
      if (funcionario.id === undefined) {
        const today = dayjs().startOf('day');
        funcionario.criado = today;
        funcionario.atualizado = today;
        funcionario.ultimoLogin = today;
      }

      this.updateForm(funcionario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const funcionario = this.createFromForm();
    if (funcionario.id !== undefined) {
      this.subscribeToSaveResponse(this.funcionarioService.update(funcionario));
    } else {
      this.subscribeToSaveResponse(this.funcionarioService.create(funcionario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuncionario>>): void {
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

  protected updateForm(funcionario: IFuncionario): void {
    this.editForm.patchValue({
      id: funcionario.id,
      nome: funcionario.nome,
      cpf: funcionario.cpf,
      criado: funcionario.criado ? funcionario.criado.format(DATE_TIME_FORMAT) : null,
      atualizado: funcionario.atualizado ? funcionario.atualizado.format(DATE_TIME_FORMAT) : null,
      ultimoLogin: funcionario.ultimoLogin ? funcionario.ultimoLogin.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IFuncionario {
    return {
      ...new Funcionario(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      atualizado: this.editForm.get(['atualizado'])!.value ? dayjs(this.editForm.get(['atualizado'])!.value, DATE_TIME_FORMAT) : undefined,
      ultimoLogin: this.editForm.get(['ultimoLogin'])!.value
        ? dayjs(this.editForm.get(['ultimoLogin'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
