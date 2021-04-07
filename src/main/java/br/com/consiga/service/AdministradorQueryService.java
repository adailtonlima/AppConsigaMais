package br.com.consiga.service;

import br.com.consiga.domain.*; // for static metamodels
import br.com.consiga.domain.Administrador;
import br.com.consiga.repository.AdministradorRepository;
import br.com.consiga.service.criteria.AdministradorCriteria;
import br.com.consiga.service.dto.AdministradorDTO;
import br.com.consiga.service.mapper.AdministradorMapper;
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
 * Service for executing complex queries for {@link Administrador} entities in the database.
 * The main input is a {@link AdministradorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdministradorDTO} or a {@link Page} of {@link AdministradorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdministradorQueryService extends QueryService<Administrador> {

    private final Logger log = LoggerFactory.getLogger(AdministradorQueryService.class);

    private final AdministradorRepository administradorRepository;

    private final AdministradorMapper administradorMapper;

    public AdministradorQueryService(AdministradorRepository administradorRepository, AdministradorMapper administradorMapper) {
        this.administradorRepository = administradorRepository;
        this.administradorMapper = administradorMapper;
    }

    /**
     * Return a {@link List} of {@link AdministradorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdministradorDTO> findByCriteria(AdministradorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Administrador> specification = createSpecification(criteria);
        return administradorMapper.toDto(administradorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdministradorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdministradorDTO> findByCriteria(AdministradorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Administrador> specification = createSpecification(criteria);
        return administradorRepository.findAll(specification, page).map(administradorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdministradorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Administrador> specification = createSpecification(criteria);
        return administradorRepository.count(specification);
    }

    /**
     * Function to convert {@link AdministradorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Administrador> createSpecification(AdministradorCriteria criteria) {
        Specification<Administrador> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Administrador_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Administrador_.nome));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Administrador_.criado));
            }
            if (criteria.getAtualizado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAtualizado(), Administrador_.atualizado));
            }
            if (criteria.getUltimoLogin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUltimoLogin(), Administrador_.ultimoLogin));
            }
            if (criteria.getEmpresasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmpresasId(),
                            root -> root.join(Administrador_.empresas, JoinType.LEFT).get(Empresa_.id)
                        )
                    );
            }
            if (criteria.getFiliaisId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiliaisId(),
                            root -> root.join(Administrador_.filiais, JoinType.LEFT).get(Filial_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
