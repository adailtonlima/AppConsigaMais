import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FuncionarioComponent } from '../list/funcionario.component';
import { FuncionarioDetailComponent } from '../detail/funcionario-detail.component';
import { FuncionarioUpdateComponent } from '../update/funcionario-update.component';
import { FuncionarioRoutingResolveService } from './funcionario-routing-resolve.service';

const funcionarioRoute: Routes = [
  {
    path: '',
    component: FuncionarioComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FuncionarioDetailComponent,
    resolve: {
      funcionario: FuncionarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FuncionarioUpdateComponent,
    resolve: {
      funcionario: FuncionarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FuncionarioUpdateComponent,
    resolve: {
      funcionario: FuncionarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(funcionarioRoute)],
  exports: [RouterModule],
})
export class FuncionarioRoutingModule {}
