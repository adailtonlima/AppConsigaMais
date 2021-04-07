import { IAdministrador } from 'app/entities/administrador/administrador.model';

export interface IEmpresa {
  id?: number;
  nome?: string;
  cnpj?: string | null;
  razaoSocial?: string | null;
  administradores?: IAdministrador[] | null;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public nome?: string,
    public cnpj?: string | null,
    public razaoSocial?: string | null,
    public administradores?: IAdministrador[] | null
  ) {}
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
