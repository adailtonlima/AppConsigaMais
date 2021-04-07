package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.FilialDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Filial} and its DTO {@link FilialDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmpresaMapper.class, AdministradorMapper.class })
public interface FilialMapper extends EntityMapper<FilialDTO, Filial> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "nome")
    @Mapping(target = "administradores", source = "administradores", qualifiedByName = "nomeSet")
    FilialDTO toDto(Filial s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FilialDTO toDtoId(Filial filial);

    @Mapping(target = "removeAdministradores", ignore = true)
    Filial toEntity(FilialDTO filialDTO);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    FilialDTO toDtoNome(Filial filial);
}
