package br.com.consiga.service;

import br.com.consiga.service.dto.MensalidadePedidoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.consiga.domain.MensalidadePedido}.
 */
public interface MensalidadePedidoService {
    /**
     * Save a mensalidadePedido.
     *
     * @param mensalidadePedidoDTO the entity to save.
     * @return the persisted entity.
     */
    MensalidadePedidoDTO save(MensalidadePedidoDTO mensalidadePedidoDTO);

    /**
     * Partially updates a mensalidadePedido.
     *
     * @param mensalidadePedidoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MensalidadePedidoDTO> partialUpdate(MensalidadePedidoDTO mensalidadePedidoDTO);

    /**
     * Get all the mensalidadePedidos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MensalidadePedidoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mensalidadePedido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MensalidadePedidoDTO> findOne(Long id);

    /**
     * Delete the "id" mensalidadePedido.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
