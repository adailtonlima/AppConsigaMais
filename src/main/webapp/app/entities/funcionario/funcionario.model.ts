import * as dayjs from 'dayjs';

export interface IFuncionario {
  id?: number;
  nome?: string;
  cpf?: string;
  criado?: dayjs.Dayjs | null;
  atualizado?: dayjs.Dayjs | null;
  ultimoLogin?: dayjs.Dayjs | null;
}

export class Funcionario implements IFuncionario {
  constructor(
    public id?: number,
    public nome?: string,
    public cpf?: string,
    public criado?: dayjs.Dayjs | null,
    public atualizado?: dayjs.Dayjs | null,
    public ultimoLogin?: dayjs.Dayjs | null
  ) {}
}

export function getFuncionarioIdentifier(funcionario: IFuncionario): number | undefined {
  return funcionario.id;
}
