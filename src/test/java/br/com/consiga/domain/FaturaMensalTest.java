package br.com.consiga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FaturaMensalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FaturaMensal.class);
        FaturaMensal faturaMensal1 = new FaturaMensal();
        faturaMensal1.setId(1L);
        FaturaMensal faturaMensal2 = new FaturaMensal();
        faturaMensal2.setId(faturaMensal1.getId());
        assertThat(faturaMensal1).isEqualTo(faturaMensal2);
        faturaMensal2.setId(2L);
        assertThat(faturaMensal1).isNotEqualTo(faturaMensal2);
        faturaMensal1.setId(null);
        assertThat(faturaMensal1).isNotEqualTo(faturaMensal2);
    }
}
