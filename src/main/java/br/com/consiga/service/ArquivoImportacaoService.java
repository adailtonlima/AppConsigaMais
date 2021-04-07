package br.com.consiga.service;

import br.com.consiga.service.dto.ArquivoImportacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.consiga.domain.ArquivoImportacao}.
 */
public interface ArquivoImportacaoService {
    /**
     * Save a arquivoImportacao.
     *
     * @param arquivoImportacaoDTO the entity to save.
     * @return the persisted entity.
     */
    ArquivoImportacaoDTO save(ArquivoImportacaoDTO arquivoImportacaoDTO);

    /**
     * Partially updates a arquivoImportacao.
     *
     * @param arquivoImportacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArquivoImportacaoDTO> partialUpdate(ArquivoImportacaoDTO arquivoImportacaoDTO);

    /**
     * Get all the arquivoImportacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArquivoImportacaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" arquivoImportacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArquivoImportacaoDTO> findOne(Long id);

    /**
     * Delete the "id" arquivoImportacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
