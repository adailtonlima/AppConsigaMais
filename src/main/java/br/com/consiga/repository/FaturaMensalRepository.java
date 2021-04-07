package br.com.consiga.repository;

import br.com.consiga.domain.FaturaMensal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FaturaMensal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FaturaMensalRepository extends JpaRepository<FaturaMensal, Long>, JpaSpecificationExecutor<FaturaMensal> {}
