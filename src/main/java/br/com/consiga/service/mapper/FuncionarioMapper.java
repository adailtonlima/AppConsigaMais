package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.FuncionarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Funcionario} and its DTO {@link FuncionarioDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FuncionarioMapper extends EntityMapper<FuncionarioDTO, Funcionario> {
    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    FuncionarioDTO toDtoNome(Funcionario funcionario);

    @Named("cpf")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cpf", source = "cpf")
    FuncionarioDTO toDtoCpf(Funcionario funcionario);
}
