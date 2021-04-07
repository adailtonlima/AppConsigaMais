import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFuncionario, Funcionario } from '../funcionario.model';
import { FuncionarioService } from '../service/funcionario.service';

@Injectable({ providedIn: 'root' })
export class FuncionarioRoutingResolveService implements Resolve<IFuncionario> {
  constructor(protected service: FuncionarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFuncionario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((funcionario: HttpResponse<Funcionario>) => {
          if (funcionario.body) {
            return of(funcionario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Funcionario());
  }
}
