package br.com.consiga.repository;

import br.com.consiga.domain.ArquivoImportacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ArquivoImportacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArquivoImportacaoRepository extends JpaRepository<ArquivoImportacao, Long>, JpaSpecificationExecutor<ArquivoImportacao> {}
