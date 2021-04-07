import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArquivoImportacaoDetailComponent } from './arquivo-importacao-detail.component';

describe('Component Tests', () => {
  describe('ArquivoImportacao Management Detail Component', () => {
    let comp: ArquivoImportacaoDetailComponent;
    let fixture: ComponentFixture<ArquivoImportacaoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ArquivoImportacaoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ arquivoImportacao: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ArquivoImportacaoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArquivoImportacaoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load arquivoImportacao on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.arquivoImportacao).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
