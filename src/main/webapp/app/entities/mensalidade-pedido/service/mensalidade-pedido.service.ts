import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMensalidadePedido, getMensalidadePedidoIdentifier } from '../mensalidade-pedido.model';

export type EntityResponseType = HttpResponse<IMensalidadePedido>;
export type EntityArrayResponseType = HttpResponse<IMensalidadePedido[]>;

@Injectable({ providedIn: 'root' })
export class MensalidadePedidoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/mensalidade-pedidos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(mensalidadePedido: IMensalidadePedido): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mensalidadePedido);
    return this.http
      .post<IMensalidadePedido>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mensalidadePedido: IMensalidadePedido): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mensalidadePedido);
    return this.http
      .put<IMensalidadePedido>(`${this.resourceUrl}/${getMensalidadePedidoIdentifier(mensalidadePedido) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mensalidadePedido: IMensalidadePedido): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mensalidadePedido);
    return this.http
      .patch<IMensalidadePedido>(`${this.resourceUrl}/${getMensalidadePedidoIdentifier(mensalidadePedido) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMensalidadePedido>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMensalidadePedido[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMensalidadePedidoToCollectionIfMissing(
    mensalidadePedidoCollection: IMensalidadePedido[],
    ...mensalidadePedidosToCheck: (IMensalidadePedido | null | undefined)[]
  ): IMensalidadePedido[] {
    const mensalidadePedidos: IMensalidadePedido[] = mensalidadePedidosToCheck.filter(isPresent);
    if (mensalidadePedidos.length > 0) {
      const mensalidadePedidoCollectionIdentifiers = mensalidadePedidoCollection.map(
        mensalidadePedidoItem => getMensalidadePedidoIdentifier(mensalidadePedidoItem)!
      );
      const mensalidadePedidosToAdd = mensalidadePedidos.filter(mensalidadePedidoItem => {
        const mensalidadePedidoIdentifier = getMensalidadePedidoIdentifier(mensalidadePedidoItem);
        if (mensalidadePedidoIdentifier == null || mensalidadePedidoCollectionIdentifiers.includes(mensalidadePedidoIdentifier)) {
          return false;
        }
        mensalidadePedidoCollectionIdentifiers.push(mensalidadePedidoIdentifier);
        return true;
      });
      return [...mensalidadePedidosToAdd, ...mensalidadePedidoCollection];
    }
    return mensalidadePedidoCollection;
  }

  protected convertDateFromClient(mensalidadePedido: IMensalidadePedido): IMensalidadePedido {
    return Object.assign({}, mensalidadePedido, {
      criado: mensalidadePedido.criado?.isValid() ? mensalidadePedido.criado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((mensalidadePedido: IMensalidadePedido) => {
        mensalidadePedido.criado = mensalidadePedido.criado ? dayjs(mensalidadePedido.criado) : undefined;
      });
    }
    return res;
  }
}
