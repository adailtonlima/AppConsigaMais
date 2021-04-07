package br.com.consiga.web.rest;

import static br.com.consiga.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.consiga.IntegrationTest;
import br.com.consiga.domain.Administrador;
import br.com.consiga.domain.Empresa;
import br.com.consiga.domain.Filial;
import br.com.consiga.domain.Funcionario;
import br.com.consiga.domain.Pedido;
import br.com.consiga.domain.enumeration.StatusPedido;
import br.com.consiga.repository.PedidoRepository;
import br.com.consiga.service.criteria.PedidoCriteria;
import br.com.consiga.service.dto.PedidoDTO;
import br.com.consiga.service.mapper.PedidoMapper;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link PedidoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PedidoResourceIT {

    private static final StatusPedido DEFAULT_ESTADO = StatusPedido.PENDENTE;
    private static final StatusPedido UPDATED_ESTADO = StatusPedido.APROVADO;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_APROVACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_APROVACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_APROVACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_DATA_EXPIRACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_EXPIRACAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_EXPIRACAO = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_RENDA = 1D;
    private static final Double UPDATED_RENDA = 2D;
    private static final Double SMALLER_RENDA = 1D - 1D;

    private static final Double DEFAULT_VALOR_SOLICITADO = 1D;
    private static final Double UPDATED_VALOR_SOLICITADO = 2D;
    private static final Double SMALLER_VALOR_SOLICITADO = 1D - 1D;

    private static final Integer DEFAULT_QT_PARCELAS_SOLICITADO = 1;
    private static final Integer UPDATED_QT_PARCELAS_SOLICITADO = 2;
    private static final Integer SMALLER_QT_PARCELAS_SOLICITADO = 1 - 1;

    private static final Double DEFAULT_VALOR_APROVADO = 1D;
    private static final Double UPDATED_VALOR_APROVADO = 2D;
    private static final Double SMALLER_VALOR_APROVADO = 1D - 1D;

    private static final Double DEFAULT_VALOR_PARCELA_APROVADO = 1D;
    private static final Double UPDATED_VALOR_PARCELA_APROVADO = 2D;
    private static final Double SMALLER_VALOR_PARCELA_APROVADO = 1D - 1D;

    private static final Integer DEFAULT_QT_PARCELAS_APROVADO = 1;
    private static final Integer UPDATED_QT_PARCELAS_APROVADO = 2;
    private static final Integer SMALLER_QT_PARCELAS_APROVADO = 1 - 1;

    private static final LocalDate DEFAULT_DATA_PRIMEIRO_VENCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PRIMEIRO_VENCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_PRIMEIRO_VENCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATA_ULTIMO_VENCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ULTIMO_VENCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_ULTIMO_VENCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/pedidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .estado(DEFAULT_ESTADO)
            .criado(DEFAULT_CRIADO)
            .dataAprovacao(DEFAULT_DATA_APROVACAO)
            .dataExpiracao(DEFAULT_DATA_EXPIRACAO)
            .renda(DEFAULT_RENDA)
            .valorSolicitado(DEFAULT_VALOR_SOLICITADO)
            .qtParcelasSolicitado(DEFAULT_QT_PARCELAS_SOLICITADO)
            .valorAprovado(DEFAULT_VALOR_APROVADO)
            .valorParcelaAprovado(DEFAULT_VALOR_PARCELA_APROVADO)
            .qtParcelasAprovado(DEFAULT_QT_PARCELAS_APROVADO)
            .dataPrimeiroVencimento(DEFAULT_DATA_PRIMEIRO_VENCIMENTO)
            .dataUltimoVencimento(DEFAULT_DATA_ULTIMO_VENCIMENTO);
        return pedido;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createUpdatedEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .estado(UPDATED_ESTADO)
            .criado(UPDATED_CRIADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .dataExpiracao(UPDATED_DATA_EXPIRACAO)
            .renda(UPDATED_RENDA)
            .valorSolicitado(UPDATED_VALOR_SOLICITADO)
            .qtParcelasSolicitado(UPDATED_QT_PARCELAS_SOLICITADO)
            .valorAprovado(UPDATED_VALOR_APROVADO)
            .valorParcelaAprovado(UPDATED_VALOR_PARCELA_APROVADO)
            .qtParcelasAprovado(UPDATED_QT_PARCELAS_APROVADO)
            .dataPrimeiroVencimento(UPDATED_DATA_PRIMEIRO_VENCIMENTO)
            .dataUltimoVencimento(UPDATED_DATA_ULTIMO_VENCIMENTO);
        return pedido;
    }

    @BeforeEach
    public void initTest() {
        pedido = createEntity(em);
    }

    @Test
    @Transactional
    void createPedido() throws Exception {
        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();
        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);
        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedidoDTO)))
            .andExpect(status().isCreated());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeCreate + 1);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPedido.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testPedido.getDataAprovacao()).isEqualTo(DEFAULT_DATA_APROVACAO);
        assertThat(testPedido.getDataExpiracao()).isEqualTo(DEFAULT_DATA_EXPIRACAO);
        assertThat(testPedido.getRenda()).isEqualTo(DEFAULT_RENDA);
        assertThat(testPedido.getValorSolicitado()).isEqualTo(DEFAULT_VALOR_SOLICITADO);
        assertThat(testPedido.getQtParcelasSolicitado()).isEqualTo(DEFAULT_QT_PARCELAS_SOLICITADO);
        assertThat(testPedido.getValorAprovado()).isEqualTo(DEFAULT_VALOR_APROVADO);
        assertThat(testPedido.getValorParcelaAprovado()).isEqualTo(DEFAULT_VALOR_PARCELA_APROVADO);
        assertThat(testPedido.getQtParcelasAprovado()).isEqualTo(DEFAULT_QT_PARCELAS_APROVADO);
        assertThat(testPedido.getDataPrimeiroVencimento()).isEqualTo(DEFAULT_DATA_PRIMEIRO_VENCIMENTO);
        assertThat(testPedido.getDataUltimoVencimento()).isEqualTo(DEFAULT_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void createPedidoWithExistingId() throws Exception {
        // Create the Pedido with an existing ID
        pedido.setId(1L);
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPedidos() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].dataAprovacao").value(hasItem(sameInstant(DEFAULT_DATA_APROVACAO))))
            .andExpect(jsonPath("$.[*].dataExpiracao").value(hasItem(DEFAULT_DATA_EXPIRACAO.toString())))
            .andExpect(jsonPath("$.[*].renda").value(hasItem(DEFAULT_RENDA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorSolicitado").value(hasItem(DEFAULT_VALOR_SOLICITADO.doubleValue())))
            .andExpect(jsonPath("$.[*].qtParcelasSolicitado").value(hasItem(DEFAULT_QT_PARCELAS_SOLICITADO)))
            .andExpect(jsonPath("$.[*].valorAprovado").value(hasItem(DEFAULT_VALOR_APROVADO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorParcelaAprovado").value(hasItem(DEFAULT_VALOR_PARCELA_APROVADO.doubleValue())))
            .andExpect(jsonPath("$.[*].qtParcelasAprovado").value(hasItem(DEFAULT_QT_PARCELAS_APROVADO)))
            .andExpect(jsonPath("$.[*].dataPrimeiroVencimento").value(hasItem(DEFAULT_DATA_PRIMEIRO_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].dataUltimoVencimento").value(hasItem(DEFAULT_DATA_ULTIMO_VENCIMENTO.toString())));
    }

    @Test
    @Transactional
    void getPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL_ID, pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.dataAprovacao").value(sameInstant(DEFAULT_DATA_APROVACAO)))
            .andExpect(jsonPath("$.dataExpiracao").value(DEFAULT_DATA_EXPIRACAO.toString()))
            .andExpect(jsonPath("$.renda").value(DEFAULT_RENDA.doubleValue()))
            .andExpect(jsonPath("$.valorSolicitado").value(DEFAULT_VALOR_SOLICITADO.doubleValue()))
            .andExpect(jsonPath("$.qtParcelasSolicitado").value(DEFAULT_QT_PARCELAS_SOLICITADO))
            .andExpect(jsonPath("$.valorAprovado").value(DEFAULT_VALOR_APROVADO.doubleValue()))
            .andExpect(jsonPath("$.valorParcelaAprovado").value(DEFAULT_VALOR_PARCELA_APROVADO.doubleValue()))
            .andExpect(jsonPath("$.qtParcelasAprovado").value(DEFAULT_QT_PARCELAS_APROVADO))
            .andExpect(jsonPath("$.dataPrimeiroVencimento").value(DEFAULT_DATA_PRIMEIRO_VENCIMENTO.toString()))
            .andExpect(jsonPath("$.dataUltimoVencimento").value(DEFAULT_DATA_ULTIMO_VENCIMENTO.toString()));
    }

    @Test
    @Transactional
    void getPedidosByIdFiltering() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        Long id = pedido.getId();

        defaultPedidoShouldBeFound("id.equals=" + id);
        defaultPedidoShouldNotBeFound("id.notEquals=" + id);

        defaultPedidoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPedidoShouldNotBeFound("id.greaterThan=" + id);

        defaultPedidoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPedidoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado equals to DEFAULT_ESTADO
        defaultPedidoShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the pedidoList where estado equals to UPDATED_ESTADO
        defaultPedidoShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado not equals to DEFAULT_ESTADO
        defaultPedidoShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the pedidoList where estado not equals to UPDATED_ESTADO
        defaultPedidoShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultPedidoShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the pedidoList where estado equals to UPDATED_ESTADO
        defaultPedidoShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado is not null
        defaultPedidoShouldBeFound("estado.specified=true");

        // Get all the pedidoList where estado is null
        defaultPedidoShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado equals to DEFAULT_CRIADO
        defaultPedidoShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the pedidoList where criado equals to UPDATED_CRIADO
        defaultPedidoShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado not equals to DEFAULT_CRIADO
        defaultPedidoShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the pedidoList where criado not equals to UPDATED_CRIADO
        defaultPedidoShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultPedidoShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the pedidoList where criado equals to UPDATED_CRIADO
        defaultPedidoShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado is not null
        defaultPedidoShouldBeFound("criado.specified=true");

        // Get all the pedidoList where criado is null
        defaultPedidoShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado is greater than or equal to DEFAULT_CRIADO
        defaultPedidoShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the pedidoList where criado is greater than or equal to UPDATED_CRIADO
        defaultPedidoShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado is less than or equal to DEFAULT_CRIADO
        defaultPedidoShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the pedidoList where criado is less than or equal to SMALLER_CRIADO
        defaultPedidoShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado is less than DEFAULT_CRIADO
        defaultPedidoShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the pedidoList where criado is less than UPDATED_CRIADO
        defaultPedidoShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPedidosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where criado is greater than DEFAULT_CRIADO
        defaultPedidoShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the pedidoList where criado is greater than SMALLER_CRIADO
        defaultPedidoShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao equals to DEFAULT_DATA_APROVACAO
        defaultPedidoShouldBeFound("dataAprovacao.equals=" + DEFAULT_DATA_APROVACAO);

        // Get all the pedidoList where dataAprovacao equals to UPDATED_DATA_APROVACAO
        defaultPedidoShouldNotBeFound("dataAprovacao.equals=" + UPDATED_DATA_APROVACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao not equals to DEFAULT_DATA_APROVACAO
        defaultPedidoShouldNotBeFound("dataAprovacao.notEquals=" + DEFAULT_DATA_APROVACAO);

        // Get all the pedidoList where dataAprovacao not equals to UPDATED_DATA_APROVACAO
        defaultPedidoShouldBeFound("dataAprovacao.notEquals=" + UPDATED_DATA_APROVACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao in DEFAULT_DATA_APROVACAO or UPDATED_DATA_APROVACAO
        defaultPedidoShouldBeFound("dataAprovacao.in=" + DEFAULT_DATA_APROVACAO + "," + UPDATED_DATA_APROVACAO);

        // Get all the pedidoList where dataAprovacao equals to UPDATED_DATA_APROVACAO
        defaultPedidoShouldNotBeFound("dataAprovacao.in=" + UPDATED_DATA_APROVACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao is not null
        defaultPedidoShouldBeFound("dataAprovacao.specified=true");

        // Get all the pedidoList where dataAprovacao is null
        defaultPedidoShouldNotBeFound("dataAprovacao.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao is greater than or equal to DEFAULT_DATA_APROVACAO
        defaultPedidoShouldBeFound("dataAprovacao.greaterThanOrEqual=" + DEFAULT_DATA_APROVACAO);

        // Get all the pedidoList where dataAprovacao is greater than or equal to UPDATED_DATA_APROVACAO
        defaultPedidoShouldNotBeFound("dataAprovacao.greaterThanOrEqual=" + UPDATED_DATA_APROVACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao is less than or equal to DEFAULT_DATA_APROVACAO
        defaultPedidoShouldBeFound("dataAprovacao.lessThanOrEqual=" + DEFAULT_DATA_APROVACAO);

        // Get all the pedidoList where dataAprovacao is less than or equal to SMALLER_DATA_APROVACAO
        defaultPedidoShouldNotBeFound("dataAprovacao.lessThanOrEqual=" + SMALLER_DATA_APROVACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao is less than DEFAULT_DATA_APROVACAO
        defaultPedidoShouldNotBeFound("dataAprovacao.lessThan=" + DEFAULT_DATA_APROVACAO);

        // Get all the pedidoList where dataAprovacao is less than UPDATED_DATA_APROVACAO
        defaultPedidoShouldBeFound("dataAprovacao.lessThan=" + UPDATED_DATA_APROVACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataAprovacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataAprovacao is greater than DEFAULT_DATA_APROVACAO
        defaultPedidoShouldNotBeFound("dataAprovacao.greaterThan=" + DEFAULT_DATA_APROVACAO);

        // Get all the pedidoList where dataAprovacao is greater than SMALLER_DATA_APROVACAO
        defaultPedidoShouldBeFound("dataAprovacao.greaterThan=" + SMALLER_DATA_APROVACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao equals to DEFAULT_DATA_EXPIRACAO
        defaultPedidoShouldBeFound("dataExpiracao.equals=" + DEFAULT_DATA_EXPIRACAO);

        // Get all the pedidoList where dataExpiracao equals to UPDATED_DATA_EXPIRACAO
        defaultPedidoShouldNotBeFound("dataExpiracao.equals=" + UPDATED_DATA_EXPIRACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao not equals to DEFAULT_DATA_EXPIRACAO
        defaultPedidoShouldNotBeFound("dataExpiracao.notEquals=" + DEFAULT_DATA_EXPIRACAO);

        // Get all the pedidoList where dataExpiracao not equals to UPDATED_DATA_EXPIRACAO
        defaultPedidoShouldBeFound("dataExpiracao.notEquals=" + UPDATED_DATA_EXPIRACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao in DEFAULT_DATA_EXPIRACAO or UPDATED_DATA_EXPIRACAO
        defaultPedidoShouldBeFound("dataExpiracao.in=" + DEFAULT_DATA_EXPIRACAO + "," + UPDATED_DATA_EXPIRACAO);

        // Get all the pedidoList where dataExpiracao equals to UPDATED_DATA_EXPIRACAO
        defaultPedidoShouldNotBeFound("dataExpiracao.in=" + UPDATED_DATA_EXPIRACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao is not null
        defaultPedidoShouldBeFound("dataExpiracao.specified=true");

        // Get all the pedidoList where dataExpiracao is null
        defaultPedidoShouldNotBeFound("dataExpiracao.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao is greater than or equal to DEFAULT_DATA_EXPIRACAO
        defaultPedidoShouldBeFound("dataExpiracao.greaterThanOrEqual=" + DEFAULT_DATA_EXPIRACAO);

        // Get all the pedidoList where dataExpiracao is greater than or equal to UPDATED_DATA_EXPIRACAO
        defaultPedidoShouldNotBeFound("dataExpiracao.greaterThanOrEqual=" + UPDATED_DATA_EXPIRACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao is less than or equal to DEFAULT_DATA_EXPIRACAO
        defaultPedidoShouldBeFound("dataExpiracao.lessThanOrEqual=" + DEFAULT_DATA_EXPIRACAO);

        // Get all the pedidoList where dataExpiracao is less than or equal to SMALLER_DATA_EXPIRACAO
        defaultPedidoShouldNotBeFound("dataExpiracao.lessThanOrEqual=" + SMALLER_DATA_EXPIRACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao is less than DEFAULT_DATA_EXPIRACAO
        defaultPedidoShouldNotBeFound("dataExpiracao.lessThan=" + DEFAULT_DATA_EXPIRACAO);

        // Get all the pedidoList where dataExpiracao is less than UPDATED_DATA_EXPIRACAO
        defaultPedidoShouldBeFound("dataExpiracao.lessThan=" + UPDATED_DATA_EXPIRACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataExpiracaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataExpiracao is greater than DEFAULT_DATA_EXPIRACAO
        defaultPedidoShouldNotBeFound("dataExpiracao.greaterThan=" + DEFAULT_DATA_EXPIRACAO);

        // Get all the pedidoList where dataExpiracao is greater than SMALLER_DATA_EXPIRACAO
        defaultPedidoShouldBeFound("dataExpiracao.greaterThan=" + SMALLER_DATA_EXPIRACAO);
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda equals to DEFAULT_RENDA
        defaultPedidoShouldBeFound("renda.equals=" + DEFAULT_RENDA);

        // Get all the pedidoList where renda equals to UPDATED_RENDA
        defaultPedidoShouldNotBeFound("renda.equals=" + UPDATED_RENDA);
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda not equals to DEFAULT_RENDA
        defaultPedidoShouldNotBeFound("renda.notEquals=" + DEFAULT_RENDA);

        // Get all the pedidoList where renda not equals to UPDATED_RENDA
        defaultPedidoShouldBeFound("renda.notEquals=" + UPDATED_RENDA);
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda in DEFAULT_RENDA or UPDATED_RENDA
        defaultPedidoShouldBeFound("renda.in=" + DEFAULT_RENDA + "," + UPDATED_RENDA);

        // Get all the pedidoList where renda equals to UPDATED_RENDA
        defaultPedidoShouldNotBeFound("renda.in=" + UPDATED_RENDA);
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda is not null
        defaultPedidoShouldBeFound("renda.specified=true");

        // Get all the pedidoList where renda is null
        defaultPedidoShouldNotBeFound("renda.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda is greater than or equal to DEFAULT_RENDA
        defaultPedidoShouldBeFound("renda.greaterThanOrEqual=" + DEFAULT_RENDA);

        // Get all the pedidoList where renda is greater than or equal to UPDATED_RENDA
        defaultPedidoShouldNotBeFound("renda.greaterThanOrEqual=" + UPDATED_RENDA);
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda is less than or equal to DEFAULT_RENDA
        defaultPedidoShouldBeFound("renda.lessThanOrEqual=" + DEFAULT_RENDA);

        // Get all the pedidoList where renda is less than or equal to SMALLER_RENDA
        defaultPedidoShouldNotBeFound("renda.lessThanOrEqual=" + SMALLER_RENDA);
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda is less than DEFAULT_RENDA
        defaultPedidoShouldNotBeFound("renda.lessThan=" + DEFAULT_RENDA);

        // Get all the pedidoList where renda is less than UPDATED_RENDA
        defaultPedidoShouldBeFound("renda.lessThan=" + UPDATED_RENDA);
    }

    @Test
    @Transactional
    void getAllPedidosByRendaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where renda is greater than DEFAULT_RENDA
        defaultPedidoShouldNotBeFound("renda.greaterThan=" + DEFAULT_RENDA);

        // Get all the pedidoList where renda is greater than SMALLER_RENDA
        defaultPedidoShouldBeFound("renda.greaterThan=" + SMALLER_RENDA);
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado equals to DEFAULT_VALOR_SOLICITADO
        defaultPedidoShouldBeFound("valorSolicitado.equals=" + DEFAULT_VALOR_SOLICITADO);

        // Get all the pedidoList where valorSolicitado equals to UPDATED_VALOR_SOLICITADO
        defaultPedidoShouldNotBeFound("valorSolicitado.equals=" + UPDATED_VALOR_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado not equals to DEFAULT_VALOR_SOLICITADO
        defaultPedidoShouldNotBeFound("valorSolicitado.notEquals=" + DEFAULT_VALOR_SOLICITADO);

        // Get all the pedidoList where valorSolicitado not equals to UPDATED_VALOR_SOLICITADO
        defaultPedidoShouldBeFound("valorSolicitado.notEquals=" + UPDATED_VALOR_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado in DEFAULT_VALOR_SOLICITADO or UPDATED_VALOR_SOLICITADO
        defaultPedidoShouldBeFound("valorSolicitado.in=" + DEFAULT_VALOR_SOLICITADO + "," + UPDATED_VALOR_SOLICITADO);

        // Get all the pedidoList where valorSolicitado equals to UPDATED_VALOR_SOLICITADO
        defaultPedidoShouldNotBeFound("valorSolicitado.in=" + UPDATED_VALOR_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado is not null
        defaultPedidoShouldBeFound("valorSolicitado.specified=true");

        // Get all the pedidoList where valorSolicitado is null
        defaultPedidoShouldNotBeFound("valorSolicitado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado is greater than or equal to DEFAULT_VALOR_SOLICITADO
        defaultPedidoShouldBeFound("valorSolicitado.greaterThanOrEqual=" + DEFAULT_VALOR_SOLICITADO);

        // Get all the pedidoList where valorSolicitado is greater than or equal to UPDATED_VALOR_SOLICITADO
        defaultPedidoShouldNotBeFound("valorSolicitado.greaterThanOrEqual=" + UPDATED_VALOR_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado is less than or equal to DEFAULT_VALOR_SOLICITADO
        defaultPedidoShouldBeFound("valorSolicitado.lessThanOrEqual=" + DEFAULT_VALOR_SOLICITADO);

        // Get all the pedidoList where valorSolicitado is less than or equal to SMALLER_VALOR_SOLICITADO
        defaultPedidoShouldNotBeFound("valorSolicitado.lessThanOrEqual=" + SMALLER_VALOR_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado is less than DEFAULT_VALOR_SOLICITADO
        defaultPedidoShouldNotBeFound("valorSolicitado.lessThan=" + DEFAULT_VALOR_SOLICITADO);

        // Get all the pedidoList where valorSolicitado is less than UPDATED_VALOR_SOLICITADO
        defaultPedidoShouldBeFound("valorSolicitado.lessThan=" + UPDATED_VALOR_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorSolicitadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorSolicitado is greater than DEFAULT_VALOR_SOLICITADO
        defaultPedidoShouldNotBeFound("valorSolicitado.greaterThan=" + DEFAULT_VALOR_SOLICITADO);

        // Get all the pedidoList where valorSolicitado is greater than SMALLER_VALOR_SOLICITADO
        defaultPedidoShouldBeFound("valorSolicitado.greaterThan=" + SMALLER_VALOR_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado equals to DEFAULT_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldBeFound("qtParcelasSolicitado.equals=" + DEFAULT_QT_PARCELAS_SOLICITADO);

        // Get all the pedidoList where qtParcelasSolicitado equals to UPDATED_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.equals=" + UPDATED_QT_PARCELAS_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado not equals to DEFAULT_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.notEquals=" + DEFAULT_QT_PARCELAS_SOLICITADO);

        // Get all the pedidoList where qtParcelasSolicitado not equals to UPDATED_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldBeFound("qtParcelasSolicitado.notEquals=" + UPDATED_QT_PARCELAS_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado in DEFAULT_QT_PARCELAS_SOLICITADO or UPDATED_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldBeFound("qtParcelasSolicitado.in=" + DEFAULT_QT_PARCELAS_SOLICITADO + "," + UPDATED_QT_PARCELAS_SOLICITADO);

        // Get all the pedidoList where qtParcelasSolicitado equals to UPDATED_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.in=" + UPDATED_QT_PARCELAS_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado is not null
        defaultPedidoShouldBeFound("qtParcelasSolicitado.specified=true");

        // Get all the pedidoList where qtParcelasSolicitado is null
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado is greater than or equal to DEFAULT_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldBeFound("qtParcelasSolicitado.greaterThanOrEqual=" + DEFAULT_QT_PARCELAS_SOLICITADO);

        // Get all the pedidoList where qtParcelasSolicitado is greater than or equal to UPDATED_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.greaterThanOrEqual=" + UPDATED_QT_PARCELAS_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado is less than or equal to DEFAULT_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldBeFound("qtParcelasSolicitado.lessThanOrEqual=" + DEFAULT_QT_PARCELAS_SOLICITADO);

        // Get all the pedidoList where qtParcelasSolicitado is less than or equal to SMALLER_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.lessThanOrEqual=" + SMALLER_QT_PARCELAS_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado is less than DEFAULT_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.lessThan=" + DEFAULT_QT_PARCELAS_SOLICITADO);

        // Get all the pedidoList where qtParcelasSolicitado is less than UPDATED_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldBeFound("qtParcelasSolicitado.lessThan=" + UPDATED_QT_PARCELAS_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasSolicitadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasSolicitado is greater than DEFAULT_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldNotBeFound("qtParcelasSolicitado.greaterThan=" + DEFAULT_QT_PARCELAS_SOLICITADO);

        // Get all the pedidoList where qtParcelasSolicitado is greater than SMALLER_QT_PARCELAS_SOLICITADO
        defaultPedidoShouldBeFound("qtParcelasSolicitado.greaterThan=" + SMALLER_QT_PARCELAS_SOLICITADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado equals to DEFAULT_VALOR_APROVADO
        defaultPedidoShouldBeFound("valorAprovado.equals=" + DEFAULT_VALOR_APROVADO);

        // Get all the pedidoList where valorAprovado equals to UPDATED_VALOR_APROVADO
        defaultPedidoShouldNotBeFound("valorAprovado.equals=" + UPDATED_VALOR_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado not equals to DEFAULT_VALOR_APROVADO
        defaultPedidoShouldNotBeFound("valorAprovado.notEquals=" + DEFAULT_VALOR_APROVADO);

        // Get all the pedidoList where valorAprovado not equals to UPDATED_VALOR_APROVADO
        defaultPedidoShouldBeFound("valorAprovado.notEquals=" + UPDATED_VALOR_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado in DEFAULT_VALOR_APROVADO or UPDATED_VALOR_APROVADO
        defaultPedidoShouldBeFound("valorAprovado.in=" + DEFAULT_VALOR_APROVADO + "," + UPDATED_VALOR_APROVADO);

        // Get all the pedidoList where valorAprovado equals to UPDATED_VALOR_APROVADO
        defaultPedidoShouldNotBeFound("valorAprovado.in=" + UPDATED_VALOR_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado is not null
        defaultPedidoShouldBeFound("valorAprovado.specified=true");

        // Get all the pedidoList where valorAprovado is null
        defaultPedidoShouldNotBeFound("valorAprovado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado is greater than or equal to DEFAULT_VALOR_APROVADO
        defaultPedidoShouldBeFound("valorAprovado.greaterThanOrEqual=" + DEFAULT_VALOR_APROVADO);

        // Get all the pedidoList where valorAprovado is greater than or equal to UPDATED_VALOR_APROVADO
        defaultPedidoShouldNotBeFound("valorAprovado.greaterThanOrEqual=" + UPDATED_VALOR_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado is less than or equal to DEFAULT_VALOR_APROVADO
        defaultPedidoShouldBeFound("valorAprovado.lessThanOrEqual=" + DEFAULT_VALOR_APROVADO);

        // Get all the pedidoList where valorAprovado is less than or equal to SMALLER_VALOR_APROVADO
        defaultPedidoShouldNotBeFound("valorAprovado.lessThanOrEqual=" + SMALLER_VALOR_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado is less than DEFAULT_VALOR_APROVADO
        defaultPedidoShouldNotBeFound("valorAprovado.lessThan=" + DEFAULT_VALOR_APROVADO);

        // Get all the pedidoList where valorAprovado is less than UPDATED_VALOR_APROVADO
        defaultPedidoShouldBeFound("valorAprovado.lessThan=" + UPDATED_VALOR_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorAprovadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorAprovado is greater than DEFAULT_VALOR_APROVADO
        defaultPedidoShouldNotBeFound("valorAprovado.greaterThan=" + DEFAULT_VALOR_APROVADO);

        // Get all the pedidoList where valorAprovado is greater than SMALLER_VALOR_APROVADO
        defaultPedidoShouldBeFound("valorAprovado.greaterThan=" + SMALLER_VALOR_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado equals to DEFAULT_VALOR_PARCELA_APROVADO
        defaultPedidoShouldBeFound("valorParcelaAprovado.equals=" + DEFAULT_VALOR_PARCELA_APROVADO);

        // Get all the pedidoList where valorParcelaAprovado equals to UPDATED_VALOR_PARCELA_APROVADO
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.equals=" + UPDATED_VALOR_PARCELA_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado not equals to DEFAULT_VALOR_PARCELA_APROVADO
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.notEquals=" + DEFAULT_VALOR_PARCELA_APROVADO);

        // Get all the pedidoList where valorParcelaAprovado not equals to UPDATED_VALOR_PARCELA_APROVADO
        defaultPedidoShouldBeFound("valorParcelaAprovado.notEquals=" + UPDATED_VALOR_PARCELA_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado in DEFAULT_VALOR_PARCELA_APROVADO or UPDATED_VALOR_PARCELA_APROVADO
        defaultPedidoShouldBeFound("valorParcelaAprovado.in=" + DEFAULT_VALOR_PARCELA_APROVADO + "," + UPDATED_VALOR_PARCELA_APROVADO);

        // Get all the pedidoList where valorParcelaAprovado equals to UPDATED_VALOR_PARCELA_APROVADO
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.in=" + UPDATED_VALOR_PARCELA_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado is not null
        defaultPedidoShouldBeFound("valorParcelaAprovado.specified=true");

        // Get all the pedidoList where valorParcelaAprovado is null
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado is greater than or equal to DEFAULT_VALOR_PARCELA_APROVADO
        defaultPedidoShouldBeFound("valorParcelaAprovado.greaterThanOrEqual=" + DEFAULT_VALOR_PARCELA_APROVADO);

        // Get all the pedidoList where valorParcelaAprovado is greater than or equal to UPDATED_VALOR_PARCELA_APROVADO
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.greaterThanOrEqual=" + UPDATED_VALOR_PARCELA_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado is less than or equal to DEFAULT_VALOR_PARCELA_APROVADO
        defaultPedidoShouldBeFound("valorParcelaAprovado.lessThanOrEqual=" + DEFAULT_VALOR_PARCELA_APROVADO);

        // Get all the pedidoList where valorParcelaAprovado is less than or equal to SMALLER_VALOR_PARCELA_APROVADO
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.lessThanOrEqual=" + SMALLER_VALOR_PARCELA_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado is less than DEFAULT_VALOR_PARCELA_APROVADO
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.lessThan=" + DEFAULT_VALOR_PARCELA_APROVADO);

        // Get all the pedidoList where valorParcelaAprovado is less than UPDATED_VALOR_PARCELA_APROVADO
        defaultPedidoShouldBeFound("valorParcelaAprovado.lessThan=" + UPDATED_VALOR_PARCELA_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByValorParcelaAprovadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where valorParcelaAprovado is greater than DEFAULT_VALOR_PARCELA_APROVADO
        defaultPedidoShouldNotBeFound("valorParcelaAprovado.greaterThan=" + DEFAULT_VALOR_PARCELA_APROVADO);

        // Get all the pedidoList where valorParcelaAprovado is greater than SMALLER_VALOR_PARCELA_APROVADO
        defaultPedidoShouldBeFound("valorParcelaAprovado.greaterThan=" + SMALLER_VALOR_PARCELA_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado equals to DEFAULT_QT_PARCELAS_APROVADO
        defaultPedidoShouldBeFound("qtParcelasAprovado.equals=" + DEFAULT_QT_PARCELAS_APROVADO);

        // Get all the pedidoList where qtParcelasAprovado equals to UPDATED_QT_PARCELAS_APROVADO
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.equals=" + UPDATED_QT_PARCELAS_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado not equals to DEFAULT_QT_PARCELAS_APROVADO
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.notEquals=" + DEFAULT_QT_PARCELAS_APROVADO);

        // Get all the pedidoList where qtParcelasAprovado not equals to UPDATED_QT_PARCELAS_APROVADO
        defaultPedidoShouldBeFound("qtParcelasAprovado.notEquals=" + UPDATED_QT_PARCELAS_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado in DEFAULT_QT_PARCELAS_APROVADO or UPDATED_QT_PARCELAS_APROVADO
        defaultPedidoShouldBeFound("qtParcelasAprovado.in=" + DEFAULT_QT_PARCELAS_APROVADO + "," + UPDATED_QT_PARCELAS_APROVADO);

        // Get all the pedidoList where qtParcelasAprovado equals to UPDATED_QT_PARCELAS_APROVADO
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.in=" + UPDATED_QT_PARCELAS_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado is not null
        defaultPedidoShouldBeFound("qtParcelasAprovado.specified=true");

        // Get all the pedidoList where qtParcelasAprovado is null
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado is greater than or equal to DEFAULT_QT_PARCELAS_APROVADO
        defaultPedidoShouldBeFound("qtParcelasAprovado.greaterThanOrEqual=" + DEFAULT_QT_PARCELAS_APROVADO);

        // Get all the pedidoList where qtParcelasAprovado is greater than or equal to UPDATED_QT_PARCELAS_APROVADO
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.greaterThanOrEqual=" + UPDATED_QT_PARCELAS_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado is less than or equal to DEFAULT_QT_PARCELAS_APROVADO
        defaultPedidoShouldBeFound("qtParcelasAprovado.lessThanOrEqual=" + DEFAULT_QT_PARCELAS_APROVADO);

        // Get all the pedidoList where qtParcelasAprovado is less than or equal to SMALLER_QT_PARCELAS_APROVADO
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.lessThanOrEqual=" + SMALLER_QT_PARCELAS_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado is less than DEFAULT_QT_PARCELAS_APROVADO
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.lessThan=" + DEFAULT_QT_PARCELAS_APROVADO);

        // Get all the pedidoList where qtParcelasAprovado is less than UPDATED_QT_PARCELAS_APROVADO
        defaultPedidoShouldBeFound("qtParcelasAprovado.lessThan=" + UPDATED_QT_PARCELAS_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByQtParcelasAprovadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where qtParcelasAprovado is greater than DEFAULT_QT_PARCELAS_APROVADO
        defaultPedidoShouldNotBeFound("qtParcelasAprovado.greaterThan=" + DEFAULT_QT_PARCELAS_APROVADO);

        // Get all the pedidoList where qtParcelasAprovado is greater than SMALLER_QT_PARCELAS_APROVADO
        defaultPedidoShouldBeFound("qtParcelasAprovado.greaterThan=" + SMALLER_QT_PARCELAS_APROVADO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento equals to DEFAULT_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldBeFound("dataPrimeiroVencimento.equals=" + DEFAULT_DATA_PRIMEIRO_VENCIMENTO);

        // Get all the pedidoList where dataPrimeiroVencimento equals to UPDATED_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.equals=" + UPDATED_DATA_PRIMEIRO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento not equals to DEFAULT_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.notEquals=" + DEFAULT_DATA_PRIMEIRO_VENCIMENTO);

        // Get all the pedidoList where dataPrimeiroVencimento not equals to UPDATED_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldBeFound("dataPrimeiroVencimento.notEquals=" + UPDATED_DATA_PRIMEIRO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento in DEFAULT_DATA_PRIMEIRO_VENCIMENTO or UPDATED_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldBeFound(
            "dataPrimeiroVencimento.in=" + DEFAULT_DATA_PRIMEIRO_VENCIMENTO + "," + UPDATED_DATA_PRIMEIRO_VENCIMENTO
        );

        // Get all the pedidoList where dataPrimeiroVencimento equals to UPDATED_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.in=" + UPDATED_DATA_PRIMEIRO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento is not null
        defaultPedidoShouldBeFound("dataPrimeiroVencimento.specified=true");

        // Get all the pedidoList where dataPrimeiroVencimento is null
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento is greater than or equal to DEFAULT_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldBeFound("dataPrimeiroVencimento.greaterThanOrEqual=" + DEFAULT_DATA_PRIMEIRO_VENCIMENTO);

        // Get all the pedidoList where dataPrimeiroVencimento is greater than or equal to UPDATED_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.greaterThanOrEqual=" + UPDATED_DATA_PRIMEIRO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento is less than or equal to DEFAULT_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldBeFound("dataPrimeiroVencimento.lessThanOrEqual=" + DEFAULT_DATA_PRIMEIRO_VENCIMENTO);

        // Get all the pedidoList where dataPrimeiroVencimento is less than or equal to SMALLER_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.lessThanOrEqual=" + SMALLER_DATA_PRIMEIRO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento is less than DEFAULT_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.lessThan=" + DEFAULT_DATA_PRIMEIRO_VENCIMENTO);

        // Get all the pedidoList where dataPrimeiroVencimento is less than UPDATED_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldBeFound("dataPrimeiroVencimento.lessThan=" + UPDATED_DATA_PRIMEIRO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataPrimeiroVencimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataPrimeiroVencimento is greater than DEFAULT_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataPrimeiroVencimento.greaterThan=" + DEFAULT_DATA_PRIMEIRO_VENCIMENTO);

        // Get all the pedidoList where dataPrimeiroVencimento is greater than SMALLER_DATA_PRIMEIRO_VENCIMENTO
        defaultPedidoShouldBeFound("dataPrimeiroVencimento.greaterThan=" + SMALLER_DATA_PRIMEIRO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento equals to DEFAULT_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldBeFound("dataUltimoVencimento.equals=" + DEFAULT_DATA_ULTIMO_VENCIMENTO);

        // Get all the pedidoList where dataUltimoVencimento equals to UPDATED_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.equals=" + UPDATED_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento not equals to DEFAULT_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.notEquals=" + DEFAULT_DATA_ULTIMO_VENCIMENTO);

        // Get all the pedidoList where dataUltimoVencimento not equals to UPDATED_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldBeFound("dataUltimoVencimento.notEquals=" + UPDATED_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento in DEFAULT_DATA_ULTIMO_VENCIMENTO or UPDATED_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldBeFound("dataUltimoVencimento.in=" + DEFAULT_DATA_ULTIMO_VENCIMENTO + "," + UPDATED_DATA_ULTIMO_VENCIMENTO);

        // Get all the pedidoList where dataUltimoVencimento equals to UPDATED_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.in=" + UPDATED_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento is not null
        defaultPedidoShouldBeFound("dataUltimoVencimento.specified=true");

        // Get all the pedidoList where dataUltimoVencimento is null
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento is greater than or equal to DEFAULT_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldBeFound("dataUltimoVencimento.greaterThanOrEqual=" + DEFAULT_DATA_ULTIMO_VENCIMENTO);

        // Get all the pedidoList where dataUltimoVencimento is greater than or equal to UPDATED_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.greaterThanOrEqual=" + UPDATED_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento is less than or equal to DEFAULT_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldBeFound("dataUltimoVencimento.lessThanOrEqual=" + DEFAULT_DATA_ULTIMO_VENCIMENTO);

        // Get all the pedidoList where dataUltimoVencimento is less than or equal to SMALLER_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.lessThanOrEqual=" + SMALLER_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento is less than DEFAULT_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.lessThan=" + DEFAULT_DATA_ULTIMO_VENCIMENTO);

        // Get all the pedidoList where dataUltimoVencimento is less than UPDATED_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldBeFound("dataUltimoVencimento.lessThan=" + UPDATED_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDataUltimoVencimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where dataUltimoVencimento is greater than DEFAULT_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldNotBeFound("dataUltimoVencimento.greaterThan=" + DEFAULT_DATA_ULTIMO_VENCIMENTO);

        // Get all the pedidoList where dataUltimoVencimento is greater than SMALLER_DATA_ULTIMO_VENCIMENTO
        defaultPedidoShouldBeFound("dataUltimoVencimento.greaterThan=" + SMALLER_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByFuncionarioIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        Funcionario funcionario = FuncionarioResourceIT.createEntity(em);
        em.persist(funcionario);
        em.flush();
        pedido.setFuncionario(funcionario);
        pedidoRepository.saveAndFlush(pedido);
        Long funcionarioId = funcionario.getId();

        // Get all the pedidoList where funcionario equals to funcionarioId
        defaultPedidoShouldBeFound("funcionarioId.equals=" + funcionarioId);

        // Get all the pedidoList where funcionario equals to (funcionarioId + 1)
        defaultPedidoShouldNotBeFound("funcionarioId.equals=" + (funcionarioId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        Empresa empresa = EmpresaResourceIT.createEntity(em);
        em.persist(empresa);
        em.flush();
        pedido.setEmpresa(empresa);
        pedidoRepository.saveAndFlush(pedido);
        Long empresaId = empresa.getId();

        // Get all the pedidoList where empresa equals to empresaId
        defaultPedidoShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the pedidoList where empresa equals to (empresaId + 1)
        defaultPedidoShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByFiliaIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        Filial filia = FilialResourceIT.createEntity(em);
        em.persist(filia);
        em.flush();
        pedido.setFilia(filia);
        pedidoRepository.saveAndFlush(pedido);
        Long filiaId = filia.getId();

        // Get all the pedidoList where filia equals to filiaId
        defaultPedidoShouldBeFound("filiaId.equals=" + filiaId);

        // Get all the pedidoList where filia equals to (filiaId + 1)
        defaultPedidoShouldNotBeFound("filiaId.equals=" + (filiaId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByQuemAprovouIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        Administrador quemAprovou = AdministradorResourceIT.createEntity(em);
        em.persist(quemAprovou);
        em.flush();
        pedido.setQuemAprovou(quemAprovou);
        pedidoRepository.saveAndFlush(pedido);
        Long quemAprovouId = quemAprovou.getId();

        // Get all the pedidoList where quemAprovou equals to quemAprovouId
        defaultPedidoShouldBeFound("quemAprovouId.equals=" + quemAprovouId);

        // Get all the pedidoList where quemAprovou equals to (quemAprovouId + 1)
        defaultPedidoShouldNotBeFound("quemAprovouId.equals=" + (quemAprovouId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPedidoShouldBeFound(String filter) throws Exception {
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].dataAprovacao").value(hasItem(sameInstant(DEFAULT_DATA_APROVACAO))))
            .andExpect(jsonPath("$.[*].dataExpiracao").value(hasItem(DEFAULT_DATA_EXPIRACAO.toString())))
            .andExpect(jsonPath("$.[*].renda").value(hasItem(DEFAULT_RENDA.doubleValue())))
            .andExpect(jsonPath("$.[*].valorSolicitado").value(hasItem(DEFAULT_VALOR_SOLICITADO.doubleValue())))
            .andExpect(jsonPath("$.[*].qtParcelasSolicitado").value(hasItem(DEFAULT_QT_PARCELAS_SOLICITADO)))
            .andExpect(jsonPath("$.[*].valorAprovado").value(hasItem(DEFAULT_VALOR_APROVADO.doubleValue())))
            .andExpect(jsonPath("$.[*].valorParcelaAprovado").value(hasItem(DEFAULT_VALOR_PARCELA_APROVADO.doubleValue())))
            .andExpect(jsonPath("$.[*].qtParcelasAprovado").value(hasItem(DEFAULT_QT_PARCELAS_APROVADO)))
            .andExpect(jsonPath("$.[*].dataPrimeiroVencimento").value(hasItem(DEFAULT_DATA_PRIMEIRO_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].dataUltimoVencimento").value(hasItem(DEFAULT_DATA_ULTIMO_VENCIMENTO.toString())));

        // Check, that the count call also returns 1
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPedidoShouldNotBeFound(String filter) throws Exception {
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido
        Pedido updatedPedido = pedidoRepository.findById(pedido.getId()).get();
        // Disconnect from session so that the updates on updatedPedido are not directly saved in db
        em.detach(updatedPedido);
        updatedPedido
            .estado(UPDATED_ESTADO)
            .criado(UPDATED_CRIADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .dataExpiracao(UPDATED_DATA_EXPIRACAO)
            .renda(UPDATED_RENDA)
            .valorSolicitado(UPDATED_VALOR_SOLICITADO)
            .qtParcelasSolicitado(UPDATED_QT_PARCELAS_SOLICITADO)
            .valorAprovado(UPDATED_VALOR_APROVADO)
            .valorParcelaAprovado(UPDATED_VALOR_PARCELA_APROVADO)
            .qtParcelasAprovado(UPDATED_QT_PARCELAS_APROVADO)
            .dataPrimeiroVencimento(UPDATED_DATA_PRIMEIRO_VENCIMENTO)
            .dataUltimoVencimento(UPDATED_DATA_ULTIMO_VENCIMENTO);
        PedidoDTO pedidoDTO = pedidoMapper.toDto(updatedPedido);

        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pedidoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPedido.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testPedido.getDataAprovacao()).isEqualTo(UPDATED_DATA_APROVACAO);
        assertThat(testPedido.getDataExpiracao()).isEqualTo(UPDATED_DATA_EXPIRACAO);
        assertThat(testPedido.getRenda()).isEqualTo(UPDATED_RENDA);
        assertThat(testPedido.getValorSolicitado()).isEqualTo(UPDATED_VALOR_SOLICITADO);
        assertThat(testPedido.getQtParcelasSolicitado()).isEqualTo(UPDATED_QT_PARCELAS_SOLICITADO);
        assertThat(testPedido.getValorAprovado()).isEqualTo(UPDATED_VALOR_APROVADO);
        assertThat(testPedido.getValorParcelaAprovado()).isEqualTo(UPDATED_VALOR_PARCELA_APROVADO);
        assertThat(testPedido.getQtParcelasAprovado()).isEqualTo(UPDATED_QT_PARCELAS_APROVADO);
        assertThat(testPedido.getDataPrimeiroVencimento()).isEqualTo(UPDATED_DATA_PRIMEIRO_VENCIMENTO);
        assertThat(testPedido.getDataUltimoVencimento()).isEqualTo(UPDATED_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void putNonExistingPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedidoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido
            .criado(UPDATED_CRIADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .qtParcelasSolicitado(UPDATED_QT_PARCELAS_SOLICITADO)
            .valorAprovado(UPDATED_VALOR_APROVADO)
            .valorParcelaAprovado(UPDATED_VALOR_PARCELA_APROVADO)
            .dataPrimeiroVencimento(UPDATED_DATA_PRIMEIRO_VENCIMENTO);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPedido.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testPedido.getDataAprovacao()).isEqualTo(UPDATED_DATA_APROVACAO);
        assertThat(testPedido.getDataExpiracao()).isEqualTo(DEFAULT_DATA_EXPIRACAO);
        assertThat(testPedido.getRenda()).isEqualTo(DEFAULT_RENDA);
        assertThat(testPedido.getValorSolicitado()).isEqualTo(DEFAULT_VALOR_SOLICITADO);
        assertThat(testPedido.getQtParcelasSolicitado()).isEqualTo(UPDATED_QT_PARCELAS_SOLICITADO);
        assertThat(testPedido.getValorAprovado()).isEqualTo(UPDATED_VALOR_APROVADO);
        assertThat(testPedido.getValorParcelaAprovado()).isEqualTo(UPDATED_VALOR_PARCELA_APROVADO);
        assertThat(testPedido.getQtParcelasAprovado()).isEqualTo(DEFAULT_QT_PARCELAS_APROVADO);
        assertThat(testPedido.getDataPrimeiroVencimento()).isEqualTo(UPDATED_DATA_PRIMEIRO_VENCIMENTO);
        assertThat(testPedido.getDataUltimoVencimento()).isEqualTo(DEFAULT_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void fullUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido
            .estado(UPDATED_ESTADO)
            .criado(UPDATED_CRIADO)
            .dataAprovacao(UPDATED_DATA_APROVACAO)
            .dataExpiracao(UPDATED_DATA_EXPIRACAO)
            .renda(UPDATED_RENDA)
            .valorSolicitado(UPDATED_VALOR_SOLICITADO)
            .qtParcelasSolicitado(UPDATED_QT_PARCELAS_SOLICITADO)
            .valorAprovado(UPDATED_VALOR_APROVADO)
            .valorParcelaAprovado(UPDATED_VALOR_PARCELA_APROVADO)
            .qtParcelasAprovado(UPDATED_QT_PARCELAS_APROVADO)
            .dataPrimeiroVencimento(UPDATED_DATA_PRIMEIRO_VENCIMENTO)
            .dataUltimoVencimento(UPDATED_DATA_ULTIMO_VENCIMENTO);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPedido.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testPedido.getDataAprovacao()).isEqualTo(UPDATED_DATA_APROVACAO);
        assertThat(testPedido.getDataExpiracao()).isEqualTo(UPDATED_DATA_EXPIRACAO);
        assertThat(testPedido.getRenda()).isEqualTo(UPDATED_RENDA);
        assertThat(testPedido.getValorSolicitado()).isEqualTo(UPDATED_VALOR_SOLICITADO);
        assertThat(testPedido.getQtParcelasSolicitado()).isEqualTo(UPDATED_QT_PARCELAS_SOLICITADO);
        assertThat(testPedido.getValorAprovado()).isEqualTo(UPDATED_VALOR_APROVADO);
        assertThat(testPedido.getValorParcelaAprovado()).isEqualTo(UPDATED_VALOR_PARCELA_APROVADO);
        assertThat(testPedido.getQtParcelasAprovado()).isEqualTo(UPDATED_QT_PARCELAS_APROVADO);
        assertThat(testPedido.getDataPrimeiroVencimento()).isEqualTo(UPDATED_DATA_PRIMEIRO_VENCIMENTO);
        assertThat(testPedido.getDataUltimoVencimento()).isEqualTo(UPDATED_DATA_ULTIMO_VENCIMENTO);
    }

    @Test
    @Transactional
    void patchNonExistingPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedidoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pedidoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeDelete = pedidoRepository.findAll().size();

        // Delete the pedido
        restPedidoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedido.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
