import * as dayjs from 'dayjs';
import { IPedido } from 'app/entities/pedido/pedido.model';
import { IFaturaMensal } from 'app/entities/fatura-mensal/fatura-mensal.model';

export interface IMensalidadePedido {
  id?: number;
  nParcela?: number | null;
  valor?: number | null;
  criado?: dayjs.Dayjs | null;
  valorParcial?: number | null;
  pedido?: IPedido | null;
  fatura?: IFaturaMensal | null;
}

export class MensalidadePedido implements IMensalidadePedido {
  constructor(
    public id?: number,
    public nParcela?: number | null,
    public valor?: number | null,
    public criado?: dayjs.Dayjs | null,
    public valorParcial?: number | null,
    public pedido?: IPedido | null,
    public fatura?: IFaturaMensal | null
  ) {}
}

export function getMensalidadePedidoIdentifier(mensalidadePedido: IMensalidadePedido): number | undefined {
  return mensalidadePedido.id;
}
