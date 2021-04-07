package br.com.consiga.service;

import br.com.consiga.domain.*; // for static metamodels
import br.com.consiga.domain.Estado;
import br.com.consiga.repository.EstadoRepository;
import br.com.consiga.service.criteria.EstadoCriteria;
import br.com.consiga.service.dto.EstadoDTO;
import br.com.consiga.service.mapper.EstadoMapper;
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
 * Service for executing complex queries for {@link Estado} entities in the database.
 * The main input is a {@link EstadoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EstadoDTO} or a {@link Page} of {@link EstadoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstadoQueryService extends QueryService<Estado> {

    private final Logger log = LoggerFactory.getLogger(EstadoQueryService.class);

    private final EstadoRepository estadoRepository;

    private final EstadoMapper estadoMapper;

    public EstadoQueryService(EstadoRepository estadoRepository, EstadoMapper estadoMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
    }

    /**
     * Return a {@link List} of {@link EstadoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EstadoDTO> findByCriteria(EstadoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Estado> specification = createSpecification(criteria);
        return estadoMapper.toDto(estadoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EstadoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstadoDTO> findByCriteria(EstadoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Estado> specification = createSpecification(criteria);
        return estadoRepository.findAll(specification, page).map(estadoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstadoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Estado> specification = createSpecification(criteria);
        return estadoRepository.count(specification);
    }

    /**
     * Function to convert {@link EstadoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Estado> createSpecification(EstadoCriteria criteria) {
        Specification<Estado> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Estado_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Estado_.nome));
            }
        }
        return specification;
    }
}
