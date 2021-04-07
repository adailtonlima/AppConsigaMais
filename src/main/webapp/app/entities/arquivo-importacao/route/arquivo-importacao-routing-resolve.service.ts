import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArquivoImportacao, ArquivoImportacao } from '../arquivo-importacao.model';
import { ArquivoImportacaoService } from '../service/arquivo-importacao.service';

@Injectable({ providedIn: 'root' })
export class ArquivoImportacaoRoutingResolveService implements Resolve<IArquivoImportacao> {
  constructor(protected service: ArquivoImportacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArquivoImportacao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((arquivoImportacao: HttpResponse<ArquivoImportacao>) => {
          if (arquivoImportacao.body) {
            return of(arquivoImportacao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ArquivoImportacao());
  }
}
