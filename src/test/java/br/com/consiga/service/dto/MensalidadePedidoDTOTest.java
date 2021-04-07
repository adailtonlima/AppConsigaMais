package br.com.consiga.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MensalidadePedidoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MensalidadePedidoDTO.class);
        MensalidadePedidoDTO mensalidadePedidoDTO1 = new MensalidadePedidoDTO();
        mensalidadePedidoDTO1.setId(1L);
        MensalidadePedidoDTO mensalidadePedidoDTO2 = new MensalidadePedidoDTO();
        assertThat(mensalidadePedidoDTO1).isNotEqualTo(mensalidadePedidoDTO2);
        mensalidadePedidoDTO2.setId(mensalidadePedidoDTO1.getId());
        assertThat(mensalidadePedidoDTO1).isEqualTo(mensalidadePedidoDTO2);
        mensalidadePedidoDTO2.setId(2L);
        assertThat(mensalidadePedidoDTO1).isNotEqualTo(mensalidadePedidoDTO2);
        mensalidadePedidoDTO1.setId(null);
        assertThat(mensalidadePedidoDTO1).isNotEqualTo(mensalidadePedidoDTO2);
    }
}
