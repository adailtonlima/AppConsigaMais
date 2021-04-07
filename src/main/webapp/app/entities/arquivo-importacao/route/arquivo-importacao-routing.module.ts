import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArquivoImportacaoComponent } from '../list/arquivo-importacao.component';
import { ArquivoImportacaoDetailComponent } from '../detail/arquivo-importacao-detail.component';
import { ArquivoImportacaoUpdateComponent } from '../update/arquivo-importacao-update.component';
import { ArquivoImportacaoRoutingResolveService } from './arquivo-importacao-routing-resolve.service';

const arquivoImportacaoRoute: Routes = [
  {
    path: '',
    component: ArquivoImportacaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArquivoImportacaoDetailComponent,
    resolve: {
      arquivoImportacao: ArquivoImportacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArquivoImportacaoUpdateComponent,
    resolve: {
      arquivoImportacao: ArquivoImportacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArquivoImportacaoUpdateComponent,
    resolve: {
      arquivoImportacao: ArquivoImportacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(arquivoImportacaoRoute)],
  exports: [RouterModule],
})
export class ArquivoImportacaoRoutingModule {}
