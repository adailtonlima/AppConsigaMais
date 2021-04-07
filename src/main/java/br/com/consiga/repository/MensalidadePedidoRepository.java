package br.com.consiga.repository;

import br.com.consiga.domain.MensalidadePedido;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MensalidadePedido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MensalidadePedidoRepository extends JpaRepository<MensalidadePedido, Long>, JpaSpecificationExecutor<MensalidadePedido> {}
