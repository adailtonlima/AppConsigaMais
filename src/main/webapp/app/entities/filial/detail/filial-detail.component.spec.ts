import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FilialDetailComponent } from './filial-detail.component';

describe('Component Tests', () => {
  describe('Filial Management Detail Component', () => {
    let comp: FilialDetailComponent;
    let fixture: ComponentFixture<FilialDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FilialDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ filial: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FilialDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FilialDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load filial on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.filial).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
