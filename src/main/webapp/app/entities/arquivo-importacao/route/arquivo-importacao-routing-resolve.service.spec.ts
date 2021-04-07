jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IArquivoImportacao, ArquivoImportacao } from '../arquivo-importacao.model';
import { ArquivoImportacaoService } from '../service/arquivo-importacao.service';

import { ArquivoImportacaoRoutingResolveService } from './arquivo-importacao-routing-resolve.service';

describe('Service Tests', () => {
  describe('ArquivoImportacao routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ArquivoImportacaoRoutingResolveService;
    let service: ArquivoImportacaoService;
    let resultArquivoImportacao: IArquivoImportacao | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ArquivoImportacaoRoutingResolveService);
      service = TestBed.inject(ArquivoImportacaoService);
      resultArquivoImportacao = undefined;
    });

    describe('resolve', () => {
      it('should return IArquivoImportacao returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultArquivoImportacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultArquivoImportacao).toEqual({ id: 123 });
      });

      it('should return new IArquivoImportacao if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultArquivoImportacao = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultArquivoImportacao).toEqual(new ArquivoImportacao());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultArquivoImportacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultArquivoImportacao).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
