package br.com.consiga.web.rest;

import br.com.consiga.repository.AdministradorRepository;
import br.com.consiga.service.AdministradorQueryService;
import br.com.consiga.service.AdministradorService;
import br.com.consiga.service.criteria.AdministradorCriteria;
import br.com.consiga.service.dto.AdministradorDTO;
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
 * REST controller for managing {@link br.com.consiga.domain.Administrador}.
 */
@RestController
@RequestMapping("/api")
public class AdministradorResource {

    private final Logger log = LoggerFactory.getLogger(AdministradorResource.class);

    private static final String ENTITY_NAME = "administrador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdministradorService administradorService;

    private final AdministradorRepository administradorRepository;

    private final AdministradorQueryService administradorQueryService;

    public AdministradorResource(
        AdministradorService administradorService,
        AdministradorRepository administradorRepository,
        AdministradorQueryService administradorQueryService
    ) {
        this.administradorService = administradorService;
        this.administradorRepository = administradorRepository;
        this.administradorQueryService = administradorQueryService;
    }

    /**
     * {@code POST  /administradors} : Create a new administrador.
     *
     * @param administradorDTO the administradorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new administradorDTO, or with status {@code 400 (Bad Request)} if the administrador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/administradors")
    public ResponseEntity<AdministradorDTO> createAdministrador(@RequestBody AdministradorDTO administradorDTO) throws URISyntaxException {
        log.debug("REST request to save Administrador : {}", administradorDTO);
        if (administradorDTO.getId() != null) {
            throw new BadRequestAlertException("A new administrador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdministradorDTO result = administradorService.save(administradorDTO);
        return ResponseEntity
            .created(new URI("/api/administradors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /administradors/:id} : Updates an existing administrador.
     *
     * @param id the id of the administradorDTO to save.
     * @param administradorDTO the administradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administradorDTO,
     * or with status {@code 400 (Bad Request)} if the administradorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the administradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/administradors/{id}")
    public ResponseEntity<AdministradorDTO> updateAdministrador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdministradorDTO administradorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Administrador : {}, {}", id, administradorDTO);
        if (administradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdministradorDTO result = administradorService.save(administradorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administradorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /administradors/:id} : Partial updates given fields of an existing administrador, field will ignore if it is null
     *
     * @param id the id of the administradorDTO to save.
     * @param administradorDTO the administradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administradorDTO,
     * or with status {@code 400 (Bad Request)} if the administradorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the administradorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the administradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/administradors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AdministradorDTO> partialUpdateAdministrador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdministradorDTO administradorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Administrador partially : {}, {}", id, administradorDTO);
        if (administradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdministradorDTO> result = administradorService.partialUpdate(administradorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administradorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /administradors} : get all the administradors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of administradors in body.
     */
    @GetMapping("/administradors")
    public ResponseEntity<List<AdministradorDTO>> getAllAdministradors(AdministradorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Administradors by criteria: {}", criteria);
        Page<AdministradorDTO> page = administradorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /administradors/count} : count all the administradors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/administradors/count")
    public ResponseEntity<Long> countAdministradors(AdministradorCriteria criteria) {
        log.debug("REST request to count Administradors by criteria: {}", criteria);
        return ResponseEntity.ok().body(administradorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /administradors/:id} : get the "id" administrador.
     *
     * @param id the id of the administradorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the administradorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/administradors/{id}")
    public ResponseEntity<AdministradorDTO> getAdministrador(@PathVariable Long id) {
        log.debug("REST request to get Administrador : {}", id);
        Optional<AdministradorDTO> administradorDTO = administradorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(administradorDTO);
    }

    /**
     * {@code DELETE  /administradors/:id} : delete the "id" administrador.
     *
     * @param id the id of the administradorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/administradors/{id}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable Long id) {
        log.debug("REST request to delete Administrador : {}", id);
        administradorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
