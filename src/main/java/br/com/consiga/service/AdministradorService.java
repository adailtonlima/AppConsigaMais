package br.com.consiga.service;

import br.com.consiga.service.dto.AdministradorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.consiga.domain.Administrador}.
 */
public interface AdministradorService {
    /**
     * Save a administrador.
     *
     * @param administradorDTO the entity to save.
     * @return the persisted entity.
     */
    AdministradorDTO save(AdministradorDTO administradorDTO);

    /**
     * Partially updates a administrador.
     *
     * @param administradorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdministradorDTO> partialUpdate(AdministradorDTO administradorDTO);

    /**
     * Get all the administradors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdministradorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" administrador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdministradorDTO> findOne(Long id);

    /**
     * Delete the "id" administrador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
