package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.CidadeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cidade} and its DTO {@link CidadeDTO}.
 */
@Mapper(componentModel = "spring", uses = { EstadoMapper.class })
public interface CidadeMapper extends EntityMapper<CidadeDTO, Cidade> {
    @Mapping(target = "estado", source = "estado", qualifiedByName = "nome")
    CidadeDTO toDto(Cidade s);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CidadeDTO toDtoNome(Cidade cidade);
}
