package br.com.consiga.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstadoMapperTest {

    private EstadoMapper estadoMapper;

    @BeforeEach
    public void setUp() {
        estadoMapper = new EstadoMapperImpl();
    }
}
