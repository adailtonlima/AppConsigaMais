package br.com.consiga.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FaturaMensalMapperTest {

    private FaturaMensalMapper faturaMensalMapper;

    @BeforeEach
    public void setUp() {
        faturaMensalMapper = new FaturaMensalMapperImpl();
    }
}
