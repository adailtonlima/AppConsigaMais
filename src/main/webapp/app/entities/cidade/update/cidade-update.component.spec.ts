jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CidadeService } from '../service/cidade.service';
import { ICidade, Cidade } from '../cidade.model';
import { IEstado } from 'app/entities/estado/estado.model';
import { EstadoService } from 'app/entities/estado/service/estado.service';

import { CidadeUpdateComponent } from './cidade-update.component';

describe('Component Tests', () => {
  describe('Cidade Management Update Component', () => {
    let comp: CidadeUpdateComponent;
    let fixture: ComponentFixture<CidadeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cidadeService: CidadeService;
    let estadoService: EstadoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CidadeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CidadeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CidadeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cidadeService = TestBed.inject(CidadeService);
      estadoService = TestBed.inject(EstadoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Estado query and add missing value', () => {
        const cidade: ICidade = { id: 456 };
        const estado: IEstado = { id: 50389 };
        cidade.estado = estado;

        const estadoCollection: IEstado[] = [{ id: 72535 }];
        spyOn(estadoService, 'query').and.returnValue(of(new HttpResponse({ body: estadoCollection })));
        const additionalEstados = [estado];
        const expectedCollection: IEstado[] = [...additionalEstados, ...estadoCollection];
        spyOn(estadoService, 'addEstadoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ cidade });
        comp.ngOnInit();

        expect(estadoService.query).toHaveBeenCalled();
        expect(estadoService.addEstadoToCollectionIfMissing).toHaveBeenCalledWith(estadoCollection, ...additionalEstados);
        expect(comp.estadosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cidade: ICidade = { id: 456 };
        const estado: IEstado = { id: 36686 };
        cidade.estado = estado;

        activatedRoute.data = of({ cidade });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cidade));
        expect(comp.estadosSharedCollection).toContain(estado);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cidade = { id: 123 };
        spyOn(cidadeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cidade });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cidade }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cidadeService.update).toHaveBeenCalledWith(cidade);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cidade = new Cidade();
        spyOn(cidadeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cidade });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cidade }));
        saveSubject.complete();

        // THEN
        expect(cidadeService.create).toHaveBeenCalledWith(cidade);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const cidade = { id: 123 };
        spyOn(cidadeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ cidade });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cidadeService.update).toHaveBeenCalledWith(cidade);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEstadoById', () => {
        it('Should return tracked Estado primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEstadoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
