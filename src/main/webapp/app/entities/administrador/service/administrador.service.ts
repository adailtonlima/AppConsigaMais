import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdministrador, getAdministradorIdentifier } from '../administrador.model';

export type EntityResponseType = HttpResponse<IAdministrador>;
export type EntityArrayResponseType = HttpResponse<IAdministrador[]>;

@Injectable({ providedIn: 'root' })
export class AdministradorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/administradors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(administrador: IAdministrador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(administrador);
    return this.http
      .post<IAdministrador>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(administrador: IAdministrador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(administrador);
    return this.http
      .put<IAdministrador>(`${this.resourceUrl}/${getAdministradorIdentifier(administrador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(administrador: IAdministrador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(administrador);
    return this.http
      .patch<IAdministrador>(`${this.resourceUrl}/${getAdministradorIdentifier(administrador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAdministrador>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAdministrador[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdministradorToCollectionIfMissing(
    administradorCollection: IAdministrador[],
    ...administradorsToCheck: (IAdministrador | null | undefined)[]
  ): IAdministrador[] {
    const administradors: IAdministrador[] = administradorsToCheck.filter(isPresent);
    if (administradors.length > 0) {
      const administradorCollectionIdentifiers = administradorCollection.map(
        administradorItem => getAdministradorIdentifier(administradorItem)!
      );
      const administradorsToAdd = administradors.filter(administradorItem => {
        const administradorIdentifier = getAdministradorIdentifier(administradorItem);
        if (administradorIdentifier == null || administradorCollectionIdentifiers.includes(administradorIdentifier)) {
          return false;
        }
        administradorCollectionIdentifiers.push(administradorIdentifier);
        return true;
      });
      return [...administradorsToAdd, ...administradorCollection];
    }
    return administradorCollection;
  }

  protected convertDateFromClient(administrador: IAdministrador): IAdministrador {
    return Object.assign({}, administrador, {
      criado: administrador.criado?.isValid() ? administrador.criado.toJSON() : undefined,
      atualizado: administrador.atualizado?.isValid() ? administrador.atualizado.toJSON() : undefined,
      ultimoLogin: administrador.ultimoLogin?.isValid() ? administrador.ultimoLogin.toJSON() : undefined,
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
      res.body.forEach((administrador: IAdministrador) => {
        administrador.criado = administrador.criado ? dayjs(administrador.criado) : undefined;
        administrador.atualizado = administrador.atualizado ? dayjs(administrador.atualizado) : undefined;
        administrador.ultimoLogin = administrador.ultimoLogin ? dayjs(administrador.ultimoLogin) : undefined;
      });
    }
    return res;
  }
}
