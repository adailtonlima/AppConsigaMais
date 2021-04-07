jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMensalidadePedido, MensalidadePedido } from '../mensalidade-pedido.model';
import { MensalidadePedidoService } from '../service/mensalidade-pedido.service';

import { MensalidadePedidoRoutingResolveService } from './mensalidade-pedido-routing-resolve.service';

describe('Service Tests', () => {
  describe('MensalidadePedido routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MensalidadePedidoRoutingResolveService;
    let service: MensalidadePedidoService;
    let resultMensalidadePedido: IMensalidadePedido | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MensalidadePedidoRoutingResolveService);
      service = TestBed.inject(MensalidadePedidoService);
      resultMensalidadePedido = undefined;
    });

    describe('resolve', () => {
      it('should return IMensalidadePedido returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMensalidadePedido = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMensalidadePedido).toEqual({ id: 123 });
      });

      it('should return new IMensalidadePedido if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMensalidadePedido = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMensalidadePedido).toEqual(new MensalidadePedido());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMensalidadePedido = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMensalidadePedido).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
