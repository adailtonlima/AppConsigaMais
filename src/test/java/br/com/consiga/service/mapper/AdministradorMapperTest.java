package br.com.consiga.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdministradorMapperTest {

    private AdministradorMapper administradorMapper;

    @BeforeEach
    public void setUp() {
        administradorMapper = new AdministradorMapperImpl();
    }
}
