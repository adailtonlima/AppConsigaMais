package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.EmpresaDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empresa} and its DTO {@link EmpresaDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdministradorMapper.class })
public interface EmpresaMapper extends EntityMapper<EmpresaDTO, Empresa> {
    @Mapping(target = "administradores", source = "administradores", qualifiedByName = "nomeSet")
    EmpresaDTO toDto(Empresa s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpresaDTO toDtoId(Empresa empresa);

    @Mapping(target = "removeAdministradores", ignore = true)
    Empresa toEntity(EmpresaDTO empresaDTO);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EmpresaDTO toDtoNome(Empresa empresa);
}
