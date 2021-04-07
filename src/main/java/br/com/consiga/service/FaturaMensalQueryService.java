package br.com.consiga.service;

import br.com.consiga.domain.*; // for static metamodels
import br.com.consiga.domain.FaturaMensal;
import br.com.consiga.repository.FaturaMensalRepository;
import br.com.consiga.service.criteria.FaturaMensalCriteria;
import br.com.consiga.service.dto.FaturaMensalDTO;
import br.com.consiga.service.mapper.FaturaMensalMapper;
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
 * Service for executing complex queries for {@link FaturaMensal} entities in the database.
 * The main input is a {@link FaturaMensalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FaturaMensalDTO} or a {@link Page} of {@link FaturaMensalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FaturaMensalQueryService extends QueryService<FaturaMensal> {

    private final Logger log = LoggerFactory.getLogger(FaturaMensalQueryService.class);

    private final FaturaMensalRepository faturaMensalRepository;

    private final FaturaMensalMapper faturaMensalMapper;

    public FaturaMensalQueryService(FaturaMensalRepository faturaMensalRepository, FaturaMensalMapper faturaMensalMapper) {
        this.faturaMensalRepository = faturaMensalRepository;
        this.faturaMensalMapper = faturaMensalMapper;
    }

    /**
     * Return a {@link List} of {@link FaturaMensalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FaturaMensalDTO> findByCriteria(FaturaMensalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FaturaMensal> specification = createSpecification(criteria);
        return faturaMensalMapper.toDto(faturaMensalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FaturaMensalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FaturaMensalDTO> findByCriteria(FaturaMensalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FaturaMensal> specification = createSpecification(criteria);
        return faturaMensalRepository.findAll(specification, page).map(faturaMensalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FaturaMensalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FaturaMensal> specification = createSpecification(criteria);
        return faturaMensalRepository.count(specification);
    }

    /**
     * Function to convert {@link FaturaMensalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FaturaMensal> createSpecification(FaturaMensalCriteria criteria) {
        Specification<FaturaMensal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FaturaMensal_.id));
            }
            if (criteria.getMes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMes(), FaturaMensal_.mes));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), FaturaMensal_.criado));
            }
            if (criteria.getBoletoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBoletoUrl(), FaturaMensal_.boletoUrl));
            }
            if (criteria.getDataPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPago(), FaturaMensal_.dataPago));
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmpresaId(),
                            root -> root.join(FaturaMensal_.empresa, JoinType.LEFT).get(Empresa_.id)
                        )
                    );
            }
            if (criteria.getFilialId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFilialId(), root -> root.join(FaturaMensal_.filial, JoinType.LEFT).get(Filial_.id))
                    );
            }
        }
        return specification;
    }
}
