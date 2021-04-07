package br.com.consiga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArquivoImportacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoImportacao.class);
        ArquivoImportacao arquivoImportacao1 = new ArquivoImportacao();
        arquivoImportacao1.setId(1L);
        ArquivoImportacao arquivoImportacao2 = new ArquivoImportacao();
        arquivoImportacao2.setId(arquivoImportacao1.getId());
        assertThat(arquivoImportacao1).isEqualTo(arquivoImportacao2);
        arquivoImportacao2.setId(2L);
        assertThat(arquivoImportacao1).isNotEqualTo(arquivoImportacao2);
        arquivoImportacao1.setId(null);
        assertThat(arquivoImportacao1).isNotEqualTo(arquivoImportacao2);
    }
}
