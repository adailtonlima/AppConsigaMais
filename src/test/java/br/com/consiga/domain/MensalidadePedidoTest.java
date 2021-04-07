package br.com.consiga.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.consiga.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MensalidadePedidoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MensalidadePedido.class);
        MensalidadePedido mensalidadePedido1 = new MensalidadePedido();
        mensalidadePedido1.setId(1L);
        MensalidadePedido mensalidadePedido2 = new MensalidadePedido();
        mensalidadePedido2.setId(mensalidadePedido1.getId());
        assertThat(mensalidadePedido1).isEqualTo(mensalidadePedido2);
        mensalidadePedido2.setId(2L);
        assertThat(mensalidadePedido1).isNotEqualTo(mensalidadePedido2);
        mensalidadePedido1.setId(null);
        assertThat(mensalidadePedido1).isNotEqualTo(mensalidadePedido2);
    }
}
