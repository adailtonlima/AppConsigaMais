package br.com.consiga.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.consiga.IntegrationTest;
import br.com.consiga.domain.Administrador;
import br.com.consiga.domain.Empresa;
import br.com.consiga.domain.Filial;
import br.com.consiga.repository.FilialRepository;
import br.com.consiga.service.FilialService;
import br.com.consiga.service.criteria.FilialCriteria;
import br.com.consiga.service.dto.FilialDTO;
import br.com.consiga.service.mapper.FilialMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FilialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FilialResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilialRepository filialRepository;

    @Mock
    private FilialRepository filialRepositoryMock;

    @Autowired
    private FilialMapper filialMapper;

    @Mock
    private FilialService filialServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilialMockMvc;

    private Filial filial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filial createEntity(EntityManager em) {
        Filial filial = new Filial().nome(DEFAULT_NOME).codigo(DEFAULT_CODIGO).cnpj(DEFAULT_CNPJ);
        return filial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filial createUpdatedEntity(EntityManager em) {
        Filial filial = new Filial().nome(UPDATED_NOME).codigo(UPDATED_CODIGO).cnpj(UPDATED_CNPJ);
        return filial;
    }

    @BeforeEach
    public void initTest() {
        filial = createEntity(em);
    }

    @Test
    @Transactional
    void createFilial() throws Exception {
        int databaseSizeBeforeCreate = filialRepository.findAll().size();
        // Create the Filial
        FilialDTO filialDTO = filialMapper.toDto(filial);
        restFilialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filialDTO)))
            .andExpect(status().isCreated());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeCreate + 1);
        Filial testFilial = filialList.get(filialList.size() - 1);
        assertThat(testFilial.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFilial.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testFilial.getCnpj()).isEqualTo(DEFAULT_CNPJ);
    }

    @Test
    @Transactional
    void createFilialWithExistingId() throws Exception {
        // Create the Filial with an existing ID
        filial.setId(1L);
        FilialDTO filialDTO = filialMapper.toDto(filial);

        int databaseSizeBeforeCreate = filialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFilials() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList
        restFilialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filial.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilialsWithEagerRelationshipsIsEnabled() throws Exception {
        when(filialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilialsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(filialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFilial() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get the filial
        restFilialMockMvc
            .perform(get(ENTITY_API_URL_ID, filial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filial.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ));
    }

    @Test
    @Transactional
    void getFilialsByIdFiltering() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        Long id = filial.getId();

        defaultFilialShouldBeFound("id.equals=" + id);
        defaultFilialShouldNotBeFound("id.notEquals=" + id);

        defaultFilialShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFilialShouldNotBeFound("id.greaterThan=" + id);

        defaultFilialShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFilialShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFilialsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where nome equals to DEFAULT_NOME
        defaultFilialShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the filialList where nome equals to UPDATED_NOME
        defaultFilialShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFilialsByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where nome not equals to DEFAULT_NOME
        defaultFilialShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the filialList where nome not equals to UPDATED_NOME
        defaultFilialShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFilialsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultFilialShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the filialList where nome equals to UPDATED_NOME
        defaultFilialShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFilialsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where nome is not null
        defaultFilialShouldBeFound("nome.specified=true");

        // Get all the filialList where nome is null
        defaultFilialShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllFilialsByNomeContainsSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where nome contains DEFAULT_NOME
        defaultFilialShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the filialList where nome contains UPDATED_NOME
        defaultFilialShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFilialsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where nome does not contain DEFAULT_NOME
        defaultFilialShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the filialList where nome does not contain UPDATED_NOME
        defaultFilialShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFilialsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where codigo equals to DEFAULT_CODIGO
        defaultFilialShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the filialList where codigo equals to UPDATED_CODIGO
        defaultFilialShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFilialsByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where codigo not equals to DEFAULT_CODIGO
        defaultFilialShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the filialList where codigo not equals to UPDATED_CODIGO
        defaultFilialShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFilialsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultFilialShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the filialList where codigo equals to UPDATED_CODIGO
        defaultFilialShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFilialsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where codigo is not null
        defaultFilialShouldBeFound("codigo.specified=true");

        // Get all the filialList where codigo is null
        defaultFilialShouldNotBeFound("codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllFilialsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where codigo contains DEFAULT_CODIGO
        defaultFilialShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the filialList where codigo contains UPDATED_CODIGO
        defaultFilialShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFilialsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where codigo does not contain DEFAULT_CODIGO
        defaultFilialShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the filialList where codigo does not contain UPDATED_CODIGO
        defaultFilialShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFilialsByCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where cnpj equals to DEFAULT_CNPJ
        defaultFilialShouldBeFound("cnpj.equals=" + DEFAULT_CNPJ);

        // Get all the filialList where cnpj equals to UPDATED_CNPJ
        defaultFilialShouldNotBeFound("cnpj.equals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllFilialsByCnpjIsNotEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where cnpj not equals to DEFAULT_CNPJ
        defaultFilialShouldNotBeFound("cnpj.notEquals=" + DEFAULT_CNPJ);

        // Get all the filialList where cnpj not equals to UPDATED_CNPJ
        defaultFilialShouldBeFound("cnpj.notEquals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllFilialsByCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where cnpj in DEFAULT_CNPJ or UPDATED_CNPJ
        defaultFilialShouldBeFound("cnpj.in=" + DEFAULT_CNPJ + "," + UPDATED_CNPJ);

        // Get all the filialList where cnpj equals to UPDATED_CNPJ
        defaultFilialShouldNotBeFound("cnpj.in=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllFilialsByCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where cnpj is not null
        defaultFilialShouldBeFound("cnpj.specified=true");

        // Get all the filialList where cnpj is null
        defaultFilialShouldNotBeFound("cnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllFilialsByCnpjContainsSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where cnpj contains DEFAULT_CNPJ
        defaultFilialShouldBeFound("cnpj.contains=" + DEFAULT_CNPJ);

        // Get all the filialList where cnpj contains UPDATED_CNPJ
        defaultFilialShouldNotBeFound("cnpj.contains=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllFilialsByCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        // Get all the filialList where cnpj does not contain DEFAULT_CNPJ
        defaultFilialShouldNotBeFound("cnpj.doesNotContain=" + DEFAULT_CNPJ);

        // Get all the filialList where cnpj does not contain UPDATED_CNPJ
        defaultFilialShouldBeFound("cnpj.doesNotContain=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllFilialsByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);
        Empresa empresa = EmpresaResourceIT.createEntity(em);
        em.persist(empresa);
        em.flush();
        filial.setEmpresa(empresa);
        filialRepository.saveAndFlush(filial);
        Long empresaId = empresa.getId();

        // Get all the filialList where empresa equals to empresaId
        defaultFilialShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the filialList where empresa equals to (empresaId + 1)
        defaultFilialShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllFilialsByAdministradoresIsEqualToSomething() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);
        Administrador administradores = AdministradorResourceIT.createEntity(em);
        em.persist(administradores);
        em.flush();
        filial.addAdministradores(administradores);
        filialRepository.saveAndFlush(filial);
        Long administradoresId = administradores.getId();

        // Get all the filialList where administradores equals to administradoresId
        defaultFilialShouldBeFound("administradoresId.equals=" + administradoresId);

        // Get all the filialList where administradores equals to (administradoresId + 1)
        defaultFilialShouldNotBeFound("administradoresId.equals=" + (administradoresId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFilialShouldBeFound(String filter) throws Exception {
        restFilialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filial.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)));

        // Check, that the count call also returns 1
        restFilialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFilialShouldNotBeFound(String filter) throws Exception {
        restFilialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFilialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFilial() throws Exception {
        // Get the filial
        restFilialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFilial() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        int databaseSizeBeforeUpdate = filialRepository.findAll().size();

        // Update the filial
        Filial updatedFilial = filialRepository.findById(filial.getId()).get();
        // Disconnect from session so that the updates on updatedFilial are not directly saved in db
        em.detach(updatedFilial);
        updatedFilial.nome(UPDATED_NOME).codigo(UPDATED_CODIGO).cnpj(UPDATED_CNPJ);
        FilialDTO filialDTO = filialMapper.toDto(updatedFilial);

        restFilialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
        Filial testFilial = filialList.get(filialList.size() - 1);
        assertThat(testFilial.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFilial.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testFilial.getCnpj()).isEqualTo(UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void putNonExistingFilial() throws Exception {
        int databaseSizeBeforeUpdate = filialRepository.findAll().size();
        filial.setId(count.incrementAndGet());

        // Create the Filial
        FilialDTO filialDTO = filialMapper.toDto(filial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilial() throws Exception {
        int databaseSizeBeforeUpdate = filialRepository.findAll().size();
        filial.setId(count.incrementAndGet());

        // Create the Filial
        FilialDTO filialDTO = filialMapper.toDto(filial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilial() throws Exception {
        int databaseSizeBeforeUpdate = filialRepository.findAll().size();
        filial.setId(count.incrementAndGet());

        // Create the Filial
        FilialDTO filialDTO = filialMapper.toDto(filial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilialWithPatch() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        int databaseSizeBeforeUpdate = filialRepository.findAll().size();

        // Update the filial using partial update
        Filial partialUpdatedFilial = new Filial();
        partialUpdatedFilial.setId(filial.getId());

        partialUpdatedFilial.nome(UPDATED_NOME);

        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilial))
            )
            .andExpect(status().isOk());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
        Filial testFilial = filialList.get(filialList.size() - 1);
        assertThat(testFilial.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFilial.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testFilial.getCnpj()).isEqualTo(DEFAULT_CNPJ);
    }

    @Test
    @Transactional
    void fullUpdateFilialWithPatch() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        int databaseSizeBeforeUpdate = filialRepository.findAll().size();

        // Update the filial using partial update
        Filial partialUpdatedFilial = new Filial();
        partialUpdatedFilial.setId(filial.getId());

        partialUpdatedFilial.nome(UPDATED_NOME).codigo(UPDATED_CODIGO).cnpj(UPDATED_CNPJ);

        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilial))
            )
            .andExpect(status().isOk());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
        Filial testFilial = filialList.get(filialList.size() - 1);
        assertThat(testFilial.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFilial.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testFilial.getCnpj()).isEqualTo(UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void patchNonExistingFilial() throws Exception {
        int databaseSizeBeforeUpdate = filialRepository.findAll().size();
        filial.setId(count.incrementAndGet());

        // Create the Filial
        FilialDTO filialDTO = filialMapper.toDto(filial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilial() throws Exception {
        int databaseSizeBeforeUpdate = filialRepository.findAll().size();
        filial.setId(count.incrementAndGet());

        // Create the Filial
        FilialDTO filialDTO = filialMapper.toDto(filial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilial() throws Exception {
        int databaseSizeBeforeUpdate = filialRepository.findAll().size();
        filial.setId(count.incrementAndGet());

        // Create the Filial
        FilialDTO filialDTO = filialMapper.toDto(filial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filial in the database
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilial() throws Exception {
        // Initialize the database
        filialRepository.saveAndFlush(filial);

        int databaseSizeBeforeDelete = filialRepository.findAll().size();

        // Delete the filial
        restFilialMockMvc
            .perform(delete(ENTITY_API_URL_ID, filial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Filial> filialList = filialRepository.findAll();
        assertThat(filialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
