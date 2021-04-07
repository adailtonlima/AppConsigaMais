import { IEstado } from 'app/entities/estado/estado.model';

export interface ICidade {
  id?: number;
  nome?: string;
  estado?: IEstado | null;
}

export class Cidade implements ICidade {
  constructor(public id?: number, public nome?: string, public estado?: IEstado | null) {}
}

export function getCidadeIdentifier(cidade: ICidade): number | undefined {
  return cidade.id;
}
