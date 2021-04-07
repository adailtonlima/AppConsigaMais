jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FuncionarioService } from '../service/funcionario.service';
import { IFuncionario, Funcionario } from '../funcionario.model';

import { FuncionarioUpdateComponent } from './funcionario-update.component';

describe('Component Tests', () => {
  describe('Funcionario Management Update Component', () => {
    let comp: FuncionarioUpdateComponent;
    let fixture: ComponentFixture<FuncionarioUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let funcionarioService: FuncionarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FuncionarioUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FuncionarioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FuncionarioUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      funcionarioService = TestBed.inject(FuncionarioService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const funcionario: IFuncionario = { id: 456 };

        activatedRoute.data = of({ funcionario });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(funcionario));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const funcionario = { id: 123 };
        spyOn(funcionarioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ funcionario });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: funcionario }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(funcionarioService.update).toHaveBeenCalledWith(funcionario);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const funcionario = new Funcionario();
        spyOn(funcionarioService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ funcionario });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: funcionario }));
        saveSubject.complete();

        // THEN
        expect(funcionarioService.create).toHaveBeenCalledWith(funcionario);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const funcionario = { id: 123 };
        spyOn(funcionarioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ funcionario });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(funcionarioService.update).toHaveBeenCalledWith(funcionario);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
