import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMensalidadePedido, MensalidadePedido } from '../mensalidade-pedido.model';
import { MensalidadePedidoService } from '../service/mensalidade-pedido.service';

@Injectable({ providedIn: 'root' })
export class MensalidadePedidoRoutingResolveService implements Resolve<IMensalidadePedido> {
  constructor(protected service: MensalidadePedidoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMensalidadePedido> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mensalidadePedido: HttpResponse<MensalidadePedido>) => {
          if (mensalidadePedido.body) {
            return of(mensalidadePedido.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MensalidadePedido());
  }
}
