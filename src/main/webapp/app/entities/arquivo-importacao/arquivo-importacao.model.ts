import * as dayjs from 'dayjs';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IFilial } from 'app/entities/filial/filial.model';
import { StatusArquivo } from 'app/entities/enumerations/status-arquivo.model';

export interface IArquivoImportacao {
  id?: number;
  urlArquivo?: string | null;
  urlCriticas?: string | null;
  criado?: dayjs.Dayjs | null;
  estado?: StatusArquivo | null;
  processado?: dayjs.Dayjs | null;
  criador?: IAdministrador | null;
  empresa?: IEmpresa | null;
  filial?: IFilial | null;
}

export class ArquivoImportacao implements IArquivoImportacao {
  constructor(
    public id?: number,
    public urlArquivo?: string | null,
    public urlCriticas?: string | null,
    public criado?: dayjs.Dayjs | null,
    public estado?: StatusArquivo | null,
    public processado?: dayjs.Dayjs | null,
    public criador?: IAdministrador | null,
    public empresa?: IEmpresa | null,
    public filial?: IFilial | null
  ) {}
}

export function getArquivoImportacaoIdentifier(arquivoImportacao: IArquivoImportacao): number | undefined {
  return arquivoImportacao.id;
}
