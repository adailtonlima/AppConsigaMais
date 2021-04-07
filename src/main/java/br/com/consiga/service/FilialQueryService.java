package br.com.consiga.service;

import br.com.consiga.domain.*; // for static metamodels
import br.com.consiga.domain.Filial;
import br.com.consiga.repository.FilialRepository;
import br.com.consiga.service.criteria.FilialCriteria;
import br.com.consiga.service.dto.FilialDTO;
import br.com.consiga.service.mapper.FilialMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Filial} entities in the database.
 * The main input is a {@link FilialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FilialDTO} or a {@link Page} of {@link FilialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FilialQueryService extends QueryService<Filial> {

    private final Logger log = LoggerFactory.getLogger(FilialQueryService.class);

    private final FilialRepository filialRepository;

    private final FilialMapper filialMapper;

    public FilialQueryService(FilialRepository filialRepository, FilialMapper filialMapper) {
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
    }

    /**
     * Return a {@link List} of {@link FilialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FilialDTO> findByCriteria(FilialCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Filial> specification = createSpecification(criteria);
        return filialMapper.toDto(filialRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FilialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FilialDTO> findByCriteria(FilialCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Filial> specification = createSpecification(criteria);
        return filialRepository.findAll(specification, page).map(filialMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FilialCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Filial> specification = createSpecification(criteria);
        return filialRepository.count(specification);
    }

    /**
     * Function to convert {@link FilialCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Filial> createSpecification(FilialCriteria criteria) {
        Specification<Filial> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Filial_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Filial_.nome));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), Filial_.codigo));
            }
            if (criteria.getCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnpj(), Filial_.cnpj));
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(Filial_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
            if (criteria.getAdministradoresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAdministradoresId(),
                            root -> root.join(Filial_.administradores, JoinType.LEFT).get(Administrador_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
