package br.com.consiga.web.rest;

import br.com.consiga.repository.MensalidadePedidoRepository;
import br.com.consiga.service.MensalidadePedidoQueryService;
import br.com.consiga.service.MensalidadePedidoService;
import br.com.consiga.service.criteria.MensalidadePedidoCriteria;
import br.com.consiga.service.dto.MensalidadePedidoDTO;
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
 * REST controller for managing {@link br.com.consiga.domain.MensalidadePedido}.
 */
@RestController
@RequestMapping("/api")
public class MensalidadePedidoResource {

    private final Logger log = LoggerFactory.getLogger(MensalidadePedidoResource.class);

    private static final String ENTITY_NAME = "mensalidadePedido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MensalidadePedidoService mensalidadePedidoService;

    private final MensalidadePedidoRepository mensalidadePedidoRepository;

    private final MensalidadePedidoQueryService mensalidadePedidoQueryService;

    public MensalidadePedidoResource(
        MensalidadePedidoService mensalidadePedidoService,
        MensalidadePedidoRepository mensalidadePedidoRepository,
        MensalidadePedidoQueryService mensalidadePedidoQueryService
    ) {
        this.mensalidadePedidoService = mensalidadePedidoService;
        this.mensalidadePedidoRepository = mensalidadePedidoRepository;
        this.mensalidadePedidoQueryService = mensalidadePedidoQueryService;
    }

    /**
     * {@code POST  /mensalidade-pedidos} : Create a new mensalidadePedido.
     *
     * @param mensalidadePedidoDTO the mensalidadePedidoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mensalidadePedidoDTO, or with status {@code 400 (Bad Request)} if the mensalidadePedido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mensalidade-pedidos")
    public ResponseEntity<MensalidadePedidoDTO> createMensalidadePedido(@RequestBody MensalidadePedidoDTO mensalidadePedidoDTO)
        throws URISyntaxException {
        log.debug("REST request to save MensalidadePedido : {}", mensalidadePedidoDTO);
        if (mensalidadePedidoDTO.getId() != null) {
            throw new BadRequestAlertException("A new mensalidadePedido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MensalidadePedidoDTO result = mensalidadePedidoService.save(mensalidadePedidoDTO);
        return ResponseEntity
            .created(new URI("/api/mensalidade-pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mensalidade-pedidos/:id} : Updates an existing mensalidadePedido.
     *
     * @param id the id of the mensalidadePedidoDTO to save.
     * @param mensalidadePedidoDTO the mensalidadePedidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mensalidadePedidoDTO,
     * or with status {@code 400 (Bad Request)} if the mensalidadePedidoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mensalidadePedidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mensalidade-pedidos/{id}")
    public ResponseEntity<MensalidadePedidoDTO> updateMensalidadePedido(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MensalidadePedidoDTO mensalidadePedidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MensalidadePedido : {}, {}", id, mensalidadePedidoDTO);
        if (mensalidadePedidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mensalidadePedidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mensalidadePedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MensalidadePedidoDTO result = mensalidadePedidoService.save(mensalidadePedidoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mensalidadePedidoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mensalidade-pedidos/:id} : Partial updates given fields of an existing mensalidadePedido, field will ignore if it is null
     *
     * @param id the id of the mensalidadePedidoDTO to save.
     * @param mensalidadePedidoDTO the mensalidadePedidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mensalidadePedidoDTO,
     * or with status {@code 400 (Bad Request)} if the mensalidadePedidoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mensalidadePedidoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mensalidadePedidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mensalidade-pedidos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MensalidadePedidoDTO> partialUpdateMensalidadePedido(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MensalidadePedidoDTO mensalidadePedidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MensalidadePedido partially : {}, {}", id, mensalidadePedidoDTO);
        if (mensalidadePedidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mensalidadePedidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mensalidadePedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MensalidadePedidoDTO> result = mensalidadePedidoService.partialUpdate(mensalidadePedidoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mensalidadePedidoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mensalidade-pedidos} : get all the mensalidadePedidos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mensalidadePedidos in body.
     */
    @GetMapping("/mensalidade-pedidos")
    public ResponseEntity<List<MensalidadePedidoDTO>> getAllMensalidadePedidos(MensalidadePedidoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MensalidadePedidos by criteria: {}", criteria);
        Page<MensalidadePedidoDTO> page = mensalidadePedidoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mensalidade-pedidos/count} : count all the mensalidadePedidos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mensalidade-pedidos/count")
    public ResponseEntity<Long> countMensalidadePedidos(MensalidadePedidoCriteria criteria) {
        log.debug("REST request to count MensalidadePedidos by criteria: {}", criteria);
        return ResponseEntity.ok().body(mensalidadePedidoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mensalidade-pedidos/:id} : get the "id" mensalidadePedido.
     *
     * @param id the id of the mensalidadePedidoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mensalidadePedidoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mensalidade-pedidos/{id}")
    public ResponseEntity<MensalidadePedidoDTO> getMensalidadePedido(@PathVariable Long id) {
        log.debug("REST request to get MensalidadePedido : {}", id);
        Optional<MensalidadePedidoDTO> mensalidadePedidoDTO = mensalidadePedidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mensalidadePedidoDTO);
    }

    /**
     * {@code DELETE  /mensalidade-pedidos/:id} : delete the "id" mensalidadePedido.
     *
     * @param id the id of the mensalidadePedidoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mensalidade-pedidos/{id}")
    public ResponseEntity<Void> deleteMensalidadePedido(@PathVariable Long id) {
        log.debug("REST request to delete MensalidadePedido : {}", id);
        mensalidadePedidoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
