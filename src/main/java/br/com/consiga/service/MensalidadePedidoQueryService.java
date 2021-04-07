package br.com.consiga.service;

import br.com.consiga.domain.*; // for static metamodels
import br.com.consiga.domain.MensalidadePedido;
import br.com.consiga.repository.MensalidadePedidoRepository;
import br.com.consiga.service.criteria.MensalidadePedidoCriteria;
import br.com.consiga.service.dto.MensalidadePedidoDTO;
import br.com.consiga.service.mapper.MensalidadePedidoMapper;
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
 * Service for executing complex queries for {@link MensalidadePedido} entities in the database.
 * The main input is a {@link MensalidadePedidoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MensalidadePedidoDTO} or a {@link Page} of {@link MensalidadePedidoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MensalidadePedidoQueryService extends QueryService<MensalidadePedido> {

    private final Logger log = LoggerFactory.getLogger(MensalidadePedidoQueryService.class);

    private final MensalidadePedidoRepository mensalidadePedidoRepository;

    private final MensalidadePedidoMapper mensalidadePedidoMapper;

    public MensalidadePedidoQueryService(
        MensalidadePedidoRepository mensalidadePedidoRepository,
        MensalidadePedidoMapper mensalidadePedidoMapper
    ) {
        this.mensalidadePedidoRepository = mensalidadePedidoRepository;
        this.mensalidadePedidoMapper = mensalidadePedidoMapper;
    }

    /**
     * Return a {@link List} of {@link MensalidadePedidoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MensalidadePedidoDTO> findByCriteria(MensalidadePedidoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MensalidadePedido> specification = createSpecification(criteria);
        return mensalidadePedidoMapper.toDto(mensalidadePedidoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MensalidadePedidoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MensalidadePedidoDTO> findByCriteria(MensalidadePedidoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MensalidadePedido> specification = createSpecification(criteria);
        return mensalidadePedidoRepository.findAll(specification, page).map(mensalidadePedidoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MensalidadePedidoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MensalidadePedido> specification = createSpecification(criteria);
        return mensalidadePedidoRepository.count(specification);
    }

    /**
     * Function to convert {@link MensalidadePedidoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MensalidadePedido> createSpecification(MensalidadePedidoCriteria criteria) {
        Specification<MensalidadePedido> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MensalidadePedido_.id));
            }
            if (criteria.getnParcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getnParcela(), MensalidadePedido_.nParcela));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), MensalidadePedido_.valor));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), MensalidadePedido_.criado));
            }
            if (criteria.getValorParcial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorParcial(), MensalidadePedido_.valorParcial));
            }
            if (criteria.getPedidoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPedidoId(),
                            root -> root.join(MensalidadePedido_.pedido, JoinType.LEFT).get(Pedido_.id)
                        )
                    );
            }
            if (criteria.getFaturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFaturaId(),
                            root -> root.join(MensalidadePedido_.fatura, JoinType.LEFT).get(FaturaMensal_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
