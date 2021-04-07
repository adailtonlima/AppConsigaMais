package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.EnderecoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Endereco} and its DTO {@link EnderecoDTO}.
 */
@Mapper(componentModel = "spring", uses = { CidadeMapper.class, EmpresaMapper.class, FilialMapper.class, FuncionarioMapper.class })
public interface EnderecoMapper extends EntityMapper<EnderecoDTO, Endereco> {
    @Mapping(target = "cidade", source = "cidade", qualifiedByName = "nome")
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "nome")
    @Mapping(target = "filial", source = "filial", qualifiedByName = "nome")
    @Mapping(target = "funcionario", source = "funcionario", qualifiedByName = "nome")
    EnderecoDTO toDto(Endereco s);
}
