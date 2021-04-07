import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMensalidadePedido, MensalidadePedido } from '../mensalidade-pedido.model';

import { MensalidadePedidoService } from './mensalidade-pedido.service';

describe('Service Tests', () => {
  describe('MensalidadePedido Service', () => {
    let service: MensalidadePedidoService;
    let httpMock: HttpTestingController;
    let elemDefault: IMensalidadePedido;
    let expectedResult: IMensalidadePedido | IMensalidadePedido[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MensalidadePedidoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nParcela: 0,
        valor: 0,
        criado: currentDate,
        valorParcial: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MensalidadePedido', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.create(new MensalidadePedido()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MensalidadePedido', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nParcela: 1,
            valor: 1,
            criado: currentDate.format(DATE_TIME_FORMAT),
            valorParcial: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MensalidadePedido', () => {
        const patchObject = Object.assign(
          {
            nParcela: 1,
            valor: 1,
            criado: currentDate.format(DATE_TIME_FORMAT),
            valorParcial: 1,
          },
          new MensalidadePedido()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MensalidadePedido', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nParcela: 1,
            valor: 1,
            criado: currentDate.format(DATE_TIME_FORMAT),
            valorParcial: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MensalidadePedido', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMensalidadePedidoToCollectionIfMissing', () => {
        it('should add a MensalidadePedido to an empty array', () => {
          const mensalidadePedido: IMensalidadePedido = { id: 123 };
          expectedResult = service.addMensalidadePedidoToCollectionIfMissing([], mensalidadePedido);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mensalidadePedido);
        });

        it('should not add a MensalidadePedido to an array that contains it', () => {
          const mensalidadePedido: IMensalidadePedido = { id: 123 };
          const mensalidadePedidoCollection: IMensalidadePedido[] = [
            {
              ...mensalidadePedido,
            },
            { id: 456 },
          ];
          expectedResult = service.addMensalidadePedidoToCollectionIfMissing(mensalidadePedidoCollection, mensalidadePedido);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MensalidadePedido to an array that doesn't contain it", () => {
          const mensalidadePedido: IMensalidadePedido = { id: 123 };
          const mensalidadePedidoCollection: IMensalidadePedido[] = [{ id: 456 }];
          expectedResult = service.addMensalidadePedidoToCollectionIfMissing(mensalidadePedidoCollection, mensalidadePedido);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mensalidadePedido);
        });

        it('should add only unique MensalidadePedido to an array', () => {
          const mensalidadePedidoArray: IMensalidadePedido[] = [{ id: 123 }, { id: 456 }, { id: 15226 }];
          const mensalidadePedidoCollection: IMensalidadePedido[] = [{ id: 123 }];
          expectedResult = service.addMensalidadePedidoToCollectionIfMissing(mensalidadePedidoCollection, ...mensalidadePedidoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mensalidadePedido: IMensalidadePedido = { id: 123 };
          const mensalidadePedido2: IMensalidadePedido = { id: 456 };
          expectedResult = service.addMensalidadePedidoToCollectionIfMissing([], mensalidadePedido, mensalidadePedido2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mensalidadePedido);
          expect(expectedResult).toContain(mensalidadePedido2);
        });

        it('should accept null and undefined values', () => {
          const mensalidadePedido: IMensalidadePedido = { id: 123 };
          expectedResult = service.addMensalidadePedidoToCollectionIfMissing([], null, mensalidadePedido, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mensalidadePedido);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
