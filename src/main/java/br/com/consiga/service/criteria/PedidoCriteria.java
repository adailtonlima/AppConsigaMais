package br.com.consiga.service.criteria;

import br.com.consiga.domain.enumeration.StatusPedido;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.consiga.domain.Pedido} entity. This class is used
 * in {@link br.com.consiga.web.rest.PedidoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pedidos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PedidoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusPedido
     */
    public static class StatusPedidoFilter extends Filter<StatusPedido> {

        public StatusPedidoFilter() {}

        public StatusPedidoFilter(StatusPedidoFilter filter) {
            super(filter);
        }

        @Override
        public StatusPedidoFilter copy() {
            return new StatusPedidoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatusPedidoFilter estado;

    private ZonedDateTimeFilter criado;

    private ZonedDateTimeFilter dataAprovacao;

    private LocalDateFilter dataExpiracao;

    private DoubleFilter renda;

    private DoubleFilter valorSolicitado;

    private IntegerFilter qtParcelasSolicitado;

    private DoubleFilter valorAprovado;

    private DoubleFilter valorParcelaAprovado;

    private IntegerFilter qtParcelasAprovado;

    private LocalDateFilter dataPrimeiroVencimento;

    private LocalDateFilter dataUltimoVencimento;

    private LongFilter funcionarioId;

    private LongFilter empresaId;

    private LongFilter filiaId;

    private LongFilter quemAprovouId;

    public PedidoCriteria() {}

    public PedidoCriteria(PedidoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.dataAprovacao = other.dataAprovacao == null ? null : other.dataAprovacao.copy();
        this.dataExpiracao = other.dataExpiracao == null ? null : other.dataExpiracao.copy();
        this.renda = other.renda == null ? null : other.renda.copy();
        this.valorSolicitado = other.valorSolicitado == null ? null : other.valorSolicitado.copy();
        this.qtParcelasSolicitado = other.qtParcelasSolicitado == null ? null : other.qtParcelasSolicitado.copy();
        this.valorAprovado = other.valorAprovado == null ? null : other.valorAprovado.copy();
        this.valorParcelaAprovado = other.valorParcelaAprovado == null ? null : other.valorParcelaAprovado.copy();
        this.qtParcelasAprovado = other.qtParcelasAprovado == null ? null : other.qtParcelasAprovado.copy();
        this.dataPrimeiroVencimento = other.dataPrimeiroVencimento == null ? null : other.dataPrimeiroVencimento.copy();
        this.dataUltimoVencimento = other.dataUltimoVencimento == null ? null : other.dataUltimoVencimento.copy();
        this.funcionarioId = other.funcionarioId == null ? null : other.funcionarioId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.filiaId = other.filiaId == null ? null : other.filiaId.copy();
        this.quemAprovouId = other.quemAprovouId == null ? null : other.quemAprovouId.copy();
    }

    @Override
    public PedidoCriteria copy() {
        return new PedidoCriteria(this);
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

    public StatusPedidoFilter getEstado() {
        return estado;
    }

    public StatusPedidoFilter estado() {
        if (estado == null) {
            estado = new StatusPedidoFilter();
        }
        return estado;
    }

    public void setEstado(StatusPedidoFilter estado) {
        this.estado = estado;
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

    public ZonedDateTimeFilter getDataAprovacao() {
        return dataAprovacao;
    }

    public ZonedDateTimeFilter dataAprovacao() {
        if (dataAprovacao == null) {
            dataAprovacao = new ZonedDateTimeFilter();
        }
        return dataAprovacao;
    }

    public void setDataAprovacao(ZonedDateTimeFilter dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public LocalDateFilter getDataExpiracao() {
        return dataExpiracao;
    }

    public LocalDateFilter dataExpiracao() {
        if (dataExpiracao == null) {
            dataExpiracao = new LocalDateFilter();
        }
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateFilter dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public DoubleFilter getRenda() {
        return renda;
    }

    public DoubleFilter renda() {
        if (renda == null) {
            renda = new DoubleFilter();
        }
        return renda;
    }

    public void setRenda(DoubleFilter renda) {
        this.renda = renda;
    }

    public DoubleFilter getValorSolicitado() {
        return valorSolicitado;
    }

    public DoubleFilter valorSolicitado() {
        if (valorSolicitado == null) {
            valorSolicitado = new DoubleFilter();
        }
        return valorSolicitado;
    }

    public void setValorSolicitado(DoubleFilter valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public IntegerFilter getQtParcelasSolicitado() {
        return qtParcelasSolicitado;
    }

    public IntegerFilter qtParcelasSolicitado() {
        if (qtParcelasSolicitado == null) {
            qtParcelasSolicitado = new IntegerFilter();
        }
        return qtParcelasSolicitado;
    }

    public void setQtParcelasSolicitado(IntegerFilter qtParcelasSolicitado) {
        this.qtParcelasSolicitado = qtParcelasSolicitado;
    }

    public DoubleFilter getValorAprovado() {
        return valorAprovado;
    }

    public DoubleFilter valorAprovado() {
        if (valorAprovado == null) {
            valorAprovado = new DoubleFilter();
        }
        return valorAprovado;
    }

    public void setValorAprovado(DoubleFilter valorAprovado) {
        this.valorAprovado = valorAprovado;
    }

    public DoubleFilter getValorParcelaAprovado() {
        return valorParcelaAprovado;
    }

    public DoubleFilter valorParcelaAprovado() {
        if (valorParcelaAprovado == null) {
            valorParcelaAprovado = new DoubleFilter();
        }
        return valorParcelaAprovado;
    }

    public void setValorParcelaAprovado(DoubleFilter valorParcelaAprovado) {
        this.valorParcelaAprovado = valorParcelaAprovado;
    }

    public IntegerFilter getQtParcelasAprovado() {
        return qtParcelasAprovado;
    }

    public IntegerFilter qtParcelasAprovado() {
        if (qtParcelasAprovado == null) {
            qtParcelasAprovado = new IntegerFilter();
        }
        return qtParcelasAprovado;
    }

    public void setQtParcelasAprovado(IntegerFilter qtParcelasAprovado) {
        this.qtParcelasAprovado = qtParcelasAprovado;
    }

    public LocalDateFilter getDataPrimeiroVencimento() {
        return dataPrimeiroVencimento;
    }

    public LocalDateFilter dataPrimeiroVencimento() {
        if (dataPrimeiroVencimento == null) {
            dataPrimeiroVencimento = new LocalDateFilter();
        }
        return dataPrimeiroVencimento;
    }

    public void setDataPrimeiroVencimento(LocalDateFilter dataPrimeiroVencimento) {
        this.dataPrimeiroVencimento = dataPrimeiroVencimento;
    }

    public LocalDateFilter getDataUltimoVencimento() {
        return dataUltimoVencimento;
    }

    public LocalDateFilter dataUltimoVencimento() {
        if (dataUltimoVencimento == null) {
            dataUltimoVencimento = new LocalDateFilter();
        }
        return dataUltimoVencimento;
    }

    public void setDataUltimoVencimento(LocalDateFilter dataUltimoVencimento) {
        this.dataUltimoVencimento = dataUltimoVencimento;
    }

    public LongFilter getFuncionarioId() {
        return funcionarioId;
    }

    public LongFilter funcionarioId() {
        if (funcionarioId == null) {
            funcionarioId = new LongFilter();
        }
        return funcionarioId;
    }

    public void setFuncionarioId(LongFilter funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public LongFilter getEmpresaId() {
        return empresaId;
    }

    public LongFilter empresaId() {
        if (empresaId == null) {
            empresaId = new LongFilter();
        }
        return empresaId;
    }

    public void setEmpresaId(LongFilter empresaId) {
        this.empresaId = empresaId;
    }

    public LongFilter getFiliaId() {
        return filiaId;
    }

    public LongFilter filiaId() {
        if (filiaId == null) {
            filiaId = new LongFilter();
        }
        return filiaId;
    }

    public void setFiliaId(LongFilter filiaId) {
        this.filiaId = filiaId;
    }

    public LongFilter getQuemAprovouId() {
        return quemAprovouId;
    }

    public LongFilter quemAprovouId() {
        if (quemAprovouId == null) {
            quemAprovouId = new LongFilter();
        }
        return quemAprovouId;
    }

    public void setQuemAprovouId(LongFilter quemAprovouId) {
        this.quemAprovouId = quemAprovouId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PedidoCriteria that = (PedidoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(dataAprovacao, that.dataAprovacao) &&
            Objects.equals(dataExpiracao, that.dataExpiracao) &&
            Objects.equals(renda, that.renda) &&
            Objects.equals(valorSolicitado, that.valorSolicitado) &&
            Objects.equals(qtParcelasSolicitado, that.qtParcelasSolicitado) &&
            Objects.equals(valorAprovado, that.valorAprovado) &&
            Objects.equals(valorParcelaAprovado, that.valorParcelaAprovado) &&
            Objects.equals(qtParcelasAprovado, that.qtParcelasAprovado) &&
            Objects.equals(dataPrimeiroVencimento, that.dataPrimeiroVencimento) &&
            Objects.equals(dataUltimoVencimento, that.dataUltimoVencimento) &&
            Objects.equals(funcionarioId, that.funcionarioId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(filiaId, that.filiaId) &&
            Objects.equals(quemAprovouId, that.quemAprovouId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            estado,
            criado,
            dataAprovacao,
            dataExpiracao,
            renda,
            valorSolicitado,
            qtParcelasSolicitado,
            valorAprovado,
            valorParcelaAprovado,
            qtParcelasAprovado,
            dataPrimeiroVencimento,
            dataUltimoVencimento,
            funcionarioId,
            empresaId,
            filiaId,
            quemAprovouId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (dataAprovacao != null ? "dataAprovacao=" + dataAprovacao + ", " : "") +
            (dataExpiracao != null ? "dataExpiracao=" + dataExpiracao + ", " : "") +
            (renda != null ? "renda=" + renda + ", " : "") +
            (valorSolicitado != null ? "valorSolicitado=" + valorSolicitado + ", " : "") +
            (qtParcelasSolicitado != null ? "qtParcelasSolicitado=" + qtParcelasSolicitado + ", " : "") +
            (valorAprovado != null ? "valorAprovado=" + valorAprovado + ", " : "") +
            (valorParcelaAprovado != null ? "valorParcelaAprovado=" + valorParcelaAprovado + ", " : "") +
            (qtParcelasAprovado != null ? "qtParcelasAprovado=" + qtParcelasAprovado + ", " : "") +
            (dataPrimeiroVencimento != null ? "dataPrimeiroVencimento=" + dataPrimeiroVencimento + ", " : "") +
            (dataUltimoVencimento != null ? "dataUltimoVencimento=" + dataUltimoVencimento + ", " : "") +
            (funcionarioId != null ? "funcionarioId=" + funcionarioId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (filiaId != null ? "filiaId=" + filiaId + ", " : "") +
            (quemAprovouId != null ? "quemAprovouId=" + quemAprovouId + ", " : "") +
            "}";
    }
}
