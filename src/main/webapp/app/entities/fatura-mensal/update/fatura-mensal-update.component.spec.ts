jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FaturaMensalService } from '../service/fatura-mensal.service';
import { IFaturaMensal, FaturaMensal } from '../fatura-mensal.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';

import { FaturaMensalUpdateComponent } from './fatura-mensal-update.component';

describe('Component Tests', () => {
  describe('FaturaMensal Management Update Component', () => {
    let comp: FaturaMensalUpdateComponent;
    let fixture: ComponentFixture<FaturaMensalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let faturaMensalService: FaturaMensalService;
    let empresaService: EmpresaService;
    let filialService: FilialService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FaturaMensalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FaturaMensalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FaturaMensalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      faturaMensalService = TestBed.inject(FaturaMensalService);
      empresaService = TestBed.inject(EmpresaService);
      filialService = TestBed.inject(FilialService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Empresa query and add missing value', () => {
        const faturaMensal: IFaturaMensal = { id: 456 };
        const empresa: IEmpresa = { id: 56322 };
        faturaMensal.empresa = empresa;

        const empresaCollection: IEmpresa[] = [{ id: 46138 }];
        spyOn(empresaService, 'query').and.returnValue(of(new HttpResponse({ body: empresaCollection })));
        const additionalEmpresas = [empresa];
        const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
        spyOn(empresaService, 'addEmpresaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ faturaMensal });
        comp.ngOnInit();

        expect(empresaService.query).toHaveBeenCalled();
        expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
        expect(comp.empresasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Filial query and add missing value', () => {
        const faturaMensal: IFaturaMensal = { id: 456 };
        const filial: IFilial = { id: 64432 };
        faturaMensal.filial = filial;

        const filialCollection: IFilial[] = [{ id: 49835 }];
        spyOn(filialService, 'query').and.returnValue(of(new HttpResponse({ body: filialCollection })));
        const additionalFilials = [filial];
        const expectedCollection: IFilial[] = [...additionalFilials, ...filialCollection];
        spyOn(filialService, 'addFilialToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ faturaMensal });
        comp.ngOnInit();

        expect(filialService.query).toHaveBeenCalled();
        expect(filialService.addFilialToCollectionIfMissing).toHaveBeenCalledWith(filialCollection, ...additionalFilials);
        expect(comp.filialsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const faturaMensal: IFaturaMensal = { id: 456 };
        const empresa: IEmpresa = { id: 52430 };
        faturaMensal.empresa = empresa;
        const filial: IFilial = { id: 81141 };
        faturaMensal.filial = filial;

        activatedRoute.data = of({ faturaMensal });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(faturaMensal));
        expect(comp.empresasSharedCollection).toContain(empresa);
        expect(comp.filialsSharedCollection).toContain(filial);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const faturaMensal = { id: 123 };
        spyOn(faturaMensalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ faturaMensal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: faturaMensal }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(faturaMensalService.update).toHaveBeenCalledWith(faturaMensal);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const faturaMensal = new FaturaMensal();
        spyOn(faturaMensalService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ faturaMensal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: faturaMensal }));
        saveSubject.complete();

        // THEN
        expect(faturaMensalService.create).toHaveBeenCalledWith(faturaMensal);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const faturaMensal = { id: 123 };
        spyOn(faturaMensalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ faturaMensal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(faturaMensalService.update).toHaveBeenCalledWith(faturaMensal);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
