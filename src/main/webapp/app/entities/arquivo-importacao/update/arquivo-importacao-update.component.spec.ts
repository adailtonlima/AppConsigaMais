jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ArquivoImportacaoService } from '../service/arquivo-importacao.service';
import { IArquivoImportacao, ArquivoImportacao } from '../arquivo-importacao.model';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';

import { ArquivoImportacaoUpdateComponent } from './arquivo-importacao-update.component';

describe('Component Tests', () => {
  describe('ArquivoImportacao Management Update Component', () => {
    let comp: ArquivoImportacaoUpdateComponent;
    let fixture: ComponentFixture<ArquivoImportacaoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let arquivoImportacaoService: ArquivoImportacaoService;
    let administradorService: AdministradorService;
    let empresaService: EmpresaService;
    let filialService: FilialService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ArquivoImportacaoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ArquivoImportacaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArquivoImportacaoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      arquivoImportacaoService = TestBed.inject(ArquivoImportacaoService);
      administradorService = TestBed.inject(AdministradorService);
      empresaService = TestBed.inject(EmpresaService);
      filialService = TestBed.inject(FilialService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Administrador query and add missing value', () => {
        const arquivoImportacao: IArquivoImportacao = { id: 456 };
        const criador: IAdministrador = { id: 86191 };
        arquivoImportacao.criador = criador;

        const administradorCollection: IAdministrador[] = [{ id: 22482 }];
        spyOn(administradorService, 'query').and.returnValue(of(new HttpResponse({ body: administradorCollection })));
        const additionalAdministradors = [criador];
        const expectedCollection: IAdministrador[] = [...additionalAdministradors, ...administradorCollection];
        spyOn(administradorService, 'addAdministradorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ arquivoImportacao });
        comp.ngOnInit();

        expect(administradorService.query).toHaveBeenCalled();
        expect(administradorService.addAdministradorToCollectionIfMissing).toHaveBeenCalledWith(
          administradorCollection,
          ...additionalAdministradors
        );
        expect(comp.administradorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Empresa query and add missing value', () => {
        const arquivoImportacao: IArquivoImportacao = { id: 456 };
        const empresa: IEmpresa = { id: 92835 };
        arquivoImportacao.empresa = empresa;

        const empresaCollection: IEmpresa[] = [{ id: 95414 }];
        spyOn(empresaService, 'query').and.returnValue(of(new HttpResponse({ body: empresaCollection })));
        const additionalEmpresas = [empresa];
        const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
        spyOn(empresaService, 'addEmpresaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ arquivoImportacao });
        comp.ngOnInit();

        expect(empresaService.query).toHaveBeenCalled();
        expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
        expect(comp.empresasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Filial query and add missing value', () => {
        const arquivoImportacao: IArquivoImportacao = { id: 456 };
        const filial: IFilial = { id: 1996 };
        arquivoImportacao.filial = filial;

        const filialCollection: IFilial[] = [{ id: 76139 }];
        spyOn(filialService, 'query').and.returnValue(of(new HttpResponse({ body: filialCollection })));
        const additionalFilials = [filial];
        const expectedCollection: IFilial[] = [...additionalFilials, ...filialCollection];
        spyOn(filialService, 'addFilialToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ arquivoImportacao });
        comp.ngOnInit();

        expect(filialService.query).toHaveBeenCalled();
        expect(filialService.addFilialToCollectionIfMissing).toHaveBeenCalledWith(filialCollection, ...additionalFilials);
        expect(comp.filialsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const arquivoImportacao: IArquivoImportacao = { id: 456 };
        const criador: IAdministrador = { id: 18494 };
        arquivoImportacao.criador = criador;
        const empresa: IEmpresa = { id: 77957 };
        arquivoImportacao.empresa = empresa;
        const filial: IFilial = { id: 77108 };
        arquivoImportacao.filial = filial;

        activatedRoute.data = of({ arquivoImportacao });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(arquivoImportacao));
        expect(comp.administradorsSharedCollection).toContain(criador);
        expect(comp.empresasSharedCollection).toContain(empresa);
        expect(comp.filialsSharedCollection).toContain(filial);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const arquivoImportacao = { id: 123 };
        spyOn(arquivoImportacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ arquivoImportacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: arquivoImportacao }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(arquivoImportacaoService.update).toHaveBeenCalledWith(arquivoImportacao);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const arquivoImportacao = new ArquivoImportacao();
        spyOn(arquivoImportacaoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ arquivoImportacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: arquivoImportacao }));
        saveSubject.complete();

        // THEN
        expect(arquivoImportacaoService.create).toHaveBeenCalledWith(arquivoImportacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const arquivoImportacao = { id: 123 };
        spyOn(arquivoImportacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ arquivoImportacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(arquivoImportacaoService.update).toHaveBeenCalledWith(arquivoImportacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAdministradorById', () => {
        it('Should return tracked Administrador primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAdministradorById(0, entity);
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
    });
  });
});
