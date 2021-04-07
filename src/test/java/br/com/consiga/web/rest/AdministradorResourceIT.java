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
import br.com.consiga.repository.AdministradorRepository;
import br.com.consiga.service.criteria.AdministradorCriteria;
import br.com.consiga.service.dto.AdministradorDTO;
import br.com.consiga.service.mapper.AdministradorMapper;
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
 * Integration tests for the {@link AdministradorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdministradorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ATUALIZADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ATUALIZADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ATUALIZADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ULTIMO_LOGIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ULTIMO_LOGIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ULTIMO_LOGIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/administradors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private AdministradorMapper administradorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministradorMockMvc;

    private Administrador administrador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createEntity(EntityManager em) {
        Administrador administrador = new Administrador()
            .nome(DEFAULT_NOME)
            .criado(DEFAULT_CRIADO)
            .atualizado(DEFAULT_ATUALIZADO)
            .ultimoLogin(DEFAULT_ULTIMO_LOGIN);
        return administrador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createUpdatedEntity(EntityManager em) {
        Administrador administrador = new Administrador()
            .nome(UPDATED_NOME)
            .criado(UPDATED_CRIADO)
            .atualizado(UPDATED_ATUALIZADO)
            .ultimoLogin(UPDATED_ULTIMO_LOGIN);
        return administrador;
    }

    @BeforeEach
    public void initTest() {
        administrador = createEntity(em);
    }

    @Test
    @Transactional
    void createAdministrador() throws Exception {
        int databaseSizeBeforeCreate = administradorRepository.findAll().size();
        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);
        restAdministradorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeCreate + 1);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAdministrador.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testAdministrador.getAtualizado()).isEqualTo(DEFAULT_ATUALIZADO);
        assertThat(testAdministrador.getUltimoLogin()).isEqualTo(DEFAULT_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void createAdministradorWithExistingId() throws Exception {
        // Create the Administrador with an existing ID
        administrador.setId(1L);
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        int databaseSizeBeforeCreate = administradorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministradorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdministradors() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].atualizado").value(hasItem(sameInstant(DEFAULT_ATUALIZADO))))
            .andExpect(jsonPath("$.[*].ultimoLogin").value(hasItem(sameInstant(DEFAULT_ULTIMO_LOGIN))));
    }

    @Test
    @Transactional
    void getAdministrador() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get the administrador
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL_ID, administrador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrador.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.atualizado").value(sameInstant(DEFAULT_ATUALIZADO)))
            .andExpect(jsonPath("$.ultimoLogin").value(sameInstant(DEFAULT_ULTIMO_LOGIN)));
    }

    @Test
    @Transactional
    void getAdministradorsByIdFiltering() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        Long id = administrador.getId();

        defaultAdministradorShouldBeFound("id.equals=" + id);
        defaultAdministradorShouldNotBeFound("id.notEquals=" + id);

        defaultAdministradorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdministradorShouldNotBeFound("id.greaterThan=" + id);

        defaultAdministradorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdministradorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdministradorsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where nome equals to DEFAULT_NOME
        defaultAdministradorShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the administradorList where nome equals to UPDATED_NOME
        defaultAdministradorShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAdministradorsByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where nome not equals to DEFAULT_NOME
        defaultAdministradorShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the administradorList where nome not equals to UPDATED_NOME
        defaultAdministradorShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAdministradorsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultAdministradorShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the administradorList where nome equals to UPDATED_NOME
        defaultAdministradorShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAdministradorsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where nome is not null
        defaultAdministradorShouldBeFound("nome.specified=true");

        // Get all the administradorList where nome is null
        defaultAdministradorShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministradorsByNomeContainsSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where nome contains DEFAULT_NOME
        defaultAdministradorShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the administradorList where nome contains UPDATED_NOME
        defaultAdministradorShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAdministradorsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where nome does not contain DEFAULT_NOME
        defaultAdministradorShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the administradorList where nome does not contain UPDATED_NOME
        defaultAdministradorShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado equals to DEFAULT_CRIADO
        defaultAdministradorShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the administradorList where criado equals to UPDATED_CRIADO
        defaultAdministradorShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado not equals to DEFAULT_CRIADO
        defaultAdministradorShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the administradorList where criado not equals to UPDATED_CRIADO
        defaultAdministradorShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultAdministradorShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the administradorList where criado equals to UPDATED_CRIADO
        defaultAdministradorShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado is not null
        defaultAdministradorShouldBeFound("criado.specified=true");

        // Get all the administradorList where criado is null
        defaultAdministradorShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado is greater than or equal to DEFAULT_CRIADO
        defaultAdministradorShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the administradorList where criado is greater than or equal to UPDATED_CRIADO
        defaultAdministradorShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado is less than or equal to DEFAULT_CRIADO
        defaultAdministradorShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the administradorList where criado is less than or equal to SMALLER_CRIADO
        defaultAdministradorShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado is less than DEFAULT_CRIADO
        defaultAdministradorShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the administradorList where criado is less than UPDATED_CRIADO
        defaultAdministradorShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where criado is greater than DEFAULT_CRIADO
        defaultAdministradorShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the administradorList where criado is greater than SMALLER_CRIADO
        defaultAdministradorShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado equals to DEFAULT_ATUALIZADO
        defaultAdministradorShouldBeFound("atualizado.equals=" + DEFAULT_ATUALIZADO);

        // Get all the administradorList where atualizado equals to UPDATED_ATUALIZADO
        defaultAdministradorShouldNotBeFound("atualizado.equals=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado not equals to DEFAULT_ATUALIZADO
        defaultAdministradorShouldNotBeFound("atualizado.notEquals=" + DEFAULT_ATUALIZADO);

        // Get all the administradorList where atualizado not equals to UPDATED_ATUALIZADO
        defaultAdministradorShouldBeFound("atualizado.notEquals=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsInShouldWork() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado in DEFAULT_ATUALIZADO or UPDATED_ATUALIZADO
        defaultAdministradorShouldBeFound("atualizado.in=" + DEFAULT_ATUALIZADO + "," + UPDATED_ATUALIZADO);

        // Get all the administradorList where atualizado equals to UPDATED_ATUALIZADO
        defaultAdministradorShouldNotBeFound("atualizado.in=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado is not null
        defaultAdministradorShouldBeFound("atualizado.specified=true");

        // Get all the administradorList where atualizado is null
        defaultAdministradorShouldNotBeFound("atualizado.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado is greater than or equal to DEFAULT_ATUALIZADO
        defaultAdministradorShouldBeFound("atualizado.greaterThanOrEqual=" + DEFAULT_ATUALIZADO);

        // Get all the administradorList where atualizado is greater than or equal to UPDATED_ATUALIZADO
        defaultAdministradorShouldNotBeFound("atualizado.greaterThanOrEqual=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado is less than or equal to DEFAULT_ATUALIZADO
        defaultAdministradorShouldBeFound("atualizado.lessThanOrEqual=" + DEFAULT_ATUALIZADO);

        // Get all the administradorList where atualizado is less than or equal to SMALLER_ATUALIZADO
        defaultAdministradorShouldNotBeFound("atualizado.lessThanOrEqual=" + SMALLER_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsLessThanSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado is less than DEFAULT_ATUALIZADO
        defaultAdministradorShouldNotBeFound("atualizado.lessThan=" + DEFAULT_ATUALIZADO);

        // Get all the administradorList where atualizado is less than UPDATED_ATUALIZADO
        defaultAdministradorShouldBeFound("atualizado.lessThan=" + UPDATED_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByAtualizadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where atualizado is greater than DEFAULT_ATUALIZADO
        defaultAdministradorShouldNotBeFound("atualizado.greaterThan=" + DEFAULT_ATUALIZADO);

        // Get all the administradorList where atualizado is greater than SMALLER_ATUALIZADO
        defaultAdministradorShouldBeFound("atualizado.greaterThan=" + SMALLER_ATUALIZADO);
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin equals to DEFAULT_ULTIMO_LOGIN
        defaultAdministradorShouldBeFound("ultimoLogin.equals=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the administradorList where ultimoLogin equals to UPDATED_ULTIMO_LOGIN
        defaultAdministradorShouldNotBeFound("ultimoLogin.equals=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin not equals to DEFAULT_ULTIMO_LOGIN
        defaultAdministradorShouldNotBeFound("ultimoLogin.notEquals=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the administradorList where ultimoLogin not equals to UPDATED_ULTIMO_LOGIN
        defaultAdministradorShouldBeFound("ultimoLogin.notEquals=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsInShouldWork() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin in DEFAULT_ULTIMO_LOGIN or UPDATED_ULTIMO_LOGIN
        defaultAdministradorShouldBeFound("ultimoLogin.in=" + DEFAULT_ULTIMO_LOGIN + "," + UPDATED_ULTIMO_LOGIN);

        // Get all the administradorList where ultimoLogin equals to UPDATED_ULTIMO_LOGIN
        defaultAdministradorShouldNotBeFound("ultimoLogin.in=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin is not null
        defaultAdministradorShouldBeFound("ultimoLogin.specified=true");

        // Get all the administradorList where ultimoLogin is null
        defaultAdministradorShouldNotBeFound("ultimoLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin is greater than or equal to DEFAULT_ULTIMO_LOGIN
        defaultAdministradorShouldBeFound("ultimoLogin.greaterThanOrEqual=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the administradorList where ultimoLogin is greater than or equal to UPDATED_ULTIMO_LOGIN
        defaultAdministradorShouldNotBeFound("ultimoLogin.greaterThanOrEqual=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin is less than or equal to DEFAULT_ULTIMO_LOGIN
        defaultAdministradorShouldBeFound("ultimoLogin.lessThanOrEqual=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the administradorList where ultimoLogin is less than or equal to SMALLER_ULTIMO_LOGIN
        defaultAdministradorShouldNotBeFound("ultimoLogin.lessThanOrEqual=" + SMALLER_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsLessThanSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin is less than DEFAULT_ULTIMO_LOGIN
        defaultAdministradorShouldNotBeFound("ultimoLogin.lessThan=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the administradorList where ultimoLogin is less than UPDATED_ULTIMO_LOGIN
        defaultAdministradorShouldBeFound("ultimoLogin.lessThan=" + UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllAdministradorsByUltimoLoginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList where ultimoLogin is greater than DEFAULT_ULTIMO_LOGIN
        defaultAdministradorShouldNotBeFound("ultimoLogin.greaterThan=" + DEFAULT_ULTIMO_LOGIN);

        // Get all the administradorList where ultimoLogin is greater than SMALLER_ULTIMO_LOGIN
        defaultAdministradorShouldBeFound("ultimoLogin.greaterThan=" + SMALLER_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void getAllAdministradorsByEmpresasIsEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);
        Empresa empresas = EmpresaResourceIT.createEntity(em);
        em.persist(empresas);
        em.flush();
        administrador.addEmpresas(empresas);
        administradorRepository.saveAndFlush(administrador);
        Long empresasId = empresas.getId();

        // Get all the administradorList where empresas equals to empresasId
        defaultAdministradorShouldBeFound("empresasId.equals=" + empresasId);

        // Get all the administradorList where empresas equals to (empresasId + 1)
        defaultAdministradorShouldNotBeFound("empresasId.equals=" + (empresasId + 1));
    }

    @Test
    @Transactional
    void getAllAdministradorsByFiliaisIsEqualToSomething() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);
        Filial filiais = FilialResourceIT.createEntity(em);
        em.persist(filiais);
        em.flush();
        administrador.addFiliais(filiais);
        administradorRepository.saveAndFlush(administrador);
        Long filiaisId = filiais.getId();

        // Get all the administradorList where filiais equals to filiaisId
        defaultAdministradorShouldBeFound("filiaisId.equals=" + filiaisId);

        // Get all the administradorList where filiais equals to (filiaisId + 1)
        defaultAdministradorShouldNotBeFound("filiaisId.equals=" + (filiaisId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdministradorShouldBeFound(String filter) throws Exception {
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].atualizado").value(hasItem(sameInstant(DEFAULT_ATUALIZADO))))
            .andExpect(jsonPath("$.[*].ultimoLogin").value(hasItem(sameInstant(DEFAULT_ULTIMO_LOGIN))));

        // Check, that the count call also returns 1
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdministradorShouldNotBeFound(String filter) throws Exception {
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdministrador() throws Exception {
        // Get the administrador
        restAdministradorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdministrador() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();

        // Update the administrador
        Administrador updatedAdministrador = administradorRepository.findById(administrador.getId()).get();
        // Disconnect from session so that the updates on updatedAdministrador are not directly saved in db
        em.detach(updatedAdministrador);
        updatedAdministrador.nome(UPDATED_NOME).criado(UPDATED_CRIADO).atualizado(UPDATED_ATUALIZADO).ultimoLogin(UPDATED_ULTIMO_LOGIN);
        AdministradorDTO administradorDTO = administradorMapper.toDto(updatedAdministrador);

        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAdministrador.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testAdministrador.getAtualizado()).isEqualTo(UPDATED_ATUALIZADO);
        assertThat(testAdministrador.getUltimoLogin()).isEqualTo(UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        partialUpdatedAdministrador.criado(UPDATED_CRIADO).atualizado(UPDATED_ATUALIZADO).ultimoLogin(UPDATED_ULTIMO_LOGIN);

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAdministrador.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testAdministrador.getAtualizado()).isEqualTo(UPDATED_ATUALIZADO);
        assertThat(testAdministrador.getUltimoLogin()).isEqualTo(UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        partialUpdatedAdministrador
            .nome(UPDATED_NOME)
            .criado(UPDATED_CRIADO)
            .atualizado(UPDATED_ATUALIZADO)
            .ultimoLogin(UPDATED_ULTIMO_LOGIN);

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAdministrador.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testAdministrador.getAtualizado()).isEqualTo(UPDATED_ATUALIZADO);
        assertThat(testAdministrador.getUltimoLogin()).isEqualTo(UPDATED_ULTIMO_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administradorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administradorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrador() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeDelete = administradorRepository.findAll().size();

        // Delete the administrador
        restAdministradorMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
