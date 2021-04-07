import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFilial } from '../filial.model';

@Component({
  selector: 'jhi-filial-detail',
  templateUrl: './filial-detail.component.html',
})
export class FilialDetailComponent implements OnInit {
  filial: IFilial | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filial }) => {
      this.filial = filial;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
