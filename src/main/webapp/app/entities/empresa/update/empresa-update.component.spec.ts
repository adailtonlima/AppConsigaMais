jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmpresaService } from '../service/empresa.service';
import { IEmpresa, Empresa } from '../empresa.model';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

import { EmpresaUpdateComponent } from './empresa-update.component';

describe('Component Tests', () => {
  describe('Empresa Management Update Component', () => {
    let comp: EmpresaUpdateComponent;
    let fixture: ComponentFixture<EmpresaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let empresaService: EmpresaService;
    let administradorService: AdministradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmpresaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EmpresaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmpresaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      empresaService = TestBed.inject(EmpresaService);
      administradorService = TestBed.inject(AdministradorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Administrador query and add missing value', () => {
        const empresa: IEmpresa = { id: 456 };
        const administradores: IAdministrador[] = [{ id: 34815 }];
        empresa.administradores = administradores;

        const administradorCollection: IAdministrador[] = [{ id: 57791 }];
        spyOn(administradorService, 'query').and.returnValue(of(new HttpResponse({ body: administradorCollection })));
        const additionalAdministradors = [...administradores];
        const expectedCollection: IAdministrador[] = [...additionalAdministradors, ...administradorCollection];
        spyOn(administradorService, 'addAdministradorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        expect(administradorService.query).toHaveBeenCalled();
        expect(administradorService.addAdministradorToCollectionIfMissing).toHaveBeenCalledWith(
          administradorCollection,
          ...additionalAdministradors
        );
        expect(comp.administradorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const empresa: IEmpresa = { id: 456 };
        const administradores: IAdministrador = { id: 33650 };
        empresa.administradores = [administradores];

        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(empresa));
        expect(comp.administradorsSharedCollection).toContain(administradores);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const empresa = { id: 123 };
        spyOn(empresaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: empresa }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(empresaService.update).toHaveBeenCalledWith(empresa);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const empresa = new Empresa();
        spyOn(empresaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: empresa }));
        saveSubject.complete();

        // THEN
        expect(empresaService.create).toHaveBeenCalledWith(empresa);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const empresa = { id: 123 };
        spyOn(empresaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(empresaService.update).toHaveBeenCalledWith(empresa);
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
