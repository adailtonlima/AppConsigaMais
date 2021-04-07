package br.com.consiga.web.rest;

import static br.com.consiga.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.consiga.IntegrationTest;
import br.com.consiga.domain.Funcionario;
import br.com.consiga.repository.FuncionarioRepository;
import br.com.consiga.service.criteria.FuncionarioCriteria;
import br.com.consiga.service.dto.FuncionarioDTO;
import br.com.consiga.service.mapper.FuncionarioMapper;
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
 * Integration tests for the {@link FuncionarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FuncionarioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ATUALIZADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ATUALIZADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ATUALIZADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ULTIMO_LOGIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ULTIMO_LOGIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ULTIMO_LOGIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/funcionarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioMapper funcionarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuncionarioMockMvc;

    private Funcionario funcionario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionario createEntity(EntityManager em) {
        Funcionario funcionario = new Funcionario()
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .criado(DEFAULT_CRIADO)
            .atualizado(DEFAULT_ATUALIZADO)
            .ultimoLogin(DEFAULT_ULTIMO_LOGIN);
        return funcionario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionario createUpdatedEntity(EntityManager em) {
        Funcionario funcionario = new Funcionario()
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .criado(UPDATED_CRIADO)
            .atualizado(UPDATED_ATUALIZADO)
            .ultimoLogin(UPDATED_ULTIMO_LOGIN);
        return funcionario;
    }

    @BeforeEach
    public void initTest() {
        funcionario = createEntity(em);
    }

    @Test
    @Transactional
    void createFuncionario() throws Exception {
        int databaseSizeBeforeCreate = funcionarioRepository.findAll().size();
        // Create the Funcionario
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);
        restFuncionarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeCreate + 1);
        Funcionario testFuncionario = funcionarioList.get(funcionarioList.size() - 1);
        assertThat(testFuncionario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFuncionario.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testFuncionario.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testFuncionario.getAtualizado()).isEqualTo(DEFAULT_ATUALIZADO);
        assertThat(testFuncionario.getUltimoLogin()).isEqualTo(DEFAULT_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void createFuncionarioWithExistingId() throws Exception {
        // Create the Funcionario with an existing ID
        funcionario.setId(1L);
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        int databaseSizeBeforeCreate = funcionarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuncionarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setNome(null);

        // Create the Funcionario, which fails.
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        restFuncionarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isBadRequest());

        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setCpf(null);

        // Create the Funcionario, which fails.
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        restFuncionarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isBadRequest());

        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFuncionarios() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].atualizado").value(hasItem(sameInstant(DEFAULT_ATUALIZADO))))
            .andExpect(jsonPath("$.[*].ultimoLogin").value(hasItem(sameInstant(DEFAULT_ULTIMO_LOGIN))));
    }

    @Test
    @Transactional
    void getFuncionario() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get the funcionario
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL_ID, funcionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funcionario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.atualizado").value(sameInstant(DEFAULT_ATUALIZADO)))
            .andExpect(jsonPath("$.ultimoLogin").value(sameInstant(DEFAULT_ULTIMO_LOGIN)));
    }

    @Test
    @Transactional
    void getFuncionariosByIdFiltering() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        Long id = funcionario.getId();

        defaultFuncionarioShouldBeFound("id.equals=" + id);
        defaultFuncionarioShouldNotBeFound("id.notEquals=" + id);

        defaultFuncionarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFuncionarioShouldNotBeFound("id.greaterThan=" + id);

        defaultFuncionarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFuncionarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where nome equals to DEFAULT_NOME
        defaultFuncionarioShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the funcionarioList where nome equals to UPDATED_NOME
        defaultFuncionarioShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where nome not equals to DEFAULT_NOME
        defaultFuncionarioShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the funcionarioList where nome not equals to UPDATED_NOME
        defaultFuncionarioShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultFuncionarioShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the funcionarioList where nome equals to UPDATED_NOME
        defaultFuncionarioShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where nome is not null
        defaultFuncionarioShouldBeFound("nome.specified=true");

        // Get all the funcionarioList where nome is null
        defaultFuncionarioShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByNomeContainsSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where nome contains DEFAULT_NOME
        defaultFuncionarioShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the funcionarioList where nome contains UPDATED_NOME
        defaultFuncionarioShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFuncionariosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where nome does not contain DEFAULT_NOME
        defaultFuncionarioShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the funcionarioList where nome does not contain UPDATED_NOME
        defaultFuncionarioShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where cpf equals to DEFAULT_CPF
        defaultFuncionarioShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the funcionarioList where cpf equals to UPDATED_CPF
        defaultFuncionarioShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where cpf not equals to DEFAULT_CPF
        defaultFuncionarioShouldNotBeFound("cpf.notEquals=" + DEFAULT_CPF);

        // Get all the funcionarioList where cpf not equals to UPDATED_CPF
        defaultFuncionarioShouldBeFound("cpf.notEquals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultFuncionarioShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the funcionarioList where cpf equals to UPDATED_CPF
        defaultFuncionarioShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where cpf is not null
        defaultFuncionarioShouldBeFound("cpf.specified=true");

        // Get all the funcionarioList where cpf is null
        defaultFuncionarioShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByCpfContainsSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where cpf contains DEFAULT_CPF
        defaultFuncionarioShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the funcionarioList where cpf contains UPDATED_CPF
        defaultFuncionarioShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where cpf does not contain DEFAULT_CPF
        defaultFuncionarioShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the funcionarioList where cpf does not contain UPDATED_CPF
        defaultFuncionarioShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado equals to DEFAULT_CRIADO
        defaultFuncionarioShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the funcionarioList where criado equals to UPDATED_CRIADO
        defaultFuncionarioShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado not equals to DEFAULT_CRIADO
        defaultFuncionarioShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the funcionarioList where criado not equals to UPDATED_CRIADO
        defaultFuncionarioShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultFuncionarioShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the funcionarioList where criado equals to UPDATED_CRIADO
        defaultFuncionarioShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado is not null
        defaultFuncionarioShouldBeFound("criado.specified=true");

        // Get all the funcionarioList where criado is null
        defaultFuncionarioShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado is greater than or equal to DEFAULT_CRIADO
        defaultFuncionarioShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the funcionarioList where criado is greater than or equal to UPDATED_CRIADO
        defaultFuncionarioShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado is less than or equal to DEFAULT_CRIADO
        defaultFuncionarioShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the funcionarioList where criado is less than or equal to SMALLER_CRIADO
        defaultFuncionarioShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado is less than DEFAULT_CRIADO
        defaultFuncionarioShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the funcionarioList where criado is less than UPDATED_CRIADO
        defaultFuncionarioShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where criado is greater than DEFAULT_CRIADO
        defaultFuncionarioShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the funcionarioList where criado is greater than SMALLER_CRIADO
        defaultFuncionarioShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado equals to DEFAULT_ATUALIZADO
        defaultFuncionarioShouldBeFound("atualizado.equals=" + DEFAULT_ATUALIZADO);

        // Get all the funcionarioList where atualizado equals to UPDATED_ATUALIZADO
        defaultFuncionarioShouldNotBeFound("atualizado.equals=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado not equals to DEFAULT_ATUALIZADO
        defaultFuncionarioShouldNotBeFound("atualizado.notEquals=" + DEFAULT_ATUALIZADO);

        // Get all the funcionarioList where atualizado not equals to UPDATED_ATUALIZADO
        defaultFuncionarioShouldBeFound("atualizado.notEquals=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsInShouldWork() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado in DEFAULT_ATUALIZADO or UPDATED_ATUALIZADO
        defaultFuncionarioShouldBeFound("atualizado.in=" + DEFAULT_ATUALIZADO + "," + UPDATED_ATUALIZADO);

        // Get all the funcionarioList where atualizado equals to UPDATED_ATUALIZADO
        defaultFuncionarioShouldNotBeFound("atualizado.in=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado is not null
        defaultFuncionarioShouldBeFound("atualizado.specified=true");

        // Get all the funcionarioList where atualizado is null
        defaultFuncionarioShouldNotBeFound("atualizado.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado is greater than or equal to DEFAULT_ATUALIZADO
        defaultFuncionarioShouldBeFound("atualizado.greaterThanOrEqual=" + DEFAULT_ATUALIZADO);

        // Get all the funcionarioList where atualizado is greater than or equal to UPDATED_ATUALIZADO
        defaultFuncionarioShouldNotBeFound("atualizado.greaterThanOrEqual=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado is less than or equal to DEFAULT_ATUALIZADO
        defaultFuncionarioShouldBeFound("atualizado.lessThanOrEqual=" + DEFAULT_ATUALIZADO);

        // Get all the funcionarioList where atualizado is less than or equal to SMALLER_ATUALIZADO
        defaultFuncionarioShouldNotBeFound("atualizado.lessThanOrEqual=" + SMALLER_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsLessThanSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado is less than DEFAULT_ATUALIZADO
        defaultFuncionarioShouldNotBeFound("atualizado.lessThan=" + DEFAULT_ATUALIZADO);

        // Get all the funcionarioList where atualizado is less than UPDATED_ATUALIZADO
        defaultFuncionarioShouldBeFound("atualizado.lessThan=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByAtualizadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where atualizado is greater than DEFAULT_ATUALIZADO
        defaultFuncionarioShouldNotBeFound("atualizado.greaterThan=" + DEFAULT_ATUALIZADO);

        // Get all the funcionarioList where atualizado is greater than SMALLER_ATUALIZADO
        defaultFuncionarioShouldBeFound("atualizado.greaterThan=" + SMALLER_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin equals to DEFAULT_ULTIMO_LOGIN
        defaultFuncionarioShouldBeFound("ultimoLogin.equals=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the funcionarioList where ultimoLogin equals to UPDATED_ULTIMO_LOGIN
        defaultFuncionarioShouldNotBeFound("ultimoLogin.equals=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin not equals to DEFAULT_ULTIMO_LOGIN
        defaultFuncionarioShouldNotBeFound("ultimoLogin.notEquals=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the funcionarioList where ultimoLogin not equals to UPDATED_ULTIMO_LOGIN
        defaultFuncionarioShouldBeFound("ultimoLogin.notEquals=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsInShouldWork() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin in DEFAULT_ULTIMO_LOGIN or UPDATED_ULTIMO_LOGIN
        defaultFuncionarioShouldBeFound("ultimoLogin.in=" + DEFAULT_ULTIMO_LOGIN + "," + UPDATED_ULTIMO_LOGIN);

        // Get all the funcionarioList where ultimoLogin equals to UPDATED_ULTIMO_LOGIN
        defaultFuncionarioShouldNotBeFound("ultimoLogin.in=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin is not null
        defaultFuncionarioShouldBeFound("ultimoLogin.specified=true");

        // Get all the funcionarioList where ultimoLogin is null
        defaultFuncionarioShouldNotBeFound("ultimoLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin is greater than or equal to DEFAULT_ULTIMO_LOGIN
        defaultFuncionarioShouldBeFound("ultimoLogin.greaterThanOrEqual=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the funcionarioList where ultimoLogin is greater than or equal to UPDATED_ULTIMO_LOGIN
        defaultFuncionarioShouldNotBeFound("ultimoLogin.greaterThanOrEqual=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin is less than or equal to DEFAULT_ULTIMO_LOGIN
        defaultFuncionarioShouldBeFound("ultimoLogin.lessThanOrEqual=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the funcionarioList where ultimoLogin is less than or equal to SMALLER_ULTIMO_LOGIN
        defaultFuncionarioShouldNotBeFound("ultimoLogin.lessThanOrEqual=" + SMALLER_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsLessThanSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin is less than DEFAULT_ULTIMO_LOGIN
        defaultFuncionarioShouldNotBeFound("ultimoLogin.lessThan=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the funcionarioList where ultimoLogin is less than UPDATED_ULTIMO_LOGIN
        defaultFuncionarioShouldBeFound("ultimoLogin.lessThan=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllFuncionariosByUltimoLoginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList where ultimoLogin is greater than DEFAULT_ULTIMO_LOGIN
        defaultFuncionarioShouldNotBeFound("ultimoLogin.greaterThan=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the funcionarioList where ultimoLogin is greater than SMALLER_ULTIMO_LOGIN
        defaultFuncionarioShouldBeFound("ultimoLogin.greaterThan=" + SMALLER_ULTIMO_LOGIN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFuncionarioShouldBeFound(String filter) throws Exception {
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].atualizado").value(hasItem(sameInstant(DEFAULT_ATUALIZADO))))
            .andExpect(jsonPath("$.[*].ultimoLogin").value(hasItem(sameInstant(DEFAULT_ULTIMO_LOGIN))));

        // Check, that the count call also returns 1
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFuncionarioShouldNotBeFound(String filter) throws Exception {
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFuncionario() throws Exception {
        // Get the funcionario
        restFuncionarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFuncionario() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();

        // Update the funcionario
        Funcionario updatedFuncionario = funcionarioRepository.findById(funcionario.getId()).get();
        // Disconnect from session so that the updates on updatedFuncionario are not directly saved in db
        em.detach(updatedFuncionario);
        updatedFuncionario
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .criado(UPDATED_CRIADO)
            .atualizado(UPDATED_ATUALIZADO)
            .ultimoLogin(UPDATED_ULTIMO_LOGIN);
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(updatedFuncionario);

        restFuncionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcionarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
        Funcionario testFuncionario = funcionarioList.get(funcionarioList.size() - 1);
        assertThat(testFuncionario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFuncionario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testFuncionario.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testFuncionario.getAtualizado()).isEqualTo(UPDATED_ATUALIZADO);
        assertThat(testFuncionario.getUltimoLogin()).isEqualTo(UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingFuncionario() throws Exception {
        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();
        funcionario.setId(count.incrementAndGet());

        // Create the Funcionario
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcionarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuncionario() throws Exception {
        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();
        funcionario.setId(count.incrementAndGet());

        // Create the Funcionario
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuncionario() throws Exception {
        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();
        funcionario.setId(count.incrementAndGet());

        // Create the Funcionario
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcionarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuncionarioWithPatch() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();

        // Update the funcionario using partial update
        Funcionario partialUpdatedFuncionario = new Funcionario();
        partialUpdatedFuncionario.setId(funcionario.getId());

        partialUpdatedFuncionario.ultimoLogin(UPDATED_ULTIMO_LOGIN);

        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionario))
            )
            .andExpect(status().isOk());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
        Funcionario testFuncionario = funcionarioList.get(funcionarioList.size() - 1);
        assertThat(testFuncionario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFuncionario.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testFuncionario.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testFuncionario.getAtualizado()).isEqualTo(DEFAULT_ATUALIZADO);
        assertThat(testFuncionario.getUltimoLogin()).isEqualTo(UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateFuncionarioWithPatch() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();

        // Update the funcionario using partial update
        Funcionario partialUpdatedFuncionario = new Funcionario();
        partialUpdatedFuncionario.setId(funcionario.getId());

        partialUpdatedFuncionario
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .criado(UPDATED_CRIADO)
            .atualizado(UPDATED_ATUALIZADO)
            .ultimoLogin(UPDATED_ULTIMO_LOGIN);

        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncionario))
            )
            .andExpect(status().isOk());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
        Funcionario testFuncionario = funcionarioList.get(funcionarioList.size() - 1);
        assertThat(testFuncionario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFuncionario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testFuncionario.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testFuncionario.getAtualizado()).isEqualTo(UPDATED_ATUALIZADO);
        assertThat(testFuncionario.getUltimoLogin()).isEqualTo(UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingFuncionario() throws Exception {
        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();
        funcionario.setId(count.incrementAndGet());

        // Create the Funcionario
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funcionarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuncionario() throws Exception {
        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();
        funcionario.setId(count.incrementAndGet());

        // Create the Funcionario
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuncionario() throws Exception {
        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();
        funcionario.setId(count.incrementAndGet());

        // Create the Funcionario
        FuncionarioDTO funcionarioDTO = funcionarioMapper.toDto(funcionario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(funcionarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuncionario() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        int databaseSizeBeforeDelete = funcionarioRepository.findAll().size();

        // Delete the funcionario
        restFuncionarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, funcionario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Funcionario> funcionarioList = funcionarioRepository.findAll();
        assertThat(funcionarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
