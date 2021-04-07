jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EnderecoService } from '../service/endereco.service';
import { IEndereco, Endereco } from '../endereco.model';
import { ICidade } from 'app/entities/cidade/cidade.model';
import { CidadeService } from 'app/entities/cidade/service/cidade.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { IFuncionario } from 'app/entities/funcionario/funcionario.model';
import { FuncionarioService } from 'app/entities/funcionario/service/funcionario.service';

import { EnderecoUpdateComponent } from './endereco-update.component';

describe('Component Tests', () => {
  describe('Endereco Management Update Component', () => {
    let comp: EnderecoUpdateComponent;
    let fixture: ComponentFixture<EnderecoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let enderecoService: EnderecoService;
    let cidadeService: CidadeService;
    let empresaService: EmpresaService;
    let filialService: FilialService;
    let funcionarioService: FuncionarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EnderecoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EnderecoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnderecoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      enderecoService = TestBed.inject(EnderecoService);
      cidadeService = TestBed.inject(CidadeService);
      empresaService = TestBed.inject(EmpresaService);
      filialService = TestBed.inject(FilialService);
      funcionarioService = TestBed.inject(FuncionarioService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Cidade query and add missing value', () => {
        const endereco: IEndereco = { id: 456 };
        const cidade: ICidade = { id: 57744 };
        endereco.cidade = cidade;

        const cidadeCollection: ICidade[] = [{ id: 2660 }];
        spyOn(cidadeService, 'query').and.returnValue(of(new HttpResponse({ body: cidadeCollection })));
        const additionalCidades = [cidade];
        const expectedCollection: ICidade[] = [...additionalCidades, ...cidadeCollection];
        spyOn(cidadeService, 'addCidadeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        expect(cidadeService.query).toHaveBeenCalled();
        expect(cidadeService.addCidadeToCollectionIfMissing).toHaveBeenCalledWith(cidadeCollection, ...additionalCidades);
        expect(comp.cidadesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Empresa query and add missing value', () => {
        const endereco: IEndereco = { id: 456 };
        const empresa: IEmpresa = { id: 7992 };
        endereco.empresa = empresa;

        const empresaCollection: IEmpresa[] = [{ id: 91555 }];
        spyOn(empresaService, 'query').and.returnValue(of(new HttpResponse({ body: empresaCollection })));
        const additionalEmpresas = [empresa];
        const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
        spyOn(empresaService, 'addEmpresaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        expect(empresaService.query).toHaveBeenCalled();
        expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
        expect(comp.empresasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Filial query and add missing value', () => {
        const endereco: IEndereco = { id: 456 };
        const filial: IFilial = { id: 32256 };
        endereco.filial = filial;

        const filialCollection: IFilial[] = [{ id: 97872 }];
        spyOn(filialService, 'query').and.returnValue(of(new HttpResponse({ body: filialCollection })));
        const additionalFilials = [filial];
        const expectedCollection: IFilial[] = [...additionalFilials, ...filialCollection];
        spyOn(filialService, 'addFilialToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        expect(filialService.query).toHaveBeenCalled();
        expect(filialService.addFilialToCollectionIfMissing).toHaveBeenCalledWith(filialCollection, ...additionalFilials);
        expect(comp.filialsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Funcionario query and add missing value', () => {
        const endereco: IEndereco = { id: 456 };
        const funcionario: IFuncionario = { id: 20382 };
        endereco.funcionario = funcionario;

        const funcionarioCollection: IFuncionario[] = [{ id: 37972 }];
        spyOn(funcionarioService, 'query').and.returnValue(of(new HttpResponse({ body: funcionarioCollection })));
        const additionalFuncionarios = [funcionario];
        const expectedCollection: IFuncionario[] = [...additionalFuncionarios, ...funcionarioCollection];
        spyOn(funcionarioService, 'addFuncionarioToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        expect(funcionarioService.query).toHaveBeenCalled();
        expect(funcionarioService.addFuncionarioToCollectionIfMissing).toHaveBeenCalledWith(
          funcionarioCollection,
          ...additionalFuncionarios
        );
        expect(comp.funcionariosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const endereco: IEndereco = { id: 456 };
        const cidade: ICidade = { id: 10713 };
        endereco.cidade = cidade;
        const empresa: IEmpresa = { id: 48815 };
        endereco.empresa = empresa;
        const filial: IFilial = { id: 71521 };
        endereco.filial = filial;
        const funcionario: IFuncionario = { id: 20747 };
        endereco.funcionario = funcionario;

        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(endereco));
        expect(comp.cidadesSharedCollection).toContain(cidade);
        expect(comp.empresasSharedCollection).toContain(empresa);
        expect(comp.filialsSharedCollection).toContain(filial);
        expect(comp.funcionariosSharedCollection).toContain(funcionario);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const endereco = { id: 123 };
        spyOn(enderecoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: endereco }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(enderecoService.update).toHaveBeenCalledWith(endereco);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const endereco = new Endereco();
        spyOn(enderecoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: endereco }));
        saveSubject.complete();

        // THEN
        expect(enderecoService.create).toHaveBeenCalledWith(endereco);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const endereco = { id: 123 };
        spyOn(enderecoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(enderecoService.update).toHaveBeenCalledWith(endereco);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCidadeById', () => {
        it('Should return tracked Cidade primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCidadeById(0, entity);
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

      describe('trackFuncionarioById', () => {
        it('Should return tracked Funcionario primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFuncionarioById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
