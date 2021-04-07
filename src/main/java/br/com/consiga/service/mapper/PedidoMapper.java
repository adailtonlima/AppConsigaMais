package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.PedidoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pedido} and its DTO {@link PedidoDTO}.
 */
@Mapper(componentModel = "spring", uses = { FuncionarioMapper.class, EmpresaMapper.class, FilialMapper.class, AdministradorMapper.class })
public interface PedidoMapper extends EntityMapper<PedidoDTO, Pedido> {
    @Mapping(target = "funcionario", source = "funcionario", qualifiedByName = "cpf")
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "cpf")
    @Mapping(target = "filia", source = "filia", qualifiedByName = "cpf")
    @Mapping(target = "quemAprovou", source = "quemAprovou", qualifiedByName = "nome")
    PedidoDTO toDto(Pedido s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PedidoDTO toDtoId(Pedido pedido);
}
