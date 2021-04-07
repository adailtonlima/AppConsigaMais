import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFilial, Filial } from '../filial.model';

import { FilialService } from './filial.service';

describe('Service Tests', () => {
  describe('Filial Service', () => {
    let service: FilialService;
    let httpMock: HttpTestingController;
    let elemDefault: IFilial;
    let expectedResult: IFilial | IFilial[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FilialService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        codigo: 'AAAAAAA',
        cnpj: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Filial', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Filial()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Filial', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            codigo: 'BBBBBB',
            cnpj: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Filial', () => {
        const patchObject = Object.assign(
          {
            nome: 'BBBBBB',
          },
          new Filial()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Filial', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            codigo: 'BBBBBB',
            cnpj: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Filial', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFilialToCollectionIfMissing', () => {
        it('should add a Filial to an empty array', () => {
          const filial: IFilial = { id: 123 };
          expectedResult = service.addFilialToCollectionIfMissing([], filial);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(filial);
        });

        it('should not add a Filial to an array that contains it', () => {
          const filial: IFilial = { id: 123 };
          const filialCollection: IFilial[] = [
            {
              ...filial,
            },
            { id: 456 },
          ];
          expectedResult = service.addFilialToCollectionIfMissing(filialCollection, filial);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Filial to an array that doesn't contain it", () => {
          const filial: IFilial = { id: 123 };
          const filialCollection: IFilial[] = [{ id: 456 }];
          expectedResult = service.addFilialToCollectionIfMissing(filialCollection, filial);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(filial);
        });

        it('should add only unique Filial to an array', () => {
          const filialArray: IFilial[] = [{ id: 123 }, { id: 456 }, { id: 57202 }];
          const filialCollection: IFilial[] = [{ id: 123 }];
          expectedResult = service.addFilialToCollectionIfMissing(filialCollection, ...filialArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const filial: IFilial = { id: 123 };
          const filial2: IFilial = { id: 456 };
          expectedResult = service.addFilialToCollectionIfMissing([], filial, filial2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(filial);
          expect(expectedResult).toContain(filial2);
        });

        it('should accept null and undefined values', () => {
          const filial: IFilial = { id: 123 };
          expectedResult = service.addFilialToCollectionIfMissing([], null, filial, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(filial);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
