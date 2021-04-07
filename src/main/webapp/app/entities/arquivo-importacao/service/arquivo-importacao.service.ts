import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArquivoImportacao, getArquivoImportacaoIdentifier } from '../arquivo-importacao.model';

export type EntityResponseType = HttpResponse<IArquivoImportacao>;
export type EntityArrayResponseType = HttpResponse<IArquivoImportacao[]>;

@Injectable({ providedIn: 'root' })
export class ArquivoImportacaoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/arquivo-importacaos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(arquivoImportacao: IArquivoImportacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(arquivoImportacao);
    return this.http
      .post<IArquivoImportacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(arquivoImportacao: IArquivoImportacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(arquivoImportacao);
    return this.http
      .put<IArquivoImportacao>(`${this.resourceUrl}/${getArquivoImportacaoIdentifier(arquivoImportacao) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(arquivoImportacao: IArquivoImportacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(arquivoImportacao);
    return this.http
      .patch<IArquivoImportacao>(`${this.resourceUrl}/${getArquivoImportacaoIdentifier(arquivoImportacao) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IArquivoImportacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IArquivoImportacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addArquivoImportacaoToCollectionIfMissing(
    arquivoImportacaoCollection: IArquivoImportacao[],
    ...arquivoImportacaosToCheck: (IArquivoImportacao | null | undefined)[]
  ): IArquivoImportacao[] {
    const arquivoImportacaos: IArquivoImportacao[] = arquivoImportacaosToCheck.filter(isPresent);
    if (arquivoImportacaos.length > 0) {
      const arquivoImportacaoCollectionIdentifiers = arquivoImportacaoCollection.map(
        arquivoImportacaoItem => getArquivoImportacaoIdentifier(arquivoImportacaoItem)!
      );
      const arquivoImportacaosToAdd = arquivoImportacaos.filter(arquivoImportacaoItem => {
        const arquivoImportacaoIdentifier = getArquivoImportacaoIdentifier(arquivoImportacaoItem);
        if (arquivoImportacaoIdentifier == null || arquivoImportacaoCollectionIdentifiers.includes(arquivoImportacaoIdentifier)) {
          return false;
        }
        arquivoImportacaoCollectionIdentifiers.push(arquivoImportacaoIdentifier);
        return true;
      });
      return [...arquivoImportacaosToAdd, ...arquivoImportacaoCollection];
    }
    return arquivoImportacaoCollection;
  }

  protected convertDateFromClient(arquivoImportacao: IArquivoImportacao): IArquivoImportacao {
    return Object.assign({}, arquivoImportacao, {
      criado: arquivoImportacao.criado?.isValid() ? arquivoImportacao.criado.toJSON() : undefined,
      processado: arquivoImportacao.processado?.isValid() ? arquivoImportacao.processado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
      res.body.processado = res.body.processado ? dayjs(res.body.processado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((arquivoImportacao: IArquivoImportacao) => {
        arquivoImportacao.criado = arquivoImportacao.criado ? dayjs(arquivoImportacao.criado) : undefined;
        arquivoImportacao.processado = arquivoImportacao.processado ? dayjs(arquivoImportacao.processado) : undefined;
      });
    }
    return res;
  }
}
