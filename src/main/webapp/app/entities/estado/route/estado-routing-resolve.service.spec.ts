jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEstado, Estado } from '../estado.model';
import { EstadoService } from '../service/estado.service';

import { EstadoRoutingResolveService } from './estado-routing-resolve.service';

describe('Service Tests', () => {
  describe('Estado routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EstadoRoutingResolveService;
    let service: EstadoService;
    let resultEstado: IEstado | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EstadoRoutingResolveService);
      service = TestBed.inject(EstadoService);
      resultEstado = undefined;
    });

    describe('resolve', () => {
      it('should return IEstado returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEstado = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEstado).toEqual({ id: 123 });
      });

      it('should return new IEstado if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEstado = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEstado).toEqual(new Estado());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEstado = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEstado).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
