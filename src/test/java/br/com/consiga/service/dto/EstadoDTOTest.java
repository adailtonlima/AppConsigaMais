package br.com.consiga.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoDTO.class);
        EstadoDTO estadoDTO1 = new EstadoDTO();
        estadoDTO1.setId(1L);
        EstadoDTO estadoDTO2 = new EstadoDTO();
        assertThat(estadoDTO1).isNotEqualTo(estadoDTO2);
        estadoDTO2.setId(estadoDTO1.getId());
        assertThat(estadoDTO1).isEqualTo(estadoDTO2);
        estadoDTO2.setId(2L);
        assertThat(estadoDTO1).isNotEqualTo(estadoDTO2);
        estadoDTO1.setId(null);
        assertThat(estadoDTO1).isNotEqualTo(estadoDTO2);
    }
}
