package br.com.consiga.repository;

import br.com.consiga.domain.Filial;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Filial entity.
 */
@Repository
public interface FilialRepository extends JpaRepository<Filial, Long>, JpaSpecificationExecutor<Filial> {
    @Query(
        value = "select distinct filial from Filial filial left join fetch filial.administradores",
        countQuery = "select count(distinct filial) from Filial filial"
    )
    Page<Filial> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct filial from Filial filial left join fetch filial.administradores")
    List<Filial> findAllWithEagerRelationships();

    @Query("select filial from Filial filial left join fetch filial.administradores where filial.id =:id")
    Optional<Filial> findOneWithEagerRelationships(@Param("id") Long id);
}
