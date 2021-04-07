import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFaturaMensal, FaturaMensal } from '../fatura-mensal.model';

import { FaturaMensalService } from './fatura-mensal.service';

describe('Service Tests', () => {
  describe('FaturaMensal Service', () => {
    let service: FaturaMensalService;
    let httpMock: HttpTestingController;
    let elemDefault: IFaturaMensal;
    let expectedResult: IFaturaMensal | IFaturaMensal[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FaturaMensalService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        mes: currentDate,
        criado: currentDate,
        boletoUrl: 'AAAAAAA',
        dataPago: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            mes: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
            dataPago: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FaturaMensal', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            mes: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
            dataPago: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            mes: currentDate,
            criado: currentDate,
            dataPago: currentDate,
          },
          returnedFromService
        );

        service.create(new FaturaMensal()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FaturaMensal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mes: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
            boletoUrl: 'BBBBBB',
            dataPago: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            mes: currentDate,
            criado: currentDate,
            dataPago: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FaturaMensal', () => {
        const patchObject = Object.assign(
          {
            boletoUrl: 'BBBBBB',
          },
          new FaturaMensal()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            mes: currentDate,
            criado: currentDate,
            dataPago: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FaturaMensal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mes: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
            boletoUrl: 'BBBBBB',
            dataPago: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            mes: currentDate,
            criado: currentDate,
            dataPago: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FaturaMensal', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFaturaMensalToCollectionIfMissing', () => {
        it('should add a FaturaMensal to an empty array', () => {
          const faturaMensal: IFaturaMensal = { id: 123 };
          expectedResult = service.addFaturaMensalToCollectionIfMissing([], faturaMensal);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(faturaMensal);
        });

        it('should not add a FaturaMensal to an array that contains it', () => {
          const faturaMensal: IFaturaMensal = { id: 123 };
          const faturaMensalCollection: IFaturaMensal[] = [
            {
              ...faturaMensal,
            },
            { id: 456 },
          ];
          expectedResult = service.addFaturaMensalToCollectionIfMissing(faturaMensalCollection, faturaMensal);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FaturaMensal to an array that doesn't contain it", () => {
          const faturaMensal: IFaturaMensal = { id: 123 };
          const faturaMensalCollection: IFaturaMensal[] = [{ id: 456 }];
          expectedResult = service.addFaturaMensalToCollectionIfMissing(faturaMensalCollection, faturaMensal);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(faturaMensal);
        });

        it('should add only unique FaturaMensal to an array', () => {
          const faturaMensalArray: IFaturaMensal[] = [{ id: 123 }, { id: 456 }, { id: 33903 }];
          const faturaMensalCollection: IFaturaMensal[] = [{ id: 123 }];
          expectedResult = service.addFaturaMensalToCollectionIfMissing(faturaMensalCollection, ...faturaMensalArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const faturaMensal: IFaturaMensal = { id: 123 };
          const faturaMensal2: IFaturaMensal = { id: 456 };
          expectedResult = service.addFaturaMensalToCollectionIfMissing([], faturaMensal, faturaMensal2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(faturaMensal);
          expect(expectedResult).toContain(faturaMensal2);
        });

        it('should accept null and undefined values', () => {
          const faturaMensal: IFaturaMensal = { id: 123 };
          expectedResult = service.addFaturaMensalToCollectionIfMissing([], null, faturaMensal, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(faturaMensal);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
