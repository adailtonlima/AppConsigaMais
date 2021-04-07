import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'estado',
        data: { pageTitle: 'appConsigaMaisApp.estado.home.title' },
        loadChildren: () => import('./estado/estado.module').then(m => m.EstadoModule),
      },
      {
        path: 'cidade',
        data: { pageTitle: 'appConsigaMaisApp.cidade.home.title' },
        loadChildren: () => import('./cidade/cidade.module').then(m => m.CidadeModule),
      },
      {
        path: 'endereco',
        data: { pageTitle: 'appConsigaMaisApp.endereco.home.title' },
        loadChildren: () => import('./endereco/endereco.module').then(m => m.EnderecoModule),
      },
      {
        path: 'arquivo-importacao',
        data: { pageTitle: 'appConsigaMaisApp.arquivoImportacao.home.title' },
        loadChildren: () => import('./arquivo-importacao/arquivo-importacao.module').then(m => m.ArquivoImportacaoModule),
      },
      {
        path: 'empresa',
        data: { pageTitle: 'appConsigaMaisApp.empresa.home.title' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      {
        path: 'filial',
        data: { pageTitle: 'appConsigaMaisApp.filial.home.title' },
        loadChildren: () => import('./filial/filial.module').then(m => m.FilialModule),
      },
      {
        path: 'funcionario',
        data: { pageTitle: 'appConsigaMaisApp.funcionario.home.title' },
        loadChildren: () => import('./funcionario/funcionario.module').then(m => m.FuncionarioModule),
      },
      {
        path: 'administrador',
        data: { pageTitle: 'appConsigaMaisApp.administrador.home.title' },
        loadChildren: () => import('./administrador/administrador.module').then(m => m.AdministradorModule),
      },
      {
        path: 'pedido',
        data: { pageTitle: 'appConsigaMaisApp.pedido.home.title' },
        loadChildren: () => import('./pedido/pedido.module').then(m => m.PedidoModule),
      },
      {
        path: 'mensalidade-pedido',
        data: { pageTitle: 'appConsigaMaisApp.mensalidadePedido.home.title' },
        loadChildren: () => import('./mensalidade-pedido/mensalidade-pedido.module').then(m => m.MensalidadePedidoModule),
      },
      {
        path: 'fatura-mensal',
        data: { pageTitle: 'appConsigaMaisApp.faturaMensal.home.title' },
        loadChildren: () => import('./fatura-mensal/fatura-mensal.module').then(m => m.FaturaMensalModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
