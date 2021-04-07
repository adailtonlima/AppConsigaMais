package br.com.consiga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filial.class);
        Filial filial1 = new Filial();
        filial1.setId(1L);
        Filial filial2 = new Filial();
        filial2.setId(filial1.getId());
        assertThat(filial1).isEqualTo(filial2);
        filial2.setId(2L);
        assertThat(filial1).isNotEqualTo(filial2);
        filial1.setId(null);
        assertThat(filial1).isNotEqualTo(filial2);
    }
}
