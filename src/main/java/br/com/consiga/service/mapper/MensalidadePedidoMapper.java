package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.MensalidadePedidoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MensalidadePedido} and its DTO {@link MensalidadePedidoDTO}.
 */
@Mapper(componentModel = "spring", uses = { PedidoMapper.class, FaturaMensalMapper.class })
public interface MensalidadePedidoMapper extends EntityMapper<MensalidadePedidoDTO, MensalidadePedido> {
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "id")
    @Mapping(target = "fatura", source = "fatura", qualifiedByName = "id")
    MensalidadePedidoDTO toDto(MensalidadePedido s);
}
