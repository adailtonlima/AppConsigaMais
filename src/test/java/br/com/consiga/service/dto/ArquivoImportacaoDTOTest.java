package br.com.consiga.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArquivoImportacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoImportacaoDTO.class);
        ArquivoImportacaoDTO arquivoImportacaoDTO1 = new ArquivoImportacaoDTO();
        arquivoImportacaoDTO1.setId(1L);
        ArquivoImportacaoDTO arquivoImportacaoDTO2 = new ArquivoImportacaoDTO();
        assertThat(arquivoImportacaoDTO1).isNotEqualTo(arquivoImportacaoDTO2);
        arquivoImportacaoDTO2.setId(arquivoImportacaoDTO1.getId());
        assertThat(arquivoImportacaoDTO1).isEqualTo(arquivoImportacaoDTO2);
        arquivoImportacaoDTO2.setId(2L);
        assertThat(arquivoImportacaoDTO1).isNotEqualTo(arquivoImportacaoDTO2);
        arquivoImportacaoDTO1.setId(null);
        assertThat(arquivoImportacaoDTO1).isNotEqualTo(arquivoImportacaoDTO2);
    }
}
