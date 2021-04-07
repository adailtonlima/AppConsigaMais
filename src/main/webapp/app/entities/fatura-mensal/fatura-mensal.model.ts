import * as dayjs from 'dayjs';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IFilial } from 'app/entities/filial/filial.model';

export interface IFaturaMensal {
  id?: number;
  mes?: dayjs.Dayjs | null;
  criado?: dayjs.Dayjs | null;
  boletoUrl?: string | null;
  dataPago?: dayjs.Dayjs | null;
  empresa?: IEmpresa | null;
  filial?: IFilial | null;
}

export class FaturaMensal implements IFaturaMensal {
  constructor(
    public id?: number,
    public mes?: dayjs.Dayjs | null,
    public criado?: dayjs.Dayjs | null,
    public boletoUrl?: string | null,
    public dataPago?: dayjs.Dayjs | null,
    public empresa?: IEmpresa | null,
    public filial?: IFilial | null
  ) {}
}

export function getFaturaMensalIdentifier(faturaMensal: IFaturaMensal): number | undefined {
  return faturaMensal.id;
}
