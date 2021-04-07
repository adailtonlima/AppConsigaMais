package br.com.consiga.web.rest;

import static br.com.consiga.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.consiga.IntegrationTest;
import br.com.consiga.domain.Empresa;
import br.com.consiga.domain.FaturaMensal;
import br.com.consiga.domain.Filial;
import br.com.consiga.repository.FaturaMensalRepository;
import br.com.consiga.service.criteria.FaturaMensalCriteria;
import br.com.consiga.service.dto.FaturaMensalDTO;
import br.com.consiga.service.mapper.FaturaMensalMapper;
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
 * Integration tests for the {@link FaturaMensalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FaturaMensalResourceIT {

    private static final LocalDate DEFAULT_MES = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MES = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MES = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_BOLETO_URL = "AAAAAAAAAA";
    private static final String UPDATED_BOLETO_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_PAGO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_PAGO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_PAGO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/fatura-mensals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FaturaMensalRepository faturaMensalRepository;

    @Autowired
    private FaturaMensalMapper faturaMensalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFaturaMensalMockMvc;

    private FaturaMensal faturaMensal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FaturaMensal createEntity(EntityManager em) {
        FaturaMensal faturaMensal = new FaturaMensal()
            .mes(DEFAULT_MES)
            .criado(DEFAULT_CRIADO)
            .boletoUrl(DEFAULT_BOLETO_URL)
            .dataPago(DEFAULT_DATA_PAGO);
        return faturaMensal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FaturaMensal createUpdatedEntity(EntityManager em) {
        FaturaMensal faturaMensal = new FaturaMensal()
            .mes(UPDATED_MES)
            .criado(UPDATED_CRIADO)
            .boletoUrl(UPDATED_BOLETO_URL)
            .dataPago(UPDATED_DATA_PAGO);
        return faturaMensal;
    }

    @BeforeEach
    public void initTest() {
        faturaMensal = createEntity(em);
    }

    @Test
    @Transactional
    void createFaturaMensal() throws Exception {
        int databaseSizeBeforeCreate = faturaMensalRepository.findAll().size();
        // Create the FaturaMensal
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);
        restFaturaMensalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeCreate + 1);
        FaturaMensal testFaturaMensal = faturaMensalList.get(faturaMensalList.size() - 1);
        assertThat(testFaturaMensal.getMes()).isEqualTo(DEFAULT_MES);
        assertThat(testFaturaMensal.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testFaturaMensal.getBoletoUrl()).isEqualTo(DEFAULT_BOLETO_URL);
        assertThat(testFaturaMensal.getDataPago()).isEqualTo(DEFAULT_DATA_PAGO);
    }

    @Test
    @Transactional
    void createFaturaMensalWithExistingId() throws Exception {
        // Create the FaturaMensal with an existing ID
        faturaMensal.setId(1L);
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);

        int databaseSizeBeforeCreate = faturaMensalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFaturaMensalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFaturaMensals() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList
        restFaturaMensalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faturaMensal.getId().intValue())))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].boletoUrl").value(hasItem(DEFAULT_BOLETO_URL)))
            .andExpect(jsonPath("$.[*].dataPago").value(hasItem(sameInstant(DEFAULT_DATA_PAGO))));
    }

    @Test
    @Transactional
    void getFaturaMensal() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get the faturaMensal
        restFaturaMensalMockMvc
            .perform(get(ENTITY_API_URL_ID, faturaMensal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(faturaMensal.getId().intValue()))
            .andExpect(jsonPath("$.mes").value(DEFAULT_MES.toString()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.boletoUrl").value(DEFAULT_BOLETO_URL))
            .andExpect(jsonPath("$.dataPago").value(sameInstant(DEFAULT_DATA_PAGO)));
    }

    @Test
    @Transactional
    void getFaturaMensalsByIdFiltering() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        Long id = faturaMensal.getId();

        defaultFaturaMensalShouldBeFound("id.equals=" + id);
        defaultFaturaMensalShouldNotBeFound("id.notEquals=" + id);

        defaultFaturaMensalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFaturaMensalShouldNotBeFound("id.greaterThan=" + id);

        defaultFaturaMensalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFaturaMensalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes equals to DEFAULT_MES
        defaultFaturaMensalShouldBeFound("mes.equals=" + DEFAULT_MES);

        // Get all the faturaMensalList where mes equals to UPDATED_MES
        defaultFaturaMensalShouldNotBeFound("mes.equals=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes not equals to DEFAULT_MES
        defaultFaturaMensalShouldNotBeFound("mes.notEquals=" + DEFAULT_MES);

        // Get all the faturaMensalList where mes not equals to UPDATED_MES
        defaultFaturaMensalShouldBeFound("mes.notEquals=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsInShouldWork() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes in DEFAULT_MES or UPDATED_MES
        defaultFaturaMensalShouldBeFound("mes.in=" + DEFAULT_MES + "," + UPDATED_MES);

        // Get all the faturaMensalList where mes equals to UPDATED_MES
        defaultFaturaMensalShouldNotBeFound("mes.in=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes is not null
        defaultFaturaMensalShouldBeFound("mes.specified=true");

        // Get all the faturaMensalList where mes is null
        defaultFaturaMensalShouldNotBeFound("mes.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes is greater than or equal to DEFAULT_MES
        defaultFaturaMensalShouldBeFound("mes.greaterThanOrEqual=" + DEFAULT_MES);

        // Get all the faturaMensalList where mes is greater than or equal to UPDATED_MES
        defaultFaturaMensalShouldNotBeFound("mes.greaterThanOrEqual=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes is less than or equal to DEFAULT_MES
        defaultFaturaMensalShouldBeFound("mes.lessThanOrEqual=" + DEFAULT_MES);

        // Get all the faturaMensalList where mes is less than or equal to SMALLER_MES
        defaultFaturaMensalShouldNotBeFound("mes.lessThanOrEqual=" + SMALLER_MES);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes is less than DEFAULT_MES
        defaultFaturaMensalShouldNotBeFound("mes.lessThan=" + DEFAULT_MES);

        // Get all the faturaMensalList where mes is less than UPDATED_MES
        defaultFaturaMensalShouldBeFound("mes.lessThan=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByMesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where mes is greater than DEFAULT_MES
        defaultFaturaMensalShouldNotBeFound("mes.greaterThan=" + DEFAULT_MES);

        // Get all the faturaMensalList where mes is greater than SMALLER_MES
        defaultFaturaMensalShouldBeFound("mes.greaterThan=" + SMALLER_MES);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado equals to DEFAULT_CRIADO
        defaultFaturaMensalShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the faturaMensalList where criado equals to UPDATED_CRIADO
        defaultFaturaMensalShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado not equals to DEFAULT_CRIADO
        defaultFaturaMensalShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the faturaMensalList where criado not equals to UPDATED_CRIADO
        defaultFaturaMensalShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultFaturaMensalShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the faturaMensalList where criado equals to UPDATED_CRIADO
        defaultFaturaMensalShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado is not null
        defaultFaturaMensalShouldBeFound("criado.specified=true");

        // Get all the faturaMensalList where criado is null
        defaultFaturaMensalShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado is greater than or equal to DEFAULT_CRIADO
        defaultFaturaMensalShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the faturaMensalList where criado is greater than or equal to UPDATED_CRIADO
        defaultFaturaMensalShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado is less than or equal to DEFAULT_CRIADO
        defaultFaturaMensalShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the faturaMensalList where criado is less than or equal to SMALLER_CRIADO
        defaultFaturaMensalShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado is less than DEFAULT_CRIADO
        defaultFaturaMensalShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the faturaMensalList where criado is less than UPDATED_CRIADO
        defaultFaturaMensalShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where criado is greater than DEFAULT_CRIADO
        defaultFaturaMensalShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the faturaMensalList where criado is greater than SMALLER_CRIADO
        defaultFaturaMensalShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByBoletoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where boletoUrl equals to DEFAULT_BOLETO_URL
        defaultFaturaMensalShouldBeFound("boletoUrl.equals=" + DEFAULT_BOLETO_URL);

        // Get all the faturaMensalList where boletoUrl equals to UPDATED_BOLETO_URL
        defaultFaturaMensalShouldNotBeFound("boletoUrl.equals=" + UPDATED_BOLETO_URL);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByBoletoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where boletoUrl not equals to DEFAULT_BOLETO_URL
        defaultFaturaMensalShouldNotBeFound("boletoUrl.notEquals=" + DEFAULT_BOLETO_URL);

        // Get all the faturaMensalList where boletoUrl not equals to UPDATED_BOLETO_URL
        defaultFaturaMensalShouldBeFound("boletoUrl.notEquals=" + UPDATED_BOLETO_URL);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByBoletoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where boletoUrl in DEFAULT_BOLETO_URL or UPDATED_BOLETO_URL
        defaultFaturaMensalShouldBeFound("boletoUrl.in=" + DEFAULT_BOLETO_URL + "," + UPDATED_BOLETO_URL);

        // Get all the faturaMensalList where boletoUrl equals to UPDATED_BOLETO_URL
        defaultFaturaMensalShouldNotBeFound("boletoUrl.in=" + UPDATED_BOLETO_URL);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByBoletoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where boletoUrl is not null
        defaultFaturaMensalShouldBeFound("boletoUrl.specified=true");

        // Get all the faturaMensalList where boletoUrl is null
        defaultFaturaMensalShouldNotBeFound("boletoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByBoletoUrlContainsSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where boletoUrl contains DEFAULT_BOLETO_URL
        defaultFaturaMensalShouldBeFound("boletoUrl.contains=" + DEFAULT_BOLETO_URL);

        // Get all the faturaMensalList where boletoUrl contains UPDATED_BOLETO_URL
        defaultFaturaMensalShouldNotBeFound("boletoUrl.contains=" + UPDATED_BOLETO_URL);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByBoletoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where boletoUrl does not contain DEFAULT_BOLETO_URL
        defaultFaturaMensalShouldNotBeFound("boletoUrl.doesNotContain=" + DEFAULT_BOLETO_URL);

        // Get all the faturaMensalList where boletoUrl does not contain UPDATED_BOLETO_URL
        defaultFaturaMensalShouldBeFound("boletoUrl.doesNotContain=" + UPDATED_BOLETO_URL);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago equals to DEFAULT_DATA_PAGO
        defaultFaturaMensalShouldBeFound("dataPago.equals=" + DEFAULT_DATA_PAGO);

        // Get all the faturaMensalList where dataPago equals to UPDATED_DATA_PAGO
        defaultFaturaMensalShouldNotBeFound("dataPago.equals=" + UPDATED_DATA_PAGO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago not equals to DEFAULT_DATA_PAGO
        defaultFaturaMensalShouldNotBeFound("dataPago.notEquals=" + DEFAULT_DATA_PAGO);

        // Get all the faturaMensalList where dataPago not equals to UPDATED_DATA_PAGO
        defaultFaturaMensalShouldBeFound("dataPago.notEquals=" + UPDATED_DATA_PAGO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsInShouldWork() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago in DEFAULT_DATA_PAGO or UPDATED_DATA_PAGO
        defaultFaturaMensalShouldBeFound("dataPago.in=" + DEFAULT_DATA_PAGO + "," + UPDATED_DATA_PAGO);

        // Get all the faturaMensalList where dataPago equals to UPDATED_DATA_PAGO
        defaultFaturaMensalShouldNotBeFound("dataPago.in=" + UPDATED_DATA_PAGO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago is not null
        defaultFaturaMensalShouldBeFound("dataPago.specified=true");

        // Get all the faturaMensalList where dataPago is null
        defaultFaturaMensalShouldNotBeFound("dataPago.specified=false");
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago is greater than or equal to DEFAULT_DATA_PAGO
        defaultFaturaMensalShouldBeFound("dataPago.greaterThanOrEqual=" + DEFAULT_DATA_PAGO);

        // Get all the faturaMensalList where dataPago is greater than or equal to UPDATED_DATA_PAGO
        defaultFaturaMensalShouldNotBeFound("dataPago.greaterThanOrEqual=" + UPDATED_DATA_PAGO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago is less than or equal to DEFAULT_DATA_PAGO
        defaultFaturaMensalShouldBeFound("dataPago.lessThanOrEqual=" + DEFAULT_DATA_PAGO);

        // Get all the faturaMensalList where dataPago is less than or equal to SMALLER_DATA_PAGO
        defaultFaturaMensalShouldNotBeFound("dataPago.lessThanOrEqual=" + SMALLER_DATA_PAGO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago is less than DEFAULT_DATA_PAGO
        defaultFaturaMensalShouldNotBeFound("dataPago.lessThan=" + DEFAULT_DATA_PAGO);

        // Get all the faturaMensalList where dataPago is less than UPDATED_DATA_PAGO
        defaultFaturaMensalShouldBeFound("dataPago.lessThan=" + UPDATED_DATA_PAGO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByDataPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        // Get all the faturaMensalList where dataPago is greater than DEFAULT_DATA_PAGO
        defaultFaturaMensalShouldNotBeFound("dataPago.greaterThan=" + DEFAULT_DATA_PAGO);

        // Get all the faturaMensalList where dataPago is greater than SMALLER_DATA_PAGO
        defaultFaturaMensalShouldBeFound("dataPago.greaterThan=" + SMALLER_DATA_PAGO);
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);
        Empresa empresa = EmpresaResourceIT.createEntity(em);
        em.persist(empresa);
        em.flush();
        faturaMensal.setEmpresa(empresa);
        faturaMensalRepository.saveAndFlush(faturaMensal);
        Long empresaId = empresa.getId();

        // Get all the faturaMensalList where empresa equals to empresaId
        defaultFaturaMensalShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the faturaMensalList where empresa equals to (empresaId + 1)
        defaultFaturaMensalShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllFaturaMensalsByFilialIsEqualToSomething() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);
        Filial filial = FilialResourceIT.createEntity(em);
        em.persist(filial);
        em.flush();
        faturaMensal.setFilial(filial);
        faturaMensalRepository.saveAndFlush(faturaMensal);
        Long filialId = filial.getId();

        // Get all the faturaMensalList where filial equals to filialId
        defaultFaturaMensalShouldBeFound("filialId.equals=" + filialId);

        // Get all the faturaMensalList where filial equals to (filialId + 1)
        defaultFaturaMensalShouldNotBeFound("filialId.equals=" + (filialId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFaturaMensalShouldBeFound(String filter) throws Exception {
        restFaturaMensalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faturaMensal.getId().intValue())))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].boletoUrl").value(hasItem(DEFAULT_BOLETO_URL)))
            .andExpect(jsonPath("$.[*].dataPago").value(hasItem(sameInstant(DEFAULT_DATA_PAGO))));

        // Check, that the count call also returns 1
        restFaturaMensalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFaturaMensalShouldNotBeFound(String filter) throws Exception {
        restFaturaMensalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFaturaMensalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFaturaMensal() throws Exception {
        // Get the faturaMensal
        restFaturaMensalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFaturaMensal() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();

        // Update the faturaMensal
        FaturaMensal updatedFaturaMensal = faturaMensalRepository.findById(faturaMensal.getId()).get();
        // Disconnect from session so that the updates on updatedFaturaMensal are not directly saved in db
        em.detach(updatedFaturaMensal);
        updatedFaturaMensal.mes(UPDATED_MES).criado(UPDATED_CRIADO).boletoUrl(UPDATED_BOLETO_URL).dataPago(UPDATED_DATA_PAGO);
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(updatedFaturaMensal);

        restFaturaMensalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, faturaMensalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isOk());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
        FaturaMensal testFaturaMensal = faturaMensalList.get(faturaMensalList.size() - 1);
        assertThat(testFaturaMensal.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testFaturaMensal.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testFaturaMensal.getBoletoUrl()).isEqualTo(UPDATED_BOLETO_URL);
        assertThat(testFaturaMensal.getDataPago()).isEqualTo(UPDATED_DATA_PAGO);
    }

    @Test
    @Transactional
    void putNonExistingFaturaMensal() throws Exception {
        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();
        faturaMensal.setId(count.incrementAndGet());

        // Create the FaturaMensal
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFaturaMensalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, faturaMensalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFaturaMensal() throws Exception {
        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();
        faturaMensal.setId(count.incrementAndGet());

        // Create the FaturaMensal
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMensalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFaturaMensal() throws Exception {
        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();
        faturaMensal.setId(count.incrementAndGet());

        // Create the FaturaMensal
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMensalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFaturaMensalWithPatch() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();

        // Update the faturaMensal using partial update
        FaturaMensal partialUpdatedFaturaMensal = new FaturaMensal();
        partialUpdatedFaturaMensal.setId(faturaMensal.getId());

        partialUpdatedFaturaMensal.boletoUrl(UPDATED_BOLETO_URL);

        restFaturaMensalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFaturaMensal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFaturaMensal))
            )
            .andExpect(status().isOk());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
        FaturaMensal testFaturaMensal = faturaMensalList.get(faturaMensalList.size() - 1);
        assertThat(testFaturaMensal.getMes()).isEqualTo(DEFAULT_MES);
        assertThat(testFaturaMensal.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testFaturaMensal.getBoletoUrl()).isEqualTo(UPDATED_BOLETO_URL);
        assertThat(testFaturaMensal.getDataPago()).isEqualTo(DEFAULT_DATA_PAGO);
    }

    @Test
    @Transactional
    void fullUpdateFaturaMensalWithPatch() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();

        // Update the faturaMensal using partial update
        FaturaMensal partialUpdatedFaturaMensal = new FaturaMensal();
        partialUpdatedFaturaMensal.setId(faturaMensal.getId());

        partialUpdatedFaturaMensal.mes(UPDATED_MES).criado(UPDATED_CRIADO).boletoUrl(UPDATED_BOLETO_URL).dataPago(UPDATED_DATA_PAGO);

        restFaturaMensalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFaturaMensal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFaturaMensal))
            )
            .andExpect(status().isOk());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
        FaturaMensal testFaturaMensal = faturaMensalList.get(faturaMensalList.size() - 1);
        assertThat(testFaturaMensal.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testFaturaMensal.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testFaturaMensal.getBoletoUrl()).isEqualTo(UPDATED_BOLETO_URL);
        assertThat(testFaturaMensal.getDataPago()).isEqualTo(UPDATED_DATA_PAGO);
    }

    @Test
    @Transactional
    void patchNonExistingFaturaMensal() throws Exception {
        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();
        faturaMensal.setId(count.incrementAndGet());

        // Create the FaturaMensal
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFaturaMensalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, faturaMensalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFaturaMensal() throws Exception {
        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();
        faturaMensal.setId(count.incrementAndGet());

        // Create the FaturaMensal
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMensalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFaturaMensal() throws Exception {
        int databaseSizeBeforeUpdate = faturaMensalRepository.findAll().size();
        faturaMensal.setId(count.incrementAndGet());

        // Create the FaturaMensal
        FaturaMensalDTO faturaMensalDTO = faturaMensalMapper.toDto(faturaMensal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFaturaMensalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(faturaMensalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FaturaMensal in the database
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFaturaMensal() throws Exception {
        // Initialize the database
        faturaMensalRepository.saveAndFlush(faturaMensal);

        int databaseSizeBeforeDelete = faturaMensalRepository.findAll().size();

        // Delete the faturaMensal
        restFaturaMensalMockMvc
            .perform(delete(ENTITY_API_URL_ID, faturaMensal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FaturaMensal> faturaMensalList = faturaMensalRepository.findAll();
        assertThat(faturaMensalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
