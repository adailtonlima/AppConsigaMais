package br.com.consiga.service.mapper;

import br.com.consiga.domain.*;
import br.com.consiga.service.dto.ArquivoImportacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArquivoImportacao} and its DTO {@link ArquivoImportacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdministradorMapper.class, EmpresaMapper.class, FilialMapper.class })
public interface ArquivoImportacaoMapper extends EntityMapper<ArquivoImportacaoDTO, ArquivoImportacao> {
    @Mapping(target = "criador", source = "criador", qualifiedByName = "nome")
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "nome")
    @Mapping(target = "filial", source = "filial", qualifiedByName = "nome")
    ArquivoImportacaoDTO toDto(ArquivoImportacao s);
}
