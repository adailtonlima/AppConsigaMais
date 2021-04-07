package br.com.consiga.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MensalidadePedidoMapperTest {

    private MensalidadePedidoMapper mensalidadePedidoMapper;

    @BeforeEach
    public void setUp() {
        mensalidadePedidoMapper = new MensalidadePedidoMapperImpl();
    }
}
