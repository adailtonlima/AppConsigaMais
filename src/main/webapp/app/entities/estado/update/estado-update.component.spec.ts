jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EstadoService } from '../service/estado.service';
import { IEstado, Estado } from '../estado.model';

import { EstadoUpdateComponent } from './estado-update.component';

describe('Component Tests', () => {
  describe('Estado Management Update Component', () => {
    let comp: EstadoUpdateComponent;
    let fixture: ComponentFixture<EstadoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let estadoService: EstadoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EstadoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EstadoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EstadoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      estadoService = TestBed.inject(EstadoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const estado: IEstado = { id: 456 };

        activatedRoute.data = of({ estado });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(estado));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const estado = { id: 123 };
        spyOn(estadoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ estado });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: estado }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(estadoService.update).toHaveBeenCalledWith(estado);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const estado = new Estado();
        spyOn(estadoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ estado });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: estado }));
        saveSubject.complete();

        // THEN
        expect(estadoService.create).toHaveBeenCalledWith(estado);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const estado = { id: 123 };
        spyOn(estadoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ estado });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(estadoService.update).toHaveBeenCalledWith(estado);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
