jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MensalidadePedidoService } from '../service/mensalidade-pedido.service';
import { IMensalidadePedido, MensalidadePedido } from '../mensalidade-pedido.model';
import { IPedido } from 'app/entities/pedido/pedido.model';
import { PedidoService } from 'app/entities/pedido/service/pedido.service';
import { IFaturaMensal } from 'app/entities/fatura-mensal/fatura-mensal.model';
import { FaturaMensalService } from 'app/entities/fatura-mensal/service/fatura-mensal.service';

import { MensalidadePedidoUpdateComponent } from './mensalidade-pedido-update.component';

describe('Component Tests', () => {
  describe('MensalidadePedido Management Update Component', () => {
    let comp: MensalidadePedidoUpdateComponent;
    let fixture: ComponentFixture<MensalidadePedidoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mensalidadePedidoService: MensalidadePedidoService;
    let pedidoService: PedidoService;
    let faturaMensalService: FaturaMensalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MensalidadePedidoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MensalidadePedidoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MensalidadePedidoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mensalidadePedidoService = TestBed.inject(MensalidadePedidoService);
      pedidoService = TestBed.inject(PedidoService);
      faturaMensalService = TestBed.inject(FaturaMensalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pedido query and add missing value', () => {
        const mensalidadePedido: IMensalidadePedido = { id: 456 };
        const pedido: IPedido = { id: 35980 };
        mensalidadePedido.pedido = pedido;

        const pedidoCollection: IPedido[] = [{ id: 55150 }];
        spyOn(pedidoService, 'query').and.returnValue(of(new HttpResponse({ body: pedidoCollection })));
        const additionalPedidos = [pedido];
        const expectedCollection: IPedido[] = [...additionalPedidos, ...pedidoCollection];
        spyOn(pedidoService, 'addPedidoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ mensalidadePedido });
        comp.ngOnInit();

        expect(pedidoService.query).toHaveBeenCalled();
        expect(pedidoService.addPedidoToCollectionIfMissing).toHaveBeenCalledWith(pedidoCollection, ...additionalPedidos);
        expect(comp.pedidosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call FaturaMensal query and add missing value', () => {
        const mensalidadePedido: IMensalidadePedido = { id: 456 };
        const fatura: IFaturaMensal = { id: 18891 };
        mensalidadePedido.fatura = fatura;

        const faturaMensalCollection: IFaturaMensal[] = [{ id: 53426 }];
        spyOn(faturaMensalService, 'query').and.returnValue(of(new HttpResponse({ body: faturaMensalCollection })));
        const additionalFaturaMensals = [fatura];
        const expectedCollection: IFaturaMensal[] = [...additionalFaturaMensals, ...faturaMensalCollection];
        spyOn(faturaMensalService, 'addFaturaMensalToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ mensalidadePedido });
        comp.ngOnInit();

        expect(faturaMensalService.query).toHaveBeenCalled();
        expect(faturaMensalService.addFaturaMensalToCollectionIfMissing).toHaveBeenCalledWith(
          faturaMensalCollection,
          ...additionalFaturaMensals
        );
        expect(comp.faturaMensalsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mensalidadePedido: IMensalidadePedido = { id: 456 };
        const pedido: IPedido = { id: 27935 };
        mensalidadePedido.pedido = pedido;
        const fatura: IFaturaMensal = { id: 77810 };
        mensalidadePedido.fatura = fatura;

        activatedRoute.data = of({ mensalidadePedido });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mensalidadePedido));
        expect(comp.pedidosSharedCollection).toContain(pedido);
        expect(comp.faturaMensalsSharedCollection).toContain(fatura);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mensalidadePedido = { id: 123 };
        spyOn(mensalidadePedidoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mensalidadePedido });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mensalidadePedido }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mensalidadePedidoService.update).toHaveBeenCalledWith(mensalidadePedido);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mensalidadePedido = new MensalidadePedido();
        spyOn(mensalidadePedidoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mensalidadePedido });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mensalidadePedido }));
        saveSubject.complete();

        // THEN
        expect(mensalidadePedidoService.create).toHaveBeenCalledWith(mensalidadePedido);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mensalidadePedido = { id: 123 };
        spyOn(mensalidadePedidoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mensalidadePedido });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mensalidadePedidoService.update).toHaveBeenCalledWith(mensalidadePedido);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPedidoById', () => {
        it('Should return tracked Pedido primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPedidoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFaturaMensalById', () => {
        it('Should return tracked FaturaMensal primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFaturaMensalById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
