package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.AdministradorDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrador} and its DTO {@link AdministradorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AdministradorMapper extends EntityMapper<AdministradorDTO, Administrador> {
    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AdministradorDTO toDtoNome(Administrador administrador);

    @Named("nomeSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    Set<AdministradorDTO> toDtoNomeSet(Set<Administrador> administrador);
}
