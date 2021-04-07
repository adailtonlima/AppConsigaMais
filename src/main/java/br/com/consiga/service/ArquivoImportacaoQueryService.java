package br.com.consiga.service;

import br.com.consiga.domain.*; // for static metamodels
import br.com.consiga.domain.ArquivoImportacao;
import br.com.consiga.repository.ArquivoImportacaoRepository;
import br.com.consiga.service.criteria.ArquivoImportacaoCriteria;
import br.com.consiga.service.dto.ArquivoImportacaoDTO;
import br.com.consiga.service.mapper.ArquivoImportacaoMapper;
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
 * Service for executing complex queries for {@link ArquivoImportacao} entities in the database.
 * The main input is a {@link ArquivoImportacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArquivoImportacaoDTO} or a {@link Page} of {@link ArquivoImportacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArquivoImportacaoQueryService extends QueryService<ArquivoImportacao> {

    private final Logger log = LoggerFactory.getLogger(ArquivoImportacaoQueryService.class);

    private final ArquivoImportacaoRepository arquivoImportacaoRepository;

    private final ArquivoImportacaoMapper arquivoImportacaoMapper;

    public ArquivoImportacaoQueryService(
        ArquivoImportacaoRepository arquivoImportacaoRepository,
        ArquivoImportacaoMapper arquivoImportacaoMapper
    ) {
        this.arquivoImportacaoRepository = arquivoImportacaoRepository;
        this.arquivoImportacaoMapper = arquivoImportacaoMapper;
    }

    /**
     * Return a {@link List} of {@link ArquivoImportacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArquivoImportacaoDTO> findByCriteria(ArquivoImportacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ArquivoImportacao> specification = createSpecification(criteria);
        return arquivoImportacaoMapper.toDto(arquivoImportacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArquivoImportacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArquivoImportacaoDTO> findByCriteria(ArquivoImportacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArquivoImportacao> specification = createSpecification(criteria);
        return arquivoImportacaoRepository.findAll(specification, page).map(arquivoImportacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArquivoImportacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ArquivoImportacao> specification = createSpecification(criteria);
        return arquivoImportacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link ArquivoImportacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ArquivoImportacao> createSpecification(ArquivoImportacaoCriteria criteria) {
        Specification<ArquivoImportacao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ArquivoImportacao_.id));
            }
            if (criteria.getUrlArquivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlArquivo(), ArquivoImportacao_.urlArquivo));
            }
            if (criteria.getUrlCriticas() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlCriticas(), ArquivoImportacao_.urlCriticas));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), ArquivoImportacao_.criado));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), ArquivoImportacao_.estado));
            }
            if (criteria.getProcessado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessado(), ArquivoImportacao_.processado));
            }
            if (criteria.getCriadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCriadorId(),
                            root -> root.join(ArquivoImportacao_.criador, JoinType.LEFT).get(Administrador_.id)
                        )
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmpresaId(),
                            root -> root.join(ArquivoImportacao_.empresa, JoinType.LEFT).get(Empresa_.id)
                        )
                    );
            }
            if (criteria.getFilialId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFilialId(),
                            root -> root.join(ArquivoImportacao_.filial, JoinType.LEFT).get(Filial_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
