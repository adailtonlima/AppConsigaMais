import { ICidade } from 'app/entities/cidade/cidade.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IFilial } from 'app/entities/filial/filial.model';
import { IFuncionario } from 'app/entities/funcionario/funcionario.model';

export interface IEndereco {
  id?: number;
  nome?: string | null;
  cep?: string | null;
  logradouro?: string | null;
  numero?: string | null;
  referencia?: string | null;
  bairro?: string | null;
  cidade?: ICidade | null;
  empresa?: IEmpresa | null;
  filial?: IFilial | null;
  funcionario?: IFuncionario | null;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public nome?: string | null,
    public cep?: string | null,
    public logradouro?: string | null,
    public numero?: string | null,
    public referencia?: string | null,
    public bairro?: string | null,
    public cidade?: ICidade | null,
    public empresa?: IEmpresa | null,
    public filial?: IFilial | null,
    public funcionario?: IFuncionario | null
  ) {}
}

export function getEnderecoIdentifier(endereco: IEndereco): number | undefined {
  return endereco.id;
}
