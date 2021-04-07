package br.com.consiga.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArquivoImportacaoMapperTest {

    private ArquivoImportacaoMapper arquivoImportacaoMapper;

    @BeforeEach
    public void setUp() {
        arquivoImportacaoMapper = new ArquivoImportacaoMapperImpl();
    }
}
