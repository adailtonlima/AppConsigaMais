package br.com.consiga.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FilialMapperTest {

    private FilialMapper filialMapper;

    @BeforeEach
    public void setUp() {
        filialMapper = new FilialMapperImpl();
    }
}
