import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFaturaMensal, FaturaMensal } from '../fatura-mensal.model';
import { FaturaMensalService } from '../service/fatura-mensal.service';

@Injectable({ providedIn: 'root' })
export class FaturaMensalRoutingResolveService implements Resolve<IFaturaMensal> {
  constructor(protected service: FaturaMensalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFaturaMensal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((faturaMensal: HttpResponse<FaturaMensal>) => {
          if (faturaMensal.body) {
            return of(faturaMensal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FaturaMensal());
  }
}
