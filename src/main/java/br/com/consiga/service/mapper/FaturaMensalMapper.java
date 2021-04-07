package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.FaturaMensalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FaturaMensal} and its DTO {@link FaturaMensalDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmpresaMapper.class, FilialMapper.class })
public interface FaturaMensalMapper extends EntityMapper<FaturaMensalDTO, FaturaMensal> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "nome")
    @Mapping(target = "filial", source = "filial", qualifiedByName = "nome")
    FaturaMensalDTO toDto(FaturaMensal s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FaturaMensalDTO toDtoId(FaturaMensal faturaMensal);
}
