import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FaturaMensalComponent } from '../list/fatura-mensal.component';
import { FaturaMensalDetailComponent } from '../detail/fatura-mensal-detail.component';
import { FaturaMensalUpdateComponent } from '../update/fatura-mensal-update.component';
import { FaturaMensalRoutingResolveService } from './fatura-mensal-routing-resolve.service';

const faturaMensalRoute: Routes = [
  {
    path: '',
    component: FaturaMensalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FaturaMensalDetailComponent,
    resolve: {
      faturaMensal: FaturaMensalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FaturaMensalUpdateComponent,
    resolve: {
      faturaMensal: FaturaMensalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FaturaMensalUpdateComponent,
    resolve: {
      faturaMensal: FaturaMensalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(faturaMensalRoute)],
  exports: [RouterModule],
})
export class FaturaMensalRoutingModule {}
