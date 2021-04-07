import * as dayjs from 'dayjs';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IFilial } from 'app/entities/filial/filial.model';

export interface IAdministrador {
  id?: number;
  nome?: string | null;
  criado?: dayjs.Dayjs | null;
  atualizado?: dayjs.Dayjs | null;
  ultimoLogin?: dayjs.Dayjs | null;
  empresas?: IEmpresa[] | null;
  filiais?: IFilial[] | null;
}

export class Administrador implements IAdministrador {
  constructor(
    public id?: number,
    public nome?: string | null,
    public criado?: dayjs.Dayjs | null,
    public atualizado?: dayjs.Dayjs | null,
    public ultimoLogin?: dayjs.Dayjs | null,
    public empresas?: IEmpresa[] | null,
    public filiais?: IFilial[] | null
  ) {}
}

export function getAdministradorIdentifier(administrador: IAdministrador): number | undefined {
  return administrador.id;
}
