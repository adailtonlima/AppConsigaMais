package br.com.consiga.web.rest;

import static br.com.consiga.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.consiga.IntegrationTest;
import br.com.consiga.domain.Administrador;
import br.com.consiga.domain.ArquivoImportacao;
import br.com.consiga.domain.Empresa;
import br.com.consiga.domain.Filial;
import br.com.consiga.domain.enumeration.StatusArquivo;
import br.com.consiga.repository.ArquivoImportacaoRepository;
import br.com.consiga.service.criteria.ArquivoImportacaoCriteria;
import br.com.consiga.service.dto.ArquivoImportacaoDTO;
import br.com.consiga.service.mapper.ArquivoImportacaoMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArquivoImportacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArquivoImportacaoResourceIT {

    private static final String DEFAULT_URL_ARQUIVO = "AAAAAAAAAA";
    private static final String UPDATED_URL_ARQUIVO = "BBBBBBBBBB";

    private static final String DEFAULT_URL_CRITICAS = "AAAAAAAAAA";
    private static final String UPDATED_URL_CRITICAS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final StatusArquivo DEFAULT_ESTADO = StatusArquivo.ENVIADO;
    private static final StatusArquivo UPDATED_ESTADO = StatusArquivo.PROCESSANDO;

    private static final ZonedDateTime DEFAULT_PROCESSADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PROCESSADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PROCESSADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/arquivo-importacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArquivoImportacaoRepository arquivoImportacaoRepository;

    @Autowired
    private ArquivoImportacaoMapper arquivoImportacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArquivoImportacaoMockMvc;

    private ArquivoImportacao arquivoImportacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArquivoImportacao createEntity(EntityManager em) {
        ArquivoImportacao arquivoImportacao = new ArquivoImportacao()
            .urlArquivo(DEFAULT_URL_ARQUIVO)
            .urlCriticas(DEFAULT_URL_CRITICAS)
            .criado(DEFAULT_CRIADO)
            .estado(DEFAULT_ESTADO)
            .processado(DEFAULT_PROCESSADO);
        return arquivoImportacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArquivoImportacao createUpdatedEntity(EntityManager em) {
        ArquivoImportacao arquivoImportacao = new ArquivoImportacao()
            .urlArquivo(UPDATED_URL_ARQUIVO)
            .urlCriticas(UPDATED_URL_CRITICAS)
            .criado(UPDATED_CRIADO)
            .estado(UPDATED_ESTADO)
            .processado(UPDATED_PROCESSADO);
        return arquivoImportacao;
    }

    @BeforeEach
    public void initTest() {
        arquivoImportacao = createEntity(em);
    }

    @Test
    @Transactional
    void createArquivoImportacao() throws Exception {
        int databaseSizeBeforeCreate = arquivoImportacaoRepository.findAll().size();
        // Create the ArquivoImportacao
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);
        restArquivoImportacaoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeCreate + 1);
        ArquivoImportacao testArquivoImportacao = arquivoImportacaoList.get(arquivoImportacaoList.size() - 1);
        assertThat(testArquivoImportacao.getUrlArquivo()).isEqualTo(DEFAULT_URL_ARQUIVO);
        assertThat(testArquivoImportacao.getUrlCriticas()).isEqualTo(DEFAULT_URL_CRITICAS);
        assertThat(testArquivoImportacao.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testArquivoImportacao.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testArquivoImportacao.getProcessado()).isEqualTo(DEFAULT_PROCESSADO);
    }

    @Test
    @Transactional
    void createArquivoImportacaoWithExistingId() throws Exception {
        // Create the ArquivoImportacao with an existing ID
        arquivoImportacao.setId(1L);
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);

        int databaseSizeBeforeCreate = arquivoImportacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArquivoImportacaoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaos() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList
        restArquivoImportacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivoImportacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlArquivo").value(hasItem(DEFAULT_URL_ARQUIVO)))
            .andExpect(jsonPath("$.[*].urlCriticas").value(hasItem(DEFAULT_URL_CRITICAS)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].processado").value(hasItem(sameInstant(DEFAULT_PROCESSADO))));
    }

    @Test
    @Transactional
    void getArquivoImportacao() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get the arquivoImportacao
        restArquivoImportacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, arquivoImportacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arquivoImportacao.getId().intValue()))
            .andExpect(jsonPath("$.urlArquivo").value(DEFAULT_URL_ARQUIVO))
            .andExpect(jsonPath("$.urlCriticas").value(DEFAULT_URL_CRITICAS))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.processado").value(sameInstant(DEFAULT_PROCESSADO)));
    }

    @Test
    @Transactional
    void getArquivoImportacaosByIdFiltering() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        Long id = arquivoImportacao.getId();

        defaultArquivoImportacaoShouldBeFound("id.equals=" + id);
        defaultArquivoImportacaoShouldNotBeFound("id.notEquals=" + id);

        defaultArquivoImportacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArquivoImportacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultArquivoImportacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArquivoImportacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlArquivoIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlArquivo equals to DEFAULT_URL_ARQUIVO
        defaultArquivoImportacaoShouldBeFound("urlArquivo.equals=" + DEFAULT_URL_ARQUIVO);

        // Get all the arquivoImportacaoList where urlArquivo equals to UPDATED_URL_ARQUIVO
        defaultArquivoImportacaoShouldNotBeFound("urlArquivo.equals=" + UPDATED_URL_ARQUIVO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlArquivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlArquivo not equals to DEFAULT_URL_ARQUIVO
        defaultArquivoImportacaoShouldNotBeFound("urlArquivo.notEquals=" + DEFAULT_URL_ARQUIVO);

        // Get all the arquivoImportacaoList where urlArquivo not equals to UPDATED_URL_ARQUIVO
        defaultArquivoImportacaoShouldBeFound("urlArquivo.notEquals=" + UPDATED_URL_ARQUIVO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlArquivoIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlArquivo in DEFAULT_URL_ARQUIVO or UPDATED_URL_ARQUIVO
        defaultArquivoImportacaoShouldBeFound("urlArquivo.in=" + DEFAULT_URL_ARQUIVO + "," + UPDATED_URL_ARQUIVO);

        // Get all the arquivoImportacaoList where urlArquivo equals to UPDATED_URL_ARQUIVO
        defaultArquivoImportacaoShouldNotBeFound("urlArquivo.in=" + UPDATED_URL_ARQUIVO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlArquivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlArquivo is not null
        defaultArquivoImportacaoShouldBeFound("urlArquivo.specified=true");

        // Get all the arquivoImportacaoList where urlArquivo is null
        defaultArquivoImportacaoShouldNotBeFound("urlArquivo.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlArquivoContainsSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlArquivo contains DEFAULT_URL_ARQUIVO
        defaultArquivoImportacaoShouldBeFound("urlArquivo.contains=" + DEFAULT_URL_ARQUIVO);

        // Get all the arquivoImportacaoList where urlArquivo contains UPDATED_URL_ARQUIVO
        defaultArquivoImportacaoShouldNotBeFound("urlArquivo.contains=" + UPDATED_URL_ARQUIVO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlArquivoNotContainsSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlArquivo does not contain DEFAULT_URL_ARQUIVO
        defaultArquivoImportacaoShouldNotBeFound("urlArquivo.doesNotContain=" + DEFAULT_URL_ARQUIVO);

        // Get all the arquivoImportacaoList where urlArquivo does not contain UPDATED_URL_ARQUIVO
        defaultArquivoImportacaoShouldBeFound("urlArquivo.doesNotContain=" + UPDATED_URL_ARQUIVO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlCriticasIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlCriticas equals to DEFAULT_URL_CRITICAS
        defaultArquivoImportacaoShouldBeFound("urlCriticas.equals=" + DEFAULT_URL_CRITICAS);

        // Get all the arquivoImportacaoList where urlCriticas equals to UPDATED_URL_CRITICAS
        defaultArquivoImportacaoShouldNotBeFound("urlCriticas.equals=" + UPDATED_URL_CRITICAS);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlCriticasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlCriticas not equals to DEFAULT_URL_CRITICAS
        defaultArquivoImportacaoShouldNotBeFound("urlCriticas.notEquals=" + DEFAULT_URL_CRITICAS);

        // Get all the arquivoImportacaoList where urlCriticas not equals to UPDATED_URL_CRITICAS
        defaultArquivoImportacaoShouldBeFound("urlCriticas.notEquals=" + UPDATED_URL_CRITICAS);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlCriticasIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlCriticas in DEFAULT_URL_CRITICAS or UPDATED_URL_CRITICAS
        defaultArquivoImportacaoShouldBeFound("urlCriticas.in=" + DEFAULT_URL_CRITICAS + "," + UPDATED_URL_CRITICAS);

        // Get all the arquivoImportacaoList where urlCriticas equals to UPDATED_URL_CRITICAS
        defaultArquivoImportacaoShouldNotBeFound("urlCriticas.in=" + UPDATED_URL_CRITICAS);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlCriticasIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlCriticas is not null
        defaultArquivoImportacaoShouldBeFound("urlCriticas.specified=true");

        // Get all the arquivoImportacaoList where urlCriticas is null
        defaultArquivoImportacaoShouldNotBeFound("urlCriticas.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlCriticasContainsSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlCriticas contains DEFAULT_URL_CRITICAS
        defaultArquivoImportacaoShouldBeFound("urlCriticas.contains=" + DEFAULT_URL_CRITICAS);

        // Get all the arquivoImportacaoList where urlCriticas contains UPDATED_URL_CRITICAS
        defaultArquivoImportacaoShouldNotBeFound("urlCriticas.contains=" + UPDATED_URL_CRITICAS);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByUrlCriticasNotContainsSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where urlCriticas does not contain DEFAULT_URL_CRITICAS
        defaultArquivoImportacaoShouldNotBeFound("urlCriticas.doesNotContain=" + DEFAULT_URL_CRITICAS);

        // Get all the arquivoImportacaoList where urlCriticas does not contain UPDATED_URL_CRITICAS
        defaultArquivoImportacaoShouldBeFound("urlCriticas.doesNotContain=" + UPDATED_URL_CRITICAS);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado equals to DEFAULT_CRIADO
        defaultArquivoImportacaoShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the arquivoImportacaoList where criado equals to UPDATED_CRIADO
        defaultArquivoImportacaoShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado not equals to DEFAULT_CRIADO
        defaultArquivoImportacaoShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the arquivoImportacaoList where criado not equals to UPDATED_CRIADO
        defaultArquivoImportacaoShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultArquivoImportacaoShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the arquivoImportacaoList where criado equals to UPDATED_CRIADO
        defaultArquivoImportacaoShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado is not null
        defaultArquivoImportacaoShouldBeFound("criado.specified=true");

        // Get all the arquivoImportacaoList where criado is null
        defaultArquivoImportacaoShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado is greater than or equal to DEFAULT_CRIADO
        defaultArquivoImportacaoShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the arquivoImportacaoList where criado is greater than or equal to UPDATED_CRIADO
        defaultArquivoImportacaoShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado is less than or equal to DEFAULT_CRIADO
        defaultArquivoImportacaoShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the arquivoImportacaoList where criado is less than or equal to SMALLER_CRIADO
        defaultArquivoImportacaoShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado is less than DEFAULT_CRIADO
        defaultArquivoImportacaoShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the arquivoImportacaoList where criado is less than UPDATED_CRIADO
        defaultArquivoImportacaoShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where criado is greater than DEFAULT_CRIADO
        defaultArquivoImportacaoShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the arquivoImportacaoList where criado is greater than SMALLER_CRIADO
        defaultArquivoImportacaoShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where estado equals to DEFAULT_ESTADO
        defaultArquivoImportacaoShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the arquivoImportacaoList where estado equals to UPDATED_ESTADO
        defaultArquivoImportacaoShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where estado not equals to DEFAULT_ESTADO
        defaultArquivoImportacaoShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the arquivoImportacaoList where estado not equals to UPDATED_ESTADO
        defaultArquivoImportacaoShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultArquivoImportacaoShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the arquivoImportacaoList where estado equals to UPDATED_ESTADO
        defaultArquivoImportacaoShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where estado is not null
        defaultArquivoImportacaoShouldBeFound("estado.specified=true");

        // Get all the arquivoImportacaoList where estado is null
        defaultArquivoImportacaoShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado equals to DEFAULT_PROCESSADO
        defaultArquivoImportacaoShouldBeFound("processado.equals=" + DEFAULT_PROCESSADO);

        // Get all the arquivoImportacaoList where processado equals to UPDATED_PROCESSADO
        defaultArquivoImportacaoShouldNotBeFound("processado.equals=" + UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado not equals to DEFAULT_PROCESSADO
        defaultArquivoImportacaoShouldNotBeFound("processado.notEquals=" + DEFAULT_PROCESSADO);

        // Get all the arquivoImportacaoList where processado not equals to UPDATED_PROCESSADO
        defaultArquivoImportacaoShouldBeFound("processado.notEquals=" + UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsInShouldWork() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado in DEFAULT_PROCESSADO or UPDATED_PROCESSADO
        defaultArquivoImportacaoShouldBeFound("processado.in=" + DEFAULT_PROCESSADO + "," + UPDATED_PROCESSADO);

        // Get all the arquivoImportacaoList where processado equals to UPDATED_PROCESSADO
        defaultArquivoImportacaoShouldNotBeFound("processado.in=" + UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado is not null
        defaultArquivoImportacaoShouldBeFound("processado.specified=true");

        // Get all the arquivoImportacaoList where processado is null
        defaultArquivoImportacaoShouldNotBeFound("processado.specified=false");
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado is greater than or equal to DEFAULT_PROCESSADO
        defaultArquivoImportacaoShouldBeFound("processado.greaterThanOrEqual=" + DEFAULT_PROCESSADO);

        // Get all the arquivoImportacaoList where processado is greater than or equal to UPDATED_PROCESSADO
        defaultArquivoImportacaoShouldNotBeFound("processado.greaterThanOrEqual=" + UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado is less than or equal to DEFAULT_PROCESSADO
        defaultArquivoImportacaoShouldBeFound("processado.lessThanOrEqual=" + DEFAULT_PROCESSADO);

        // Get all the arquivoImportacaoList where processado is less than or equal to SMALLER_PROCESSADO
        defaultArquivoImportacaoShouldNotBeFound("processado.lessThanOrEqual=" + SMALLER_PROCESSADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsLessThanSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado is less than DEFAULT_PROCESSADO
        defaultArquivoImportacaoShouldNotBeFound("processado.lessThan=" + DEFAULT_PROCESSADO);

        // Get all the arquivoImportacaoList where processado is less than UPDATED_PROCESSADO
        defaultArquivoImportacaoShouldBeFound("processado.lessThan=" + UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByProcessadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        // Get all the arquivoImportacaoList where processado is greater than DEFAULT_PROCESSADO
        defaultArquivoImportacaoShouldNotBeFound("processado.greaterThan=" + DEFAULT_PROCESSADO);

        // Get all the arquivoImportacaoList where processado is greater than SMALLER_PROCESSADO
        defaultArquivoImportacaoShouldBeFound("processado.greaterThan=" + SMALLER_PROCESSADO);
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByCriadorIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);
        Administrador criador = AdministradorResourceIT.createEntity(em);
        em.persist(criador);
        em.flush();
        arquivoImportacao.setCriador(criador);
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);
        Long criadorId = criador.getId();

        // Get all the arquivoImportacaoList where criador equals to criadorId
        defaultArquivoImportacaoShouldBeFound("criadorId.equals=" + criadorId);

        // Get all the arquivoImportacaoList where criador equals to (criadorId + 1)
        defaultArquivoImportacaoShouldNotBeFound("criadorId.equals=" + (criadorId + 1));
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);
        Empresa empresa = EmpresaResourceIT.createEntity(em);
        em.persist(empresa);
        em.flush();
        arquivoImportacao.setEmpresa(empresa);
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);
        Long empresaId = empresa.getId();

        // Get all the arquivoImportacaoList where empresa equals to empresaId
        defaultArquivoImportacaoShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the arquivoImportacaoList where empresa equals to (empresaId + 1)
        defaultArquivoImportacaoShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllArquivoImportacaosByFilialIsEqualToSomething() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);
        Filial filial = FilialResourceIT.createEntity(em);
        em.persist(filial);
        em.flush();
        arquivoImportacao.setFilial(filial);
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);
        Long filialId = filial.getId();

        // Get all the arquivoImportacaoList where filial equals to filialId
        defaultArquivoImportacaoShouldBeFound("filialId.equals=" + filialId);

        // Get all the arquivoImportacaoList where filial equals to (filialId + 1)
        defaultArquivoImportacaoShouldNotBeFound("filialId.equals=" + (filialId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArquivoImportacaoShouldBeFound(String filter) throws Exception {
        restArquivoImportacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivoImportacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlArquivo").value(hasItem(DEFAULT_URL_ARQUIVO)))
            .andExpect(jsonPath("$.[*].urlCriticas").value(hasItem(DEFAULT_URL_CRITICAS)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].processado").value(hasItem(sameInstant(DEFAULT_PROCESSADO))));

        // Check, that the count call also returns 1
        restArquivoImportacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArquivoImportacaoShouldNotBeFound(String filter) throws Exception {
        restArquivoImportacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArquivoImportacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArquivoImportacao() throws Exception {
        // Get the arquivoImportacao
        restArquivoImportacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArquivoImportacao() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();

        // Update the arquivoImportacao
        ArquivoImportacao updatedArquivoImportacao = arquivoImportacaoRepository.findById(arquivoImportacao.getId()).get();
        // Disconnect from session so that the updates on updatedArquivoImportacao are not directly saved in db
        em.detach(updatedArquivoImportacao);
        updatedArquivoImportacao
            .urlArquivo(UPDATED_URL_ARQUIVO)
            .urlCriticas(UPDATED_URL_CRITICAS)
            .criado(UPDATED_CRIADO)
            .estado(UPDATED_ESTADO)
            .processado(UPDATED_PROCESSADO);
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(updatedArquivoImportacao);

        restArquivoImportacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arquivoImportacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
        ArquivoImportacao testArquivoImportacao = arquivoImportacaoList.get(arquivoImportacaoList.size() - 1);
        assertThat(testArquivoImportacao.getUrlArquivo()).isEqualTo(UPDATED_URL_ARQUIVO);
        assertThat(testArquivoImportacao.getUrlCriticas()).isEqualTo(UPDATED_URL_CRITICAS);
        assertThat(testArquivoImportacao.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testArquivoImportacao.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testArquivoImportacao.getProcessado()).isEqualTo(UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void putNonExistingArquivoImportacao() throws Exception {
        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();
        arquivoImportacao.setId(count.incrementAndGet());

        // Create the ArquivoImportacao
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoImportacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arquivoImportacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArquivoImportacao() throws Exception {
        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();
        arquivoImportacao.setId(count.incrementAndGet());

        // Create the ArquivoImportacao
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoImportacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArquivoImportacao() throws Exception {
        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();
        arquivoImportacao.setId(count.incrementAndGet());

        // Create the ArquivoImportacao
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoImportacaoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArquivoImportacaoWithPatch() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();

        // Update the arquivoImportacao using partial update
        ArquivoImportacao partialUpdatedArquivoImportacao = new ArquivoImportacao();
        partialUpdatedArquivoImportacao.setId(arquivoImportacao.getId());

        partialUpdatedArquivoImportacao.urlArquivo(UPDATED_URL_ARQUIVO).estado(UPDATED_ESTADO).processado(UPDATED_PROCESSADO);

        restArquivoImportacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivoImportacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArquivoImportacao))
            )
            .andExpect(status().isOk());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
        ArquivoImportacao testArquivoImportacao = arquivoImportacaoList.get(arquivoImportacaoList.size() - 1);
        assertThat(testArquivoImportacao.getUrlArquivo()).isEqualTo(UPDATED_URL_ARQUIVO);
        assertThat(testArquivoImportacao.getUrlCriticas()).isEqualTo(DEFAULT_URL_CRITICAS);
        assertThat(testArquivoImportacao.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testArquivoImportacao.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testArquivoImportacao.getProcessado()).isEqualTo(UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void fullUpdateArquivoImportacaoWithPatch() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();

        // Update the arquivoImportacao using partial update
        ArquivoImportacao partialUpdatedArquivoImportacao = new ArquivoImportacao();
        partialUpdatedArquivoImportacao.setId(arquivoImportacao.getId());

        partialUpdatedArquivoImportacao
            .urlArquivo(UPDATED_URL_ARQUIVO)
            .urlCriticas(UPDATED_URL_CRITICAS)
            .criado(UPDATED_CRIADO)
            .estado(UPDATED_ESTADO)
            .processado(UPDATED_PROCESSADO);

        restArquivoImportacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivoImportacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArquivoImportacao))
            )
            .andExpect(status().isOk());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
        ArquivoImportacao testArquivoImportacao = arquivoImportacaoList.get(arquivoImportacaoList.size() - 1);
        assertThat(testArquivoImportacao.getUrlArquivo()).isEqualTo(UPDATED_URL_ARQUIVO);
        assertThat(testArquivoImportacao.getUrlCriticas()).isEqualTo(UPDATED_URL_CRITICAS);
        assertThat(testArquivoImportacao.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testArquivoImportacao.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testArquivoImportacao.getProcessado()).isEqualTo(UPDATED_PROCESSADO);
    }

    @Test
    @Transactional
    void patchNonExistingArquivoImportacao() throws Exception {
        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();
        arquivoImportacao.setId(count.incrementAndGet());

        // Create the ArquivoImportacao
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoImportacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arquivoImportacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArquivoImportacao() throws Exception {
        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();
        arquivoImportacao.setId(count.incrementAndGet());

        // Create the ArquivoImportacao
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoImportacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArquivoImportacao() throws Exception {
        int databaseSizeBeforeUpdate = arquivoImportacaoRepository.findAll().size();
        arquivoImportacao.setId(count.incrementAndGet());

        // Create the ArquivoImportacao
        ArquivoImportacaoDTO arquivoImportacaoDTO = arquivoImportacaoMapper.toDto(arquivoImportacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoImportacaoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arquivoImportacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArquivoImportacao in the database
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArquivoImportacao() throws Exception {
        // Initialize the database
        arquivoImportacaoRepository.saveAndFlush(arquivoImportacao);

        int databaseSizeBeforeDelete = arquivoImportacaoRepository.findAll().size();

        // Delete the arquivoImportacao
        restArquivoImportacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, arquivoImportacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArquivoImportacao> arquivoImportacaoList = arquivoImportacaoRepository.findAll();
        assertThat(arquivoImportacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
