package br.com.consiga.web.rest;

import br.com.consiga.repository.ArquivoImportacaoRepository;
import br.com.consiga.service.ArquivoImportacaoQueryService;
import br.com.consiga.service.ArquivoImportacaoService;
import br.com.consiga.service.criteria.ArquivoImportacaoCriteria;
import br.com.consiga.service.dto.ArquivoImportacaoDTO;
import br.com.consiga.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.consiga.domain.ArquivoImportacao}.
 */
@RestController
@RequestMapping("/api")
public class ArquivoImportacaoResource {

    private final Logger log = LoggerFactory.getLogger(ArquivoImportacaoResource.class);

    private static final String ENTITY_NAME = "arquivoImportacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoImportacaoService arquivoImportacaoService;

    private final ArquivoImportacaoRepository arquivoImportacaoRepository;

    private final ArquivoImportacaoQueryService arquivoImportacaoQueryService;

    public ArquivoImportacaoResource(
        ArquivoImportacaoService arquivoImportacaoService,
        ArquivoImportacaoRepository arquivoImportacaoRepository,
        ArquivoImportacaoQueryService arquivoImportacaoQueryService
    ) {
        this.arquivoImportacaoService = arquivoImportacaoService;
        this.arquivoImportacaoRepository = arquivoImportacaoRepository;
        this.arquivoImportacaoQueryService = arquivoImportacaoQueryService;
    }

    /**
     * {@code POST  /arquivo-importacaos} : Create a new arquivoImportacao.
     *
     * @param arquivoImportacaoDTO the arquivoImportacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arquivoImportacaoDTO, or with status {@code 400 (Bad Request)} if the arquivoImportacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arquivo-importacaos")
    public ResponseEntity<ArquivoImportacaoDTO> createArquivoImportacao(@RequestBody ArquivoImportacaoDTO arquivoImportacaoDTO)
        throws URISyntaxException {
        log.debug("REST request to save ArquivoImportacao : {}", arquivoImportacaoDTO);
        if (arquivoImportacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new arquivoImportacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArquivoImportacaoDTO result = arquivoImportacaoService.save(arquivoImportacaoDTO);
        return ResponseEntity
            .created(new URI("/api/arquivo-importacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arquivo-importacaos/:id} : Updates an existing arquivoImportacao.
     *
     * @param id the id of the arquivoImportacaoDTO to save.
     * @param arquivoImportacaoDTO the arquivoImportacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoImportacaoDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoImportacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arquivoImportacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arquivo-importacaos/{id}")
    public ResponseEntity<ArquivoImportacaoDTO> updateArquivoImportacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArquivoImportacaoDTO arquivoImportacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ArquivoImportacao : {}, {}", id, arquivoImportacaoDTO);
        if (arquivoImportacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoImportacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoImportacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArquivoImportacaoDTO result = arquivoImportacaoService.save(arquivoImportacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoImportacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /arquivo-importacaos/:id} : Partial updates given fields of an existing arquivoImportacao, field will ignore if it is null
     *
     * @param id the id of the arquivoImportacaoDTO to save.
     * @param arquivoImportacaoDTO the arquivoImportacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoImportacaoDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoImportacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arquivoImportacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arquivoImportacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arquivo-importacaos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ArquivoImportacaoDTO> partialUpdateArquivoImportacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArquivoImportacaoDTO arquivoImportacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArquivoImportacao partially : {}, {}", id, arquivoImportacaoDTO);
        if (arquivoImportacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoImportacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoImportacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArquivoImportacaoDTO> result = arquivoImportacaoService.partialUpdate(arquivoImportacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoImportacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /arquivo-importacaos} : get all the arquivoImportacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivoImportacaos in body.
     */
    @GetMapping("/arquivo-importacaos")
    public ResponseEntity<List<ArquivoImportacaoDTO>> getAllArquivoImportacaos(ArquivoImportacaoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ArquivoImportacaos by criteria: {}", criteria);
        Page<ArquivoImportacaoDTO> page = arquivoImportacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arquivo-importacaos/count} : count all the arquivoImportacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/arquivo-importacaos/count")
    public ResponseEntity<Long> countArquivoImportacaos(ArquivoImportacaoCriteria criteria) {
        log.debug("REST request to count ArquivoImportacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(arquivoImportacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arquivo-importacaos/:id} : get the "id" arquivoImportacao.
     *
     * @param id the id of the arquivoImportacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivoImportacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arquivo-importacaos/{id}")
    public ResponseEntity<ArquivoImportacaoDTO> getArquivoImportacao(@PathVariable Long id) {
        log.debug("REST request to get ArquivoImportacao : {}", id);
        Optional<ArquivoImportacaoDTO> arquivoImportacaoDTO = arquivoImportacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arquivoImportacaoDTO);
    }

    /**
     * {@code DELETE  /arquivo-importacaos/:id} : delete the "id" arquivoImportacao.
     *
     * @param id the id of the arquivoImportacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arquivo-importacaos/{id}")
    public ResponseEntity<Void> deleteArquivoImportacao(@PathVariable Long id) {
        log.debug("REST request to delete ArquivoImportacao : {}", id);
        arquivoImportacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
