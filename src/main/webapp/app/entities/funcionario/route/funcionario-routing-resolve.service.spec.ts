jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFuncionario, Funcionario } from '../funcionario.model';
import { FuncionarioService } from '../service/funcionario.service';

import { FuncionarioRoutingResolveService } from './funcionario-routing-resolve.service';

describe('Service Tests', () => {
  describe('Funcionario routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FuncionarioRoutingResolveService;
    let service: FuncionarioService;
    let resultFuncionario: IFuncionario | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FuncionarioRoutingResolveService);
      service = TestBed.inject(FuncionarioService);
      resultFuncionario = undefined;
    });

    describe('resolve', () => {
      it('should return IFuncionario returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFuncionario = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFuncionario).toEqual({ id: 123 });
      });

      it('should return new IFuncionario if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFuncionario = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFuncionario).toEqual(new Funcionario());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFuncionario = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFuncionario).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
