import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { StatusArquivo } from 'app/entities/enumerations/status-arquivo.model';
import { IArquivoImportacao, ArquivoImportacao } from '../arquivo-importacao.model';

import { ArquivoImportacaoService } from './arquivo-importacao.service';

describe('Service Tests', () => {
  describe('ArquivoImportacao Service', () => {
    let service: ArquivoImportacaoService;
    let httpMock: HttpTestingController;
    let elemDefault: IArquivoImportacao;
    let expectedResult: IArquivoImportacao | IArquivoImportacao[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ArquivoImportacaoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        urlArquivo: 'AAAAAAA',
        urlCriticas: 'AAAAAAA',
        criado: currentDate,
        estado: StatusArquivo.ENVIADO,
        processado: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            criado: currentDate.format(DATE_TIME_FORMAT),
            processado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ArquivoImportacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            criado: currentDate.format(DATE_TIME_FORMAT),
            processado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            processado: currentDate,
          },
          returnedFromService
        );

        service.create(new ArquivoImportacao()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ArquivoImportacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            urlArquivo: 'BBBBBB',
            urlCriticas: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
            estado: 'BBBBBB',
            processado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            processado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ArquivoImportacao', () => {
        const patchObject = Object.assign(
          {
            urlArquivo: 'BBBBBB',
            estado: 'BBBBBB',
            processado: currentDate.format(DATE_TIME_FORMAT),
          },
          new ArquivoImportacao()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            criado: currentDate,
            processado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ArquivoImportacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            urlArquivo: 'BBBBBB',
            urlCriticas: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
            estado: 'BBBBBB',
            processado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            processado: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ArquivoImportacao', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addArquivoImportacaoToCollectionIfMissing', () => {
        it('should add a ArquivoImportacao to an empty array', () => {
          const arquivoImportacao: IArquivoImportacao = { id: 123 };
          expectedResult = service.addArquivoImportacaoToCollectionIfMissing([], arquivoImportacao);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(arquivoImportacao);
        });

        it('should not add a ArquivoImportacao to an array that contains it', () => {
          const arquivoImportacao: IArquivoImportacao = { id: 123 };
          const arquivoImportacaoCollection: IArquivoImportacao[] = [
            {
              ...arquivoImportacao,
            },
            { id: 456 },
          ];
          expectedResult = service.addArquivoImportacaoToCollectionIfMissing(arquivoImportacaoCollection, arquivoImportacao);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ArquivoImportacao to an array that doesn't contain it", () => {
          const arquivoImportacao: IArquivoImportacao = { id: 123 };
          const arquivoImportacaoCollection: IArquivoImportacao[] = [{ id: 456 }];
          expectedResult = service.addArquivoImportacaoToCollectionIfMissing(arquivoImportacaoCollection, arquivoImportacao);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(arquivoImportacao);
        });

        it('should add only unique ArquivoImportacao to an array', () => {
          const arquivoImportacaoArray: IArquivoImportacao[] = [{ id: 123 }, { id: 456 }, { id: 22917 }];
          const arquivoImportacaoCollection: IArquivoImportacao[] = [{ id: 123 }];
          expectedResult = service.addArquivoImportacaoToCollectionIfMissing(arquivoImportacaoCollection, ...arquivoImportacaoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const arquivoImportacao: IArquivoImportacao = { id: 123 };
          const arquivoImportacao2: IArquivoImportacao = { id: 456 };
          expectedResult = service.addArquivoImportacaoToCollectionIfMissing([], arquivoImportacao, arquivoImportacao2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(arquivoImportacao);
          expect(expectedResult).toContain(arquivoImportacao2);
        });

        it('should accept null and undefined values', () => {
          const arquivoImportacao: IArquivoImportacao = { id: 123 };
          expectedResult = service.addArquivoImportacaoToCollectionIfMissing([], null, arquivoImportacao, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(arquivoImportacao);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
