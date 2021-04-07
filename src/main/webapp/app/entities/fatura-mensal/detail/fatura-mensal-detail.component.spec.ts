import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FaturaMensalDetailComponent } from './fatura-mensal-detail.component';

describe('Component Tests', () => {
  describe('FaturaMensal Management Detail Component', () => {
    let comp: FaturaMensalDetailComponent;
    let fixture: ComponentFixture<FaturaMensalDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FaturaMensalDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ faturaMensal: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FaturaMensalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FaturaMensalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load faturaMensal on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.faturaMensal).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
