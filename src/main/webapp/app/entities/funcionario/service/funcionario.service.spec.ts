import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFuncionario, Funcionario } from '../funcionario.model';

import { FuncionarioService } from './funcionario.service';

describe('Service Tests', () => {
  describe('Funcionario Service', () => {
    let service: FuncionarioService;
    let httpMock: HttpTestingController;
    let elemDefault: IFuncionario;
    let expectedResult: IFuncionario | IFuncionario[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FuncionarioService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        cpf: 'AAAAAAA',
        criado: currentDate,
        atualizado: currentDate,
        ultimoLogin: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            criado: currentDate.format(DATE_TIME_FORMAT),
            atualizado: currentDate.format(DATE_TIME_FORMAT),
            ultimoLogin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Funcionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            criado: currentDate.format(DATE_TIME_FORMAT),
            atualizado: currentDate.format(DATE_TIME_FORMAT),
            ultimoLogin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            atualizado: currentDate,
            ultimoLogin: currentDate,
          },
          returnedFromService
        );

        service.create(new Funcionario()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Funcionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            cpf: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
            atualizado: currentDate.format(DATE_TIME_FORMAT),
            ultimoLogin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            atualizado: currentDate,
            ultimoLogin: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Funcionario', () => {
        const patchObject = Object.assign(
          {
            ultimoLogin: currentDate.format(DATE_TIME_FORMAT),
          },
          new Funcionario()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            criado: currentDate,
            atualizado: currentDate,
            ultimoLogin: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Funcionario', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            cpf: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
            atualizado: currentDate.format(DATE_TIME_FORMAT),
            ultimoLogin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            atualizado: currentDate,
            ultimoLogin: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Funcionario', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFuncionarioToCollectionIfMissing', () => {
        it('should add a Funcionario to an empty array', () => {
          const funcionario: IFuncionario = { id: 123 };
          expectedResult = service.addFuncionarioToCollectionIfMissing([], funcionario);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(funcionario);
        });

        it('should not add a Funcionario to an array that contains it', () => {
          const funcionario: IFuncionario = { id: 123 };
          const funcionarioCollection: IFuncionario[] = [
            {
              ...funcionario,
            },
            { id: 456 },
          ];
          expectedResult = service.addFuncionarioToCollectionIfMissing(funcionarioCollection, funcionario);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Funcionario to an array that doesn't contain it", () => {
          const funcionario: IFuncionario = { id: 123 };
          const funcionarioCollection: IFuncionario[] = [{ id: 456 }];
          expectedResult = service.addFuncionarioToCollectionIfMissing(funcionarioCollection, funcionario);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(funcionario);
        });

        it('should add only unique Funcionario to an array', () => {
          const funcionarioArray: IFuncionario[] = [{ id: 123 }, { id: 456 }, { id: 46921 }];
          const funcionarioCollection: IFuncionario[] = [{ id: 123 }];
          expectedResult = service.addFuncionarioToCollectionIfMissing(funcionarioCollection, ...funcionarioArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const funcionario: IFuncionario = { id: 123 };
          const funcionario2: IFuncionario = { id: 456 };
          expectedResult = service.addFuncionarioToCollectionIfMissing([], funcionario, funcionario2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(funcionario);
          expect(expectedResult).toContain(funcionario2);
        });

        it('should accept null and undefined values', () => {
          const funcionario: IFuncionario = { id: 123 };
          expectedResult = service.addFuncionarioToCollectionIfMissing([], null, funcionario, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(funcionario);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
