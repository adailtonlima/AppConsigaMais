import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FuncionarioDetailComponent } from './funcionario-detail.component';

describe('Component Tests', () => {
  describe('Funcionario Management Detail Component', () => {
    let comp: FuncionarioDetailComponent;
    let fixture: ComponentFixture<FuncionarioDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FuncionarioDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ funcionario: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FuncionarioDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FuncionarioDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load funcionario on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.funcionario).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
