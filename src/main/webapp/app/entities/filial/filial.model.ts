import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IAdministrador } from 'app/entities/administrador/administrador.model';

export interface IFilial {
  id?: number;
  nome?: string | null;
  codigo?: string | null;
  cnpj?: string | null;
  empresa?: IEmpresa | null;
  administradores?: IAdministrador[] | null;
}

export class Filial implements IFilial {
  constructor(
    public id?: number,
    public nome?: string | null,
    public codigo?: string | null,
    public cnpj?: string | null,
    public empresa?: IEmpresa | null,
    public administradores?: IAdministrador[] | null
  ) {}
}

export function getFilialIdentifier(filial: IFilial): number | undefined {
  return filial.id;
}
