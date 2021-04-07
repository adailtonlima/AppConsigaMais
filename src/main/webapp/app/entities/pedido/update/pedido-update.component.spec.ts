jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PedidoService } from '../service/pedido.service';
import { IPedido, Pedido } from '../pedido.model';
import { IFuncionario } from 'app/entities/funcionario/funcionario.model';
import { FuncionarioService } from 'app/entities/funcionario/service/funcionario.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

import { PedidoUpdateComponent } from './pedido-update.component';

describe('Component Tests', () => {
  describe('Pedido Management Update Component', () => {
    let comp: PedidoUpdateComponent;
    let fixture: ComponentFixture<PedidoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pedidoService: PedidoService;
    let funcionarioService: FuncionarioService;
    let empresaService: EmpresaService;
    let filialService: FilialService;
    let administradorService: AdministradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PedidoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PedidoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PedidoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pedidoService = TestBed.inject(PedidoService);
      funcionarioService = TestBed.inject(FuncionarioService);
      empresaService = TestBed.inject(EmpresaService);
      filialService = TestBed.inject(FilialService);
      administradorService = TestBed.inject(AdministradorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Funcionario query and add missing value', () => {
        const pedido: IPedido = { id: 456 };
        const funcionario: IFuncionario = { id: 45596 };
        pedido.funcionario = funcionario;

        const funcionarioCollection: IFuncionario[] = [{ id: 9841 }];
        spyOn(funcionarioService, 'query').and.returnValue(of(new HttpResponse({ body: funcionarioCollection })));
        const additionalFuncionarios = [funcionario];
        const expectedCollection: IFuncionario[] = [...additionalFuncionarios, ...funcionarioCollection];
        spyOn(funcionarioService, 'addFuncionarioToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        expect(funcionarioService.query).toHaveBeenCalled();
        expect(funcionarioService.addFuncionarioToCollectionIfMissing).toHaveBeenCalledWith(
          funcionarioCollection,
          ...additionalFuncionarios
        );
        expect(comp.funcionariosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Empresa query and add missing value', () => {
        const pedido: IPedido = { id: 456 };
        const empresa: IEmpresa = { id: 36861 };
        pedido.empresa = empresa;

        const empresaCollection: IEmpresa[] = [{ id: 994 }];
        spyOn(empresaService, 'query').and.returnValue(of(new HttpResponse({ body: empresaCollection })));
        const additionalEmpresas = [empresa];
        const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
        spyOn(empresaService, 'addEmpresaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        expect(empresaService.query).toHaveBeenCalled();
        expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
        expect(comp.empresasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Filial query and add missing value', () => {
        const pedido: IPedido = { id: 456 };
        const filia: IFilial = { id: 60664 };
        pedido.filia = filia;

        const filialCollection: IFilial[] = [{ id: 47725 }];
        spyOn(filialService, 'query').and.returnValue(of(new HttpResponse({ body: filialCollection })));
        const additionalFilials = [filia];
        const expectedCollection: IFilial[] = [...additionalFilials, ...filialCollection];
        spyOn(filialService, 'addFilialToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        expect(filialService.query).toHaveBeenCalled();
        expect(filialService.addFilialToCollectionIfMissing).toHaveBeenCalledWith(filialCollection, ...additionalFilials);
        expect(comp.filialsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Administrador query and add missing value', () => {
        const pedido: IPedido = { id: 456 };
        const quemAprovou: IAdministrador = { id: 37067 };
        pedido.quemAprovou = quemAprovou;

        const administradorCollection: IAdministrador[] = [{ id: 10616 }];
        spyOn(administradorService, 'query').and.returnValue(of(new HttpResponse({ body: administradorCollection })));
        const additionalAdministradors = [quemAprovou];
        const expectedCollection: IAdministrador[] = [...additionalAdministradors, ...administradorCollection];
        spyOn(administradorService, 'addAdministradorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        expect(administradorService.query).toHaveBeenCalled();
        expect(administradorService.addAdministradorToCollectionIfMissing).toHaveBeenCalledWith(
          administradorCollection,
          ...additionalAdministradors
        );
        expect(comp.administradorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pedido: IPedido = { id: 456 };
        const funcionario: IFuncionario = { id: 3580 };
        pedido.funcionario = funcionario;
        const empresa: IEmpresa = { id: 20008 };
        pedido.empresa = empresa;
        const filia: IFilial = { id: 48241 };
        pedido.filia = filia;
        const quemAprovou: IAdministrador = { id: 24530 };
        pedido.quemAprovou = quemAprovou;

        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pedido));
        expect(comp.funcionariosSharedCollection).toContain(funcionario);
        expect(comp.empresasSharedCollection).toContain(empresa);
        expect(comp.filialsSharedCollection).toContain(filia);
        expect(comp.administradorsSharedCollection).toContain(quemAprovou);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pedido = { id: 123 };
        spyOn(pedidoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pedido }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pedidoService.update).toHaveBeenCalledWith(pedido);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pedido = new Pedido();
        spyOn(pedidoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pedido }));
        saveSubject.complete();

        // THEN
        expect(pedidoService.create).toHaveBeenCalledWith(pedido);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pedido = { id: 123 };
        spyOn(pedidoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pedido });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pedidoService.update).toHaveBeenCalledWith(pedido);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFuncionarioById', () => {
        it('Should return tracked Funcionario primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFuncionarioById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEmpresaById', () => {
        it('Should return tracked Empresa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEmpresaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFilialById', () => {
        it('Should return tracked Filial primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFilialById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAdministradorById', () => {
        it('Should return tracked Administrador primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAdministradorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
