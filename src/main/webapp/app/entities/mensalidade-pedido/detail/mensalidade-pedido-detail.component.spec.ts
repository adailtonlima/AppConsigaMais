import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MensalidadePedidoDetailComponent } from './mensalidade-pedido-detail.component';

describe('Component Tests', () => {
  describe('MensalidadePedido Management Detail Component', () => {
    let comp: MensalidadePedidoDetailComponent;
    let fixture: ComponentFixture<MensalidadePedidoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MensalidadePedidoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mensalidadePedido: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MensalidadePedidoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MensalidadePedidoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mensalidadePedido on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mensalidadePedido).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
