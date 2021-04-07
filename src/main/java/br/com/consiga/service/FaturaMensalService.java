package br.com.consiga.service;

import br.com.consiga.service.dto.FaturaMensalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.consiga.domain.FaturaMensal}.
 */
public interface FaturaMensalService {
    /**
     * Save a faturaMensal.
     *
     * @param faturaMensalDTO the entity to save.
     * @return the persisted entity.
     */
    FaturaMensalDTO save(FaturaMensalDTO faturaMensalDTO);

    /**
     * Partially updates a faturaMensal.
     *
     * @param faturaMensalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FaturaMensalDTO> partialUpdate(FaturaMensalDTO faturaMensalDTO);

    /**
     * Get all the faturaMensals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FaturaMensalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" faturaMensal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FaturaMensalDTO> findOne(Long id);

    /**
     * Delete the "id" faturaMensal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
