jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFaturaMensal, FaturaMensal } from '../fatura-mensal.model';
import { FaturaMensalService } from '../service/fatura-mensal.service';

import { FaturaMensalRoutingResolveService } from './fatura-mensal-routing-resolve.service';

describe('Service Tests', () => {
  describe('FaturaMensal routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FaturaMensalRoutingResolveService;
    let service: FaturaMensalService;
    let resultFaturaMensal: IFaturaMensal | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FaturaMensalRoutingResolveService);
      service = TestBed.inject(FaturaMensalService);
      resultFaturaMensal = undefined;
    });

    describe('resolve', () => {
      it('should return IFaturaMensal returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFaturaMensal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFaturaMensal).toEqual({ id: 123 });
      });

      it('should return new IFaturaMensal if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFaturaMensal = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFaturaMensal).toEqual(new FaturaMensal());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFaturaMensal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFaturaMensal).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
