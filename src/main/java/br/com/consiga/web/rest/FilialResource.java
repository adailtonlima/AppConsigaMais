package br.com.consiga.web.rest;

import br.com.consiga.repository.FilialRepository;
import br.com.consiga.service.FilialQueryService;
import br.com.consiga.service.FilialService;
import br.com.consiga.service.criteria.FilialCriteria;
import br.com.consiga.service.dto.FilialDTO;
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
 * REST controller for managing {@link br.com.consiga.domain.Filial}.
 */
@RestController
@RequestMapping("/api")
public class FilialResource {

    private final Logger log = LoggerFactory.getLogger(FilialResource.class);

    private static final String ENTITY_NAME = "filial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilialService filialService;

    private final FilialRepository filialRepository;

    private final FilialQueryService filialQueryService;

    public FilialResource(FilialService filialService, FilialRepository filialRepository, FilialQueryService filialQueryService) {
        this.filialService = filialService;
        this.filialRepository = filialRepository;
        this.filialQueryService = filialQueryService;
    }

    /**
     * {@code POST  /filials} : Create a new filial.
     *
     * @param filialDTO the filialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filialDTO, or with status {@code 400 (Bad Request)} if the filial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filials")
    public ResponseEntity<FilialDTO> createFilial(@RequestBody FilialDTO filialDTO) throws URISyntaxException {
        log.debug("REST request to save Filial : {}", filialDTO);
        if (filialDTO.getId() != null) {
            throw new BadRequestAlertException("A new filial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilialDTO result = filialService.save(filialDTO);
        return ResponseEntity
            .created(new URI("/api/filials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filials/:id} : Updates an existing filial.
     *
     * @param id the id of the filialDTO to save.
     * @param filialDTO the filialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filialDTO,
     * or with status {@code 400 (Bad Request)} if the filialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filials/{id}")
    public ResponseEntity<FilialDTO> updateFilial(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilialDTO filialDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Filial : {}, {}", id, filialDTO);
        if (filialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilialDTO result = filialService.save(filialDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filials/:id} : Partial updates given fields of an existing filial, field will ignore if it is null
     *
     * @param id the id of the filialDTO to save.
     * @param filialDTO the filialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filialDTO,
     * or with status {@code 400 (Bad Request)} if the filialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/filials/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FilialDTO> partialUpdateFilial(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilialDTO filialDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Filial partially : {}, {}", id, filialDTO);
        if (filialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilialDTO> result = filialService.partialUpdate(filialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /filials} : get all the filials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filials in body.
     */
    @GetMapping("/filials")
    public ResponseEntity<List<FilialDTO>> getAllFilials(FilialCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Filials by criteria: {}", criteria);
        Page<FilialDTO> page = filialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filials/count} : count all the filials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/filials/count")
    public ResponseEntity<Long> countFilials(FilialCriteria criteria) {
        log.debug("REST request to count Filials by criteria: {}", criteria);
        return ResponseEntity.ok().body(filialQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /filials/:id} : get the "id" filial.
     *
     * @param id the id of the filialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filials/{id}")
    public ResponseEntity<FilialDTO> getFilial(@PathVariable Long id) {
        log.debug("REST request to get Filial : {}", id);
        Optional<FilialDTO> filialDTO = filialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filialDTO);
    }

    /**
     * {@code DELETE  /filials/:id} : delete the "id" filial.
     *
     * @param id the id of the filialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filials/{id}")
    public ResponseEntity<Void> deleteFilial(@PathVariable Long id) {
        log.debug("REST request to delete Filial : {}", id);
        filialService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
