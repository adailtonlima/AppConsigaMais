package br.com.consiga.web.rest;

import static br.com.consiga.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.consiga.IntegrationTest;
import br.com.consiga.domain.FaturaMensal;
import br.com.consiga.domain.MensalidadePedido;
import br.com.consiga.domain.Pedido;
import br.com.consiga.repository.MensalidadePedidoRepository;
import br.com.consiga.service.criteria.MensalidadePedidoCriteria;
import br.com.consiga.service.dto.MensalidadePedidoDTO;
import br.com.consiga.service.mapper.MensalidadePedidoMapper;
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
 * Integration tests for the {@link MensalidadePedidoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MensalidadePedidoResourceIT {

    private static final Integer DEFAULT_N_PARCELA = 1;
    private static final Integer UPDATED_N_PARCELA = 2;
    private static final Integer SMALLER_N_PARCELA = 1 - 1;

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;
    private static final Double SMALLER_VALOR = 1D - 1D;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Double DEFAULT_VALOR_PARCIAL = 1D;
    private static final Double UPDATED_VALOR_PARCIAL = 2D;
    private static final Double SMALLER_VALOR_PARCIAL = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/mensalidade-pedidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MensalidadePedidoRepository mensalidadePedidoRepository;

    @Autowired
    private MensalidadePedidoMapper mensalidadePedidoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMensalidadePedidoMockMvc;

    private MensalidadePedido mensalidadePedido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MensalidadePedido createEntity(EntityManager em) {
        MensalidadePedido mensalidadePedido = new MensalidadePedido()
            .nParcela(DEFAULT_N_PARCELA)
            .valor(DEFAULT_VALOR)
            .criado(DEFAULT_CRIADO)
            .valorParcial(DEFAULT_VALOR_PARCIAL);
        return mensalidadePedido;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MensalidadePedido createUpdatedEntity(EntityManager em) {
        MensalidadePedido mensalidadePedido = new MensalidadePedido()
            .nParcela(UPDATED_N_PARCELA)
            .valor(UPDATED_VALOR)
            .criado(UPDATED_CRIADO)
            .valorParcial(UPDATED_VALOR_PARCIAL);
        return mensalidadePedido;
    }

    @BeforeEach
    public void initTest() {
        mensalidadePedido = createEntity(em);
    }

    @Test
    @Transactional
    void createMensalidadePedido() throws Exception {
        int databaseSizeBeforeCreate = mensalidadePedidoRepository.findAll().size();
        // Create the MensalidadePedido
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);
        restMensalidadePedidoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeCreate + 1);
        MensalidadePedido testMensalidadePedido = mensalidadePedidoList.get(mensalidadePedidoList.size() - 1);
        assertThat(testMensalidadePedido.getnParcela()).isEqualTo(DEFAULT_N_PARCELA);
        assertThat(testMensalidadePedido.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testMensalidadePedido.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testMensalidadePedido.getValorParcial()).isEqualTo(DEFAULT_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void createMensalidadePedidoWithExistingId() throws Exception {
        // Create the MensalidadePedido with an existing ID
        mensalidadePedido.setId(1L);
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);

        int databaseSizeBeforeCreate = mensalidadePedidoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMensalidadePedidoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidos() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList
        restMensalidadePedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensalidadePedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].nParcela").value(hasItem(DEFAULT_N_PARCELA)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].valorParcial").value(hasItem(DEFAULT_VALOR_PARCIAL.doubleValue())));
    }

    @Test
    @Transactional
    void getMensalidadePedido() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get the mensalidadePedido
        restMensalidadePedidoMockMvc
            .perform(get(ENTITY_API_URL_ID, mensalidadePedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mensalidadePedido.getId().intValue()))
            .andExpect(jsonPath("$.nParcela").value(DEFAULT_N_PARCELA))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.valorParcial").value(DEFAULT_VALOR_PARCIAL.doubleValue()));
    }

    @Test
    @Transactional
    void getMensalidadePedidosByIdFiltering() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        Long id = mensalidadePedido.getId();

        defaultMensalidadePedidoShouldBeFound("id.equals=" + id);
        defaultMensalidadePedidoShouldNotBeFound("id.notEquals=" + id);

        defaultMensalidadePedidoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMensalidadePedidoShouldNotBeFound("id.greaterThan=" + id);

        defaultMensalidadePedidoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMensalidadePedidoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela equals to DEFAULT_N_PARCELA
        defaultMensalidadePedidoShouldBeFound("nParcela.equals=" + DEFAULT_N_PARCELA);

        // Get all the mensalidadePedidoList where nParcela equals to UPDATED_N_PARCELA
        defaultMensalidadePedidoShouldNotBeFound("nParcela.equals=" + UPDATED_N_PARCELA);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela not equals to DEFAULT_N_PARCELA
        defaultMensalidadePedidoShouldNotBeFound("nParcela.notEquals=" + DEFAULT_N_PARCELA);

        // Get all the mensalidadePedidoList where nParcela not equals to UPDATED_N_PARCELA
        defaultMensalidadePedidoShouldBeFound("nParcela.notEquals=" + UPDATED_N_PARCELA);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsInShouldWork() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela in DEFAULT_N_PARCELA or UPDATED_N_PARCELA
        defaultMensalidadePedidoShouldBeFound("nParcela.in=" + DEFAULT_N_PARCELA + "," + UPDATED_N_PARCELA);

        // Get all the mensalidadePedidoList where nParcela equals to UPDATED_N_PARCELA
        defaultMensalidadePedidoShouldNotBeFound("nParcela.in=" + UPDATED_N_PARCELA);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela is not null
        defaultMensalidadePedidoShouldBeFound("nParcela.specified=true");

        // Get all the mensalidadePedidoList where nParcela is null
        defaultMensalidadePedidoShouldNotBeFound("nParcela.specified=false");
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela is greater than or equal to DEFAULT_N_PARCELA
        defaultMensalidadePedidoShouldBeFound("nParcela.greaterThanOrEqual=" + DEFAULT_N_PARCELA);

        // Get all the mensalidadePedidoList where nParcela is greater than or equal to UPDATED_N_PARCELA
        defaultMensalidadePedidoShouldNotBeFound("nParcela.greaterThanOrEqual=" + UPDATED_N_PARCELA);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela is less than or equal to DEFAULT_N_PARCELA
        defaultMensalidadePedidoShouldBeFound("nParcela.lessThanOrEqual=" + DEFAULT_N_PARCELA);

        // Get all the mensalidadePedidoList where nParcela is less than or equal to SMALLER_N_PARCELA
        defaultMensalidadePedidoShouldNotBeFound("nParcela.lessThanOrEqual=" + SMALLER_N_PARCELA);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsLessThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela is less than DEFAULT_N_PARCELA
        defaultMensalidadePedidoShouldNotBeFound("nParcela.lessThan=" + DEFAULT_N_PARCELA);

        // Get all the mensalidadePedidoList where nParcela is less than UPDATED_N_PARCELA
        defaultMensalidadePedidoShouldBeFound("nParcela.lessThan=" + UPDATED_N_PARCELA);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosBynParcelaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where nParcela is greater than DEFAULT_N_PARCELA
        defaultMensalidadePedidoShouldNotBeFound("nParcela.greaterThan=" + DEFAULT_N_PARCELA);

        // Get all the mensalidadePedidoList where nParcela is greater than SMALLER_N_PARCELA
        defaultMensalidadePedidoShouldBeFound("nParcela.greaterThan=" + SMALLER_N_PARCELA);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor equals to DEFAULT_VALOR
        defaultMensalidadePedidoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the mensalidadePedidoList where valor equals to UPDATED_VALOR
        defaultMensalidadePedidoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor not equals to DEFAULT_VALOR
        defaultMensalidadePedidoShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the mensalidadePedidoList where valor not equals to UPDATED_VALOR
        defaultMensalidadePedidoShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultMensalidadePedidoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the mensalidadePedidoList where valor equals to UPDATED_VALOR
        defaultMensalidadePedidoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor is not null
        defaultMensalidadePedidoShouldBeFound("valor.specified=true");

        // Get all the mensalidadePedidoList where valor is null
        defaultMensalidadePedidoShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor is greater than or equal to DEFAULT_VALOR
        defaultMensalidadePedidoShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the mensalidadePedidoList where valor is greater than or equal to UPDATED_VALOR
        defaultMensalidadePedidoShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor is less than or equal to DEFAULT_VALOR
        defaultMensalidadePedidoShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the mensalidadePedidoList where valor is less than or equal to SMALLER_VALOR
        defaultMensalidadePedidoShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor is less than DEFAULT_VALOR
        defaultMensalidadePedidoShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the mensalidadePedidoList where valor is less than UPDATED_VALOR
        defaultMensalidadePedidoShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valor is greater than DEFAULT_VALOR
        defaultMensalidadePedidoShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the mensalidadePedidoList where valor is greater than SMALLER_VALOR
        defaultMensalidadePedidoShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado equals to DEFAULT_CRIADO
        defaultMensalidadePedidoShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the mensalidadePedidoList where criado equals to UPDATED_CRIADO
        defaultMensalidadePedidoShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado not equals to DEFAULT_CRIADO
        defaultMensalidadePedidoShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the mensalidadePedidoList where criado not equals to UPDATED_CRIADO
        defaultMensalidadePedidoShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultMensalidadePedidoShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the mensalidadePedidoList where criado equals to UPDATED_CRIADO
        defaultMensalidadePedidoShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado is not null
        defaultMensalidadePedidoShouldBeFound("criado.specified=true");

        // Get all the mensalidadePedidoList where criado is null
        defaultMensalidadePedidoShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado is greater than or equal to DEFAULT_CRIADO
        defaultMensalidadePedidoShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the mensalidadePedidoList where criado is greater than or equal to UPDATED_CRIADO
        defaultMensalidadePedidoShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado is less than or equal to DEFAULT_CRIADO
        defaultMensalidadePedidoShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the mensalidadePedidoList where criado is less than or equal to SMALLER_CRIADO
        defaultMensalidadePedidoShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado is less than DEFAULT_CRIADO
        defaultMensalidadePedidoShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the mensalidadePedidoList where criado is less than UPDATED_CRIADO
        defaultMensalidadePedidoShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where criado is greater than DEFAULT_CRIADO
        defaultMensalidadePedidoShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the mensalidadePedidoList where criado is greater than SMALLER_CRIADO
        defaultMensalidadePedidoShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial equals to DEFAULT_VALOR_PARCIAL
        defaultMensalidadePedidoShouldBeFound("valorParcial.equals=" + DEFAULT_VALOR_PARCIAL);

        // Get all the mensalidadePedidoList where valorParcial equals to UPDATED_VALOR_PARCIAL
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.equals=" + UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial not equals to DEFAULT_VALOR_PARCIAL
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.notEquals=" + DEFAULT_VALOR_PARCIAL);

        // Get all the mensalidadePedidoList where valorParcial not equals to UPDATED_VALOR_PARCIAL
        defaultMensalidadePedidoShouldBeFound("valorParcial.notEquals=" + UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsInShouldWork() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial in DEFAULT_VALOR_PARCIAL or UPDATED_VALOR_PARCIAL
        defaultMensalidadePedidoShouldBeFound("valorParcial.in=" + DEFAULT_VALOR_PARCIAL + "," + UPDATED_VALOR_PARCIAL);

        // Get all the mensalidadePedidoList where valorParcial equals to UPDATED_VALOR_PARCIAL
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.in=" + UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial is not null
        defaultMensalidadePedidoShouldBeFound("valorParcial.specified=true");

        // Get all the mensalidadePedidoList where valorParcial is null
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.specified=false");
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial is greater than or equal to DEFAULT_VALOR_PARCIAL
        defaultMensalidadePedidoShouldBeFound("valorParcial.greaterThanOrEqual=" + DEFAULT_VALOR_PARCIAL);

        // Get all the mensalidadePedidoList where valorParcial is greater than or equal to UPDATED_VALOR_PARCIAL
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.greaterThanOrEqual=" + UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial is less than or equal to DEFAULT_VALOR_PARCIAL
        defaultMensalidadePedidoShouldBeFound("valorParcial.lessThanOrEqual=" + DEFAULT_VALOR_PARCIAL);

        // Get all the mensalidadePedidoList where valorParcial is less than or equal to SMALLER_VALOR_PARCIAL
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.lessThanOrEqual=" + SMALLER_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsLessThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial is less than DEFAULT_VALOR_PARCIAL
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.lessThan=" + DEFAULT_VALOR_PARCIAL);

        // Get all the mensalidadePedidoList where valorParcial is less than UPDATED_VALOR_PARCIAL
        defaultMensalidadePedidoShouldBeFound("valorParcial.lessThan=" + UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByValorParcialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        // Get all the mensalidadePedidoList where valorParcial is greater than DEFAULT_VALOR_PARCIAL
        defaultMensalidadePedidoShouldNotBeFound("valorParcial.greaterThan=" + DEFAULT_VALOR_PARCIAL);

        // Get all the mensalidadePedidoList where valorParcial is greater than SMALLER_VALOR_PARCIAL
        defaultMensalidadePedidoShouldBeFound("valorParcial.greaterThan=" + SMALLER_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByPedidoIsEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);
        Pedido pedido = PedidoResourceIT.createEntity(em);
        em.persist(pedido);
        em.flush();
        mensalidadePedido.setPedido(pedido);
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);
        Long pedidoId = pedido.getId();

        // Get all the mensalidadePedidoList where pedido equals to pedidoId
        defaultMensalidadePedidoShouldBeFound("pedidoId.equals=" + pedidoId);

        // Get all the mensalidadePedidoList where pedido equals to (pedidoId + 1)
        defaultMensalidadePedidoShouldNotBeFound("pedidoId.equals=" + (pedidoId + 1));
    }

    @Test
    @Transactional
    void getAllMensalidadePedidosByFaturaIsEqualToSomething() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);
        FaturaMensal fatura = FaturaMensalResourceIT.createEntity(em);
        em.persist(fatura);
        em.flush();
        mensalidadePedido.setFatura(fatura);
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);
        Long faturaId = fatura.getId();

        // Get all the mensalidadePedidoList where fatura equals to faturaId
        defaultMensalidadePedidoShouldBeFound("faturaId.equals=" + faturaId);

        // Get all the mensalidadePedidoList where fatura equals to (faturaId + 1)
        defaultMensalidadePedidoShouldNotBeFound("faturaId.equals=" + (faturaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMensalidadePedidoShouldBeFound(String filter) throws Exception {
        restMensalidadePedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensalidadePedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].nParcela").value(hasItem(DEFAULT_N_PARCELA)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].valorParcial").value(hasItem(DEFAULT_VALOR_PARCIAL.doubleValue())));

        // Check, that the count call also returns 1
        restMensalidadePedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMensalidadePedidoShouldNotBeFound(String filter) throws Exception {
        restMensalidadePedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMensalidadePedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMensalidadePedido() throws Exception {
        // Get the mensalidadePedido
        restMensalidadePedidoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMensalidadePedido() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();

        // Update the mensalidadePedido
        MensalidadePedido updatedMensalidadePedido = mensalidadePedidoRepository.findById(mensalidadePedido.getId()).get();
        // Disconnect from session so that the updates on updatedMensalidadePedido are not directly saved in db
        em.detach(updatedMensalidadePedido);
        updatedMensalidadePedido
            .nParcela(UPDATED_N_PARCELA)
            .valor(UPDATED_VALOR)
            .criado(UPDATED_CRIADO)
            .valorParcial(UPDATED_VALOR_PARCIAL);
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(updatedMensalidadePedido);

        restMensalidadePedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mensalidadePedidoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isOk());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
        MensalidadePedido testMensalidadePedido = mensalidadePedidoList.get(mensalidadePedidoList.size() - 1);
        assertThat(testMensalidadePedido.getnParcela()).isEqualTo(UPDATED_N_PARCELA);
        assertThat(testMensalidadePedido.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testMensalidadePedido.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testMensalidadePedido.getValorParcial()).isEqualTo(UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void putNonExistingMensalidadePedido() throws Exception {
        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();
        mensalidadePedido.setId(count.incrementAndGet());

        // Create the MensalidadePedido
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensalidadePedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mensalidadePedidoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMensalidadePedido() throws Exception {
        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();
        mensalidadePedido.setId(count.incrementAndGet());

        // Create the MensalidadePedido
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensalidadePedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMensalidadePedido() throws Exception {
        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();
        mensalidadePedido.setId(count.incrementAndGet());

        // Create the MensalidadePedido
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensalidadePedidoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMensalidadePedidoWithPatch() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();

        // Update the mensalidadePedido using partial update
        MensalidadePedido partialUpdatedMensalidadePedido = new MensalidadePedido();
        partialUpdatedMensalidadePedido.setId(mensalidadePedido.getId());

        partialUpdatedMensalidadePedido
            .nParcela(UPDATED_N_PARCELA)
            .valor(UPDATED_VALOR)
            .criado(UPDATED_CRIADO)
            .valorParcial(UPDATED_VALOR_PARCIAL);

        restMensalidadePedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensalidadePedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensalidadePedido))
            )
            .andExpect(status().isOk());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
        MensalidadePedido testMensalidadePedido = mensalidadePedidoList.get(mensalidadePedidoList.size() - 1);
        assertThat(testMensalidadePedido.getnParcela()).isEqualTo(UPDATED_N_PARCELA);
        assertThat(testMensalidadePedido.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testMensalidadePedido.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testMensalidadePedido.getValorParcial()).isEqualTo(UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void fullUpdateMensalidadePedidoWithPatch() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();

        // Update the mensalidadePedido using partial update
        MensalidadePedido partialUpdatedMensalidadePedido = new MensalidadePedido();
        partialUpdatedMensalidadePedido.setId(mensalidadePedido.getId());

        partialUpdatedMensalidadePedido
            .nParcela(UPDATED_N_PARCELA)
            .valor(UPDATED_VALOR)
            .criado(UPDATED_CRIADO)
            .valorParcial(UPDATED_VALOR_PARCIAL);

        restMensalidadePedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensalidadePedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensalidadePedido))
            )
            .andExpect(status().isOk());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
        MensalidadePedido testMensalidadePedido = mensalidadePedidoList.get(mensalidadePedidoList.size() - 1);
        assertThat(testMensalidadePedido.getnParcela()).isEqualTo(UPDATED_N_PARCELA);
        assertThat(testMensalidadePedido.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testMensalidadePedido.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testMensalidadePedido.getValorParcial()).isEqualTo(UPDATED_VALOR_PARCIAL);
    }

    @Test
    @Transactional
    void patchNonExistingMensalidadePedido() throws Exception {
        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();
        mensalidadePedido.setId(count.incrementAndGet());

        // Create the MensalidadePedido
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensalidadePedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mensalidadePedidoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMensalidadePedido() throws Exception {
        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();
        mensalidadePedido.setId(count.incrementAndGet());

        // Create the MensalidadePedido
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensalidadePedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMensalidadePedido() throws Exception {
        int databaseSizeBeforeUpdate = mensalidadePedidoRepository.findAll().size();
        mensalidadePedido.setId(count.incrementAndGet());

        // Create the MensalidadePedido
        MensalidadePedidoDTO mensalidadePedidoDTO = mensalidadePedidoMapper.toDto(mensalidadePedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensalidadePedidoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensalidadePedidoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MensalidadePedido in the database
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMensalidadePedido() throws Exception {
        // Initialize the database
        mensalidadePedidoRepository.saveAndFlush(mensalidadePedido);

        int databaseSizeBeforeDelete = mensalidadePedidoRepository.findAll().size();

        // Delete the mensalidadePedido
        restMensalidadePedidoMockMvc
            .perform(delete(ENTITY_API_URL_ID, mensalidadePedido.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MensalidadePedido> mensalidadePedidoList = mensalidadePedidoRepository.findAll();
        assertThat(mensalidadePedidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
