jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FilialService } from '../service/filial.service';
import { IFilial, Filial } from '../filial.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

import { FilialUpdateComponent } from './filial-update.component';

describe('Component Tests', () => {
  describe('Filial Management Update Component', () => {
    let comp: FilialUpdateComponent;
    let fixture: ComponentFixture<FilialUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let filialService: FilialService;
    let empresaService: EmpresaService;
    let administradorService: AdministradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FilialUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FilialUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FilialUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      filialService = TestBed.inject(FilialService);
      empresaService = TestBed.inject(EmpresaService);
      administradorService = TestBed.inject(AdministradorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Empresa query and add missing value', () => {
        const filial: IFilial = { id: 456 };
        const empresa: IEmpresa = { id: 75586 };
        filial.empresa = empresa;

        const empresaCollection: IEmpresa[] = [{ id: 39337 }];
        spyOn(empresaService, 'query').and.returnValue(of(new HttpResponse({ body: empresaCollection })));
        const additionalEmpresas = [empresa];
        const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
        spyOn(empresaService, 'addEmpresaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ filial });
        comp.ngOnInit();

        expect(empresaService.query).toHaveBeenCalled();
        expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
        expect(comp.empresasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Administrador query and add missing value', () => {
        const filial: IFilial = { id: 456 };
        const administradores: IAdministrador[] = [{ id: 38659 }];
        filial.administradores = administradores;

        const administradorCollection: IAdministrador[] = [{ id: 58996 }];
        spyOn(administradorService, 'query').and.returnValue(of(new HttpResponse({ body: administradorCollection })));
        const additionalAdministradors = [...administradores];
        const expectedCollection: IAdministrador[] = [...additionalAdministradors, ...administradorCollection];
        spyOn(administradorService, 'addAdministradorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ filial });
        comp.ngOnInit();

        expect(administradorService.query).toHaveBeenCalled();
        expect(administradorService.addAdministradorToCollectionIfMissing).toHaveBeenCalledWith(
          administradorCollection,
          ...additionalAdministradors
        );
        expect(comp.administradorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const filial: IFilial = { id: 456 };
        const empresa: IEmpresa = { id: 38594 };
        filial.empresa = empresa;
        const administradores: IAdministrador = { id: 72590 };
        filial.administradores = [administradores];

        activatedRoute.data = of({ filial });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(filial));
        expect(comp.empresasSharedCollection).toContain(empresa);
        expect(comp.administradorsSharedCollection).toContain(administradores);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filial = { id: 123 };
        spyOn(filialService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: filial }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(filialService.update).toHaveBeenCalledWith(filial);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filial = new Filial();
        spyOn(filialService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: filial }));
        saveSubject.complete();

        // THEN
        expect(filialService.create).toHaveBeenCalledWith(filial);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filial = { id: 123 };
        spyOn(filialService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(filialService.update).toHaveBeenCalledWith(filial);
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

      describe('trackAdministradorById', () => {
        it('Should return tracked Administrador primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAdministradorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedAdministrador', () => {
        it('Should return option if no Administrador is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedAdministrador(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Administrador for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedAdministrador(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Administrador is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedAdministrador(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
