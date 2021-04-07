import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FilialComponent } from '../list/filial.component';
import { FilialDetailComponent } from '../detail/filial-detail.component';
import { FilialUpdateComponent } from '../update/filial-update.component';
import { FilialRoutingResolveService } from './filial-routing-resolve.service';

const filialRoute: Routes = [
  {
    path: '',
    component: FilialComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FilialDetailComponent,
    resolve: {
      filial: FilialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FilialUpdateComponent,
    resolve: {
      filial: FilialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FilialUpdateComponent,
    resolve: {
      filial: FilialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(filialRoute)],
  exports: [RouterModule],
})
export class FilialRoutingModule {}
