package br.com.consiga.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CidadeMapperTest {

    private CidadeMapper cidadeMapper;

    @BeforeEach
    public void setUp() {
        cidadeMapper = new CidadeMapperImpl();
    }
}
