package br.com.consiga.service;

import br.com.consiga.domain.*; // for static metamodels
import br.com.consiga.domain.Pedido;
import br.com.consiga.repository.PedidoRepository;
import br.com.consiga.service.criteria.PedidoCriteria;
import br.com.consiga.service.dto.PedidoDTO;
import br.com.consiga.service.mapper.PedidoMapper;
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
 * Service for executing complex queries for {@link Pedido} entities in the database.
 * The main input is a {@link PedidoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PedidoDTO} or a {@link Page} of {@link PedidoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PedidoQueryService extends QueryService<Pedido> {

    private final Logger log = LoggerFactory.getLogger(PedidoQueryService.class);

    private final PedidoRepository pedidoRepository;

    private final PedidoMapper pedidoMapper;

    public PedidoQueryService(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    /**
     * Return a {@link List} of {@link PedidoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PedidoDTO> findByCriteria(PedidoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoMapper.toDto(pedidoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PedidoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PedidoDTO> findByCriteria(PedidoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.findAll(specification, page).map(pedidoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PedidoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.count(specification);
    }

    /**
     * Function to convert {@link PedidoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pedido> createSpecification(PedidoCriteria criteria) {
        Specification<Pedido> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pedido_.id));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Pedido_.estado));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Pedido_.criado));
            }
            if (criteria.getDataAprovacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataAprovacao(), Pedido_.dataAprovacao));
            }
            if (criteria.getDataExpiracao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataExpiracao(), Pedido_.dataExpiracao));
            }
            if (criteria.getRenda() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRenda(), Pedido_.renda));
            }
            if (criteria.getValorSolicitado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorSolicitado(), Pedido_.valorSolicitado));
            }
            if (criteria.getQtParcelasSolicitado() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getQtParcelasSolicitado(), Pedido_.qtParcelasSolicitado));
            }
            if (criteria.getValorAprovado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorAprovado(), Pedido_.valorAprovado));
            }
            if (criteria.getValorParcelaAprovado() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getValorParcelaAprovado(), Pedido_.valorParcelaAprovado));
            }
            if (criteria.getQtParcelasAprovado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQtParcelasAprovado(), Pedido_.qtParcelasAprovado));
            }
            if (criteria.getDataPrimeiroVencimento() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDataPrimeiroVencimento(), Pedido_.dataPrimeiroVencimento));
            }
            if (criteria.getDataUltimoVencimento() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDataUltimoVencimento(), Pedido_.dataUltimoVencimento));
            }
            if (criteria.getFuncionarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionarioId(),
                            root -> root.join(Pedido_.funcionario, JoinType.LEFT).get(Funcionario_.id)
                        )
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(Pedido_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
            if (criteria.getFiliaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFiliaId(), root -> root.join(Pedido_.filia, JoinType.LEFT).get(Filial_.id))
                    );
            }
            if (criteria.getQuemAprovouId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getQuemAprovouId(),
                            root -> root.join(Pedido_.quemAprovou, JoinType.LEFT).get(Administrador_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
