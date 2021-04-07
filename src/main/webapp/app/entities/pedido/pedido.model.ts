import * as dayjs from 'dayjs';
import { IFuncionario } from 'app/entities/funcionario/funcionario.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IFilial } from 'app/entities/filial/filial.model';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { StatusPedido } from 'app/entities/enumerations/status-pedido.model';

export interface IPedido {
  id?: number;
  estado?: StatusPedido | null;
  criado?: dayjs.Dayjs | null;
  dataAprovacao?: dayjs.Dayjs | null;
  dataExpiracao?: dayjs.Dayjs | null;
  renda?: number | null;
  valorSolicitado?: number | null;
  qtParcelasSolicitado?: number | null;
  valorAprovado?: number | null;
  valorParcelaAprovado?: number | null;
  qtParcelasAprovado?: number | null;
  dataPrimeiroVencimento?: dayjs.Dayjs | null;
  dataUltimoVencimento?: dayjs.Dayjs | null;
  funcionario?: IFuncionario | null;
  empresa?: IEmpresa | null;
  filia?: IFilial | null;
  quemAprovou?: IAdministrador | null;
}

export class Pedido implements IPedido {
  constructor(
    public id?: number,
    public estado?: StatusPedido | null,
    public criado?: dayjs.Dayjs | null,
    public dataAprovacao?: dayjs.Dayjs | null,
    public dataExpiracao?: dayjs.Dayjs | null,
    public renda?: number | null,
    public valorSolicitado?: number | null,
    public qtParcelasSolicitado?: number | null,
    public valorAprovado?: number | null,
    public valorParcelaAprovado?: number | null,
    public qtParcelasAprovado?: number | null,
    public dataPrimeiroVencimento?: dayjs.Dayjs | null,
    public dataUltimoVencimento?: dayjs.Dayjs | null,
    public funcionario?: IFuncionario | null,
    public empresa?: IEmpresa | null,
    public filia?: IFilial | null,
    public quemAprovou?: IAdministrador | null
  ) {}
}

export function getPedidoIdentifier(pedido: IPedido): number | undefined {
  return pedido.id;
}
