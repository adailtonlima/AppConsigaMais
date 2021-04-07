import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MensalidadePedidoComponent } from '../list/mensalidade-pedido.component';
import { MensalidadePedidoDetailComponent } from '../detail/mensalidade-pedido-detail.component';
import { MensalidadePedidoUpdateComponent } from '../update/mensalidade-pedido-update.component';
import { MensalidadePedidoRoutingResolveService } from './mensalidade-pedido-routing-resolve.service';

const mensalidadePedidoRoute: Routes = [
  {
    path: '',
    component: MensalidadePedidoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MensalidadePedidoDetailComponent,
    resolve: {
      mensalidadePedido: MensalidadePedidoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MensalidadePedidoUpdateComponent,
    resolve: {
      mensalidadePedido: MensalidadePedidoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MensalidadePedidoUpdateComponent,
    resolve: {
      mensalidadePedido: MensalidadePedidoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mensalidadePedidoRoute)],
  exports: [RouterModule],
})
export class MensalidadePedidoRoutingModule {}
