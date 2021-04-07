package br.com.consiga.service;

import br.com.consiga.service.dto.FuncionarioDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.consiga.domain.Funcionario}.
 */
public interface FuncionarioService {
    /**
     * Save a funcionario.
     *
     * @param funcionarioDTO the entity to save.
     * @return the persisted entity.
     */
    FuncionarioDTO save(FuncionarioDTO funcionarioDTO);

    /**
     * Partially updates a funcionario.
     *
     * @param funcionarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FuncionarioDTO> partialUpdate(FuncionarioDTO funcionarioDTO);

    /**
     * Get all the funcionarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FuncionarioDTO> findAll(Pageable pageable);

    /**
     * Get the "id" funcionario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FuncionarioDTO> findOne(Long id);

    /**
     * Delete the "id" funcionario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
