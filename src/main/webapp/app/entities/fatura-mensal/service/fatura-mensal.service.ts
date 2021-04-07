import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFaturaMensal, getFaturaMensalIdentifier } from '../fatura-mensal.model';

export type EntityResponseType = HttpResponse<IFaturaMensal>;
export type EntityArrayResponseType = HttpResponse<IFaturaMensal[]>;

@Injectable({ providedIn: 'root' })
export class FaturaMensalService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fatura-mensals');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(faturaMensal: IFaturaMensal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faturaMensal);
    return this.http
      .post<IFaturaMensal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(faturaMensal: IFaturaMensal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faturaMensal);
    return this.http
      .put<IFaturaMensal>(`${this.resourceUrl}/${getFaturaMensalIdentifier(faturaMensal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(faturaMensal: IFaturaMensal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faturaMensal);
    return this.http
      .patch<IFaturaMensal>(`${this.resourceUrl}/${getFaturaMensalIdentifier(faturaMensal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFaturaMensal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFaturaMensal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFaturaMensalToCollectionIfMissing(
    faturaMensalCollection: IFaturaMensal[],
    ...faturaMensalsToCheck: (IFaturaMensal | null | undefined)[]
  ): IFaturaMensal[] {
    const faturaMensals: IFaturaMensal[] = faturaMensalsToCheck.filter(isPresent);
    if (faturaMensals.length > 0) {
      const faturaMensalCollectionIdentifiers = faturaMensalCollection.map(
        faturaMensalItem => getFaturaMensalIdentifier(faturaMensalItem)!
      );
      const faturaMensalsToAdd = faturaMensals.filter(faturaMensalItem => {
        const faturaMensalIdentifier = getFaturaMensalIdentifier(faturaMensalItem);
        if (faturaMensalIdentifier == null || faturaMensalCollectionIdentifiers.includes(faturaMensalIdentifier)) {
          return false;
        }
        faturaMensalCollectionIdentifiers.push(faturaMensalIdentifier);
        return true;
      });
      return [...faturaMensalsToAdd, ...faturaMensalCollection];
    }
    return faturaMensalCollection;
  }

  protected convertDateFromClient(faturaMensal: IFaturaMensal): IFaturaMensal {
    return Object.assign({}, faturaMensal, {
      mes: faturaMensal.mes?.isValid() ? faturaMensal.mes.format(DATE_FORMAT) : undefined,
      criado: faturaMensal.criado?.isValid() ? faturaMensal.criado.toJSON() : undefined,
      dataPago: faturaMensal.dataPago?.isValid() ? faturaMensal.dataPago.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.mes = res.body.mes ? dayjs(res.body.mes) : undefined;
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
      res.body.dataPago = res.body.dataPago ? dayjs(res.body.dataPago) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((faturaMensal: IFaturaMensal) => {
        faturaMensal.mes = faturaMensal.mes ? dayjs(faturaMensal.mes) : undefined;
        faturaMensal.criado = faturaMensal.criado ? dayjs(faturaMensal.criado) : undefined;
        faturaMensal.dataPago = faturaMensal.dataPago ? dayjs(faturaMensal.dataPago) : undefined;
      });
    }
    return res;
  }
}
