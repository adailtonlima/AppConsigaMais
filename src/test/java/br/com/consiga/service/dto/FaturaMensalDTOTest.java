package br.com.consiga.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FaturaMensalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FaturaMensalDTO.class);
        FaturaMensalDTO faturaMensalDTO1 = new FaturaMensalDTO();
        faturaMensalDTO1.setId(1L);
        FaturaMensalDTO faturaMensalDTO2 = new FaturaMensalDTO();
        assertThat(faturaMensalDTO1).isNotEqualTo(faturaMensalDTO2);
        faturaMensalDTO2.setId(faturaMensalDTO1.getId());
        assertThat(faturaMensalDTO1).isEqualTo(faturaMensalDTO2);
        faturaMensalDTO2.setId(2L);
        assertThat(faturaMensalDTO1).isNotEqualTo(faturaMensalDTO2);
        faturaMensalDTO1.setId(null);
        assertThat(faturaMensalDTO1).isNotEqualTo(faturaMensalDTO2);
    }
}
