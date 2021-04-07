import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuncionario, getFuncionarioIdentifier } from '../funcionario.model';

export type EntityResponseType = HttpResponse<IFuncionario>;
export type EntityArrayResponseType = HttpResponse<IFuncionario[]>;

@Injectable({ providedIn: 'root' })
export class FuncionarioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/funcionarios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(funcionario: IFuncionario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionario);
    return this.http
      .post<IFuncionario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(funcionario: IFuncionario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionario);
    return this.http
      .put<IFuncionario>(`${this.resourceUrl}/${getFuncionarioIdentifier(funcionario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(funcionario: IFuncionario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionario);
    return this.http
      .patch<IFuncionario>(`${this.resourceUrl}/${getFuncionarioIdentifier(funcionario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFuncionario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFuncionario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFuncionarioToCollectionIfMissing(
    funcionarioCollection: IFuncionario[],
    ...funcionariosToCheck: (IFuncionario | null | undefined)[]
  ): IFuncionario[] {
    const funcionarios: IFuncionario[] = funcionariosToCheck.filter(isPresent);
    if (funcionarios.length > 0) {
      const funcionarioCollectionIdentifiers = funcionarioCollection.map(funcionarioItem => getFuncionarioIdentifier(funcionarioItem)!);
      const funcionariosToAdd = funcionarios.filter(funcionarioItem => {
        const funcionarioIdentifier = getFuncionarioIdentifier(funcionarioItem);
        if (funcionarioIdentifier == null || funcionarioCollectionIdentifiers.includes(funcionarioIdentifier)) {
          return false;
        }
        funcionarioCollectionIdentifiers.push(funcionarioIdentifier);
        return true;
      });
      return [...funcionariosToAdd, ...funcionarioCollection];
    }
    return funcionarioCollection;
  }

  protected convertDateFromClient(funcionario: IFuncionario): IFuncionario {
    return Object.assign({}, funcionario, {
      criado: funcionario.criado?.isValid() ? funcionario.criado.toJSON() : undefined,
      atualizado: funcionario.atualizado?.isValid() ? funcionario.atualizado.toJSON() : undefined,
      ultimoLogin: funcionario.ultimoLogin?.isValid() ? funcionario.ultimoLogin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
      res.body.atualizado = res.body.atualizado ? dayjs(res.body.atualizado) : undefined;
      res.body.ultimoLogin = res.body.ultimoLogin ? dayjs(res.body.ultimoLogin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((funcionario: IFuncionario) => {
        funcionario.criado = funcionario.criado ? dayjs(funcionario.criado) : undefined;
        funcionario.atualizado = funcionario.atualizado ? dayjs(funcionario.atualizado) : undefined;
        funcionario.ultimoLogin = funcionario.ultimoLogin ? dayjs(funcionario.ultimoLogin) : undefined;
      });
    }
    return res;
  }
}
