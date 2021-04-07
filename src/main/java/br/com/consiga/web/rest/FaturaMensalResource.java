package br.com.consiga.web.rest;

import br.com.consiga.repository.FaturaMensalRepository;
import br.com.consiga.service.FaturaMensalQueryService;
import br.com.consiga.service.FaturaMensalService;
import br.com.consiga.service.criteria.FaturaMensalCriteria;
import br.com.consiga.service.dto.FaturaMensalDTO;
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
 * REST controller for managing {@link br.com.consiga.domain.FaturaMensal}.
 */
@RestController
@RequestMapping("/api")
public class FaturaMensalResource {

    private final Logger log = LoggerFactory.getLogger(FaturaMensalResource.class);

    private static final String ENTITY_NAME = "faturaMensal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FaturaMensalService faturaMensalService;

    private final FaturaMensalRepository faturaMensalRepository;

    private final FaturaMensalQueryService faturaMensalQueryService;

    public FaturaMensalResource(
        FaturaMensalService faturaMensalService,
        FaturaMensalRepository faturaMensalRepository,
        FaturaMensalQueryService faturaMensalQueryService
    ) {
        this.faturaMensalService = faturaMensalService;
        this.faturaMensalRepository = faturaMensalRepository;
        this.faturaMensalQueryService = faturaMensalQueryService;
    }

    /**
     * {@code POST  /fatura-mensals} : Create a new faturaMensal.
     *
     * @param faturaMensalDTO the faturaMensalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new faturaMensalDTO, or with status {@code 400 (Bad Request)} if the faturaMensal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fatura-mensals")
    public ResponseEntity<FaturaMensalDTO> createFaturaMensal(@RequestBody FaturaMensalDTO faturaMensalDTO) throws URISyntaxException {
        log.debug("REST request to save FaturaMensal : {}", faturaMensalDTO);
        if (faturaMensalDTO.getId() != null) {
            throw new BadRequestAlertException("A new faturaMensal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FaturaMensalDTO result = faturaMensalService.save(faturaMensalDTO);
        return ResponseEntity
            .created(new URI("/api/fatura-mensals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fatura-mensals/:id} : Updates an existing faturaMensal.
     *
     * @param id the id of the faturaMensalDTO to save.
     * @param faturaMensalDTO the faturaMensalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated faturaMensalDTO,
     * or with status {@code 400 (Bad Request)} if the faturaMensalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the faturaMensalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fatura-mensals/{id}")
    public ResponseEntity<FaturaMensalDTO> updateFaturaMensal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FaturaMensalDTO faturaMensalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FaturaMensal : {}, {}", id, faturaMensalDTO);
        if (faturaMensalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, faturaMensalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!faturaMensalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FaturaMensalDTO result = faturaMensalService.save(faturaMensalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, faturaMensalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fatura-mensals/:id} : Partial updates given fields of an existing faturaMensal, field will ignore if it is null
     *
     * @param id the id of the faturaMensalDTO to save.
     * @param faturaMensalDTO the faturaMensalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated faturaMensalDTO,
     * or with status {@code 400 (Bad Request)} if the faturaMensalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the faturaMensalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the faturaMensalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fatura-mensals/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FaturaMensalDTO> partialUpdateFaturaMensal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FaturaMensalDTO faturaMensalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FaturaMensal partially : {}, {}", id, faturaMensalDTO);
        if (faturaMensalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, faturaMensalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!faturaMensalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FaturaMensalDTO> result = faturaMensalService.partialUpdate(faturaMensalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, faturaMensalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fatura-mensals} : get all the faturaMensals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of faturaMensals in body.
     */
    @GetMapping("/fatura-mensals")
    public ResponseEntity<List<FaturaMensalDTO>> getAllFaturaMensals(FaturaMensalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FaturaMensals by criteria: {}", criteria);
        Page<FaturaMensalDTO> page = faturaMensalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fatura-mensals/count} : count all the faturaMensals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fatura-mensals/count")
    public ResponseEntity<Long> countFaturaMensals(FaturaMensalCriteria criteria) {
        log.debug("REST request to count FaturaMensals by criteria: {}", criteria);
        return ResponseEntity.ok().body(faturaMensalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fatura-mensals/:id} : get the "id" faturaMensal.
     *
     * @param id the id of the faturaMensalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the faturaMensalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fatura-mensals/{id}")
    public ResponseEntity<FaturaMensalDTO> getFaturaMensal(@PathVariable Long id) {
        log.debug("REST request to get FaturaMensal : {}", id);
        Optional<FaturaMensalDTO> faturaMensalDTO = faturaMensalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(faturaMensalDTO);
    }

    /**
     * {@code DELETE  /fatura-mensals/:id} : delete the "id" faturaMensal.
     *
     * @param id the id of the faturaMensalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fatura-mensals/{id}")
    public ResponseEntity<Void> deleteFaturaMensal(@PathVariable Long id) {
        log.debug("REST request to delete FaturaMensal : {}", id);
        faturaMensalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
