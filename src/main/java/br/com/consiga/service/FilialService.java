package br.com.consiga.service;

import br.com.consiga.service.dto.FilialDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.consiga.domain.Filial}.
 */
public interface FilialService {
    /**
     * Save a filial.
     *
     * @param filialDTO the entity to save.
     * @return the persisted entity.
     */
    FilialDTO save(FilialDTO filialDTO);

    /**
     * Partially updates a filial.
     *
     * @param filialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FilialDTO> partialUpdate(FilialDTO filialDTO);

    /**
     * Get all the filials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FilialDTO> findAll(Pageable pageable);

    /**
     * Get all the filials with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FilialDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" filial.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FilialDTO> findOne(Long id);

    /**
     * Delete the "id" filial.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
