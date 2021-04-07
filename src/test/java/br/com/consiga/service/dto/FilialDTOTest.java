package br.com.consiga.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilialDTO.class);
        FilialDTO filialDTO1 = new FilialDTO();
        filialDTO1.setId(1L);
        FilialDTO filialDTO2 = new FilialDTO();
        assertThat(filialDTO1).isNotEqualTo(filialDTO2);
        filialDTO2.setId(filialDTO1.getId());
        assertThat(filialDTO1).isEqualTo(filialDTO2);
        filialDTO2.setId(2L);
        assertThat(filialDTO1).isNotEqualTo(filialDTO2);
        filialDTO1.setId(null);
        assertThat(filialDTO1).isNotEqualTo(filialDTO2);
    }
}
