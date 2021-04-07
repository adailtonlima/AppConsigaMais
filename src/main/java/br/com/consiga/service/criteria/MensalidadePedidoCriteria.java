package br.com.consiga.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.consiga.domain.MensalidadePedido} entity. This class is used
 * in {@link br.com.consiga.web.rest.MensalidadePedidoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mensalidade-pedidos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MensalidadePedidoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter nParcela;

    private DoubleFilter valor;

    private ZonedDateTimeFilter criado;

    private DoubleFilter valorParcial;

    private LongFilter pedidoId;

    private LongFilter faturaId;

    public MensalidadePedidoCriteria() {}

    public MensalidadePedidoCriteria(MensalidadePedidoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nParcela = other.nParcela == null ? null : other.nParcela.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.valorParcial = other.valorParcial == null ? null : other.valorParcial.copy();
        this.pedidoId = other.pedidoId == null ? null : other.pedidoId.copy();
        this.faturaId = other.faturaId == null ? null : other.faturaId.copy();
    }

    @Override
    public MensalidadePedidoCriteria copy() {
        return new MensalidadePedidoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getnParcela() {
        return nParcela;
    }

    public IntegerFilter nParcela() {
        if (nParcela == null) {
            nParcela = new IntegerFilter();
        }
        return nParcela;
    }

    public void setnParcela(IntegerFilter nParcela) {
        this.nParcela = nParcela;
    }

    public DoubleFilter getValor() {
        return valor;
    }

    public DoubleFilter valor() {
        if (valor == null) {
            valor = new DoubleFilter();
        }
        return valor;
    }

    public void setValor(DoubleFilter valor) {
        this.valor = valor;
    }

    public ZonedDateTimeFilter getCriado() {
        return criado;
    }

    public ZonedDateTimeFilter criado() {
        if (criado == null) {
            criado = new ZonedDateTimeFilter();
        }
        return criado;
    }

    public void setCriado(ZonedDateTimeFilter criado) {
        this.criado = criado;
    }

    public DoubleFilter getValorParcial() {
        return valorParcial;
    }

    public DoubleFilter valorParcial() {
        if (valorParcial == null) {
            valorParcial = new DoubleFilter();
        }
        return valorParcial;
    }

    public void setValorParcial(DoubleFilter valorParcial) {
        this.valorParcial = valorParcial;
    }

    public LongFilter getPedidoId() {
        return pedidoId;
    }

    public LongFilter pedidoId() {
        if (pedidoId == null) {
            pedidoId = new LongFilter();
        }
        return pedidoId;
    }

    public void setPedidoId(LongFilter pedidoId) {
        this.pedidoId = pedidoId;
    }

    public LongFilter getFaturaId() {
        return faturaId;
    }

    public LongFilter faturaId() {
        if (faturaId == null) {
            faturaId = new LongFilter();
        }
        return faturaId;
    }

    public void setFaturaId(LongFilter faturaId) {
        this.faturaId = faturaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MensalidadePedidoCriteria that = (MensalidadePedidoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nParcela, that.nParcela) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(valorParcial, that.valorParcial) &&
            Objects.equals(pedidoId, that.pedidoId) &&
            Objects.equals(faturaId, that.faturaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nParcela, valor, criado, valorParcial, pedidoId, faturaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MensalidadePedidoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nParcela != null ? "nParcela=" + nParcela + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (valorParcial != null ? "valorParcial=" + valorParcial + ", " : "") +
            (pedidoId != null ? "pedidoId=" + pedidoId + ", " : "") +
            (faturaId != null ? "faturaId=" + faturaId + ", " : "") +
            "}";
    }
}
