package br.com.consiga.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.consiga.domain.MensalidadePedido} entity.
 */
public class MensalidadePedidoDTO implements Serializable {

    private Long id;

    private Integer nParcela;

    private Double valor;

    private ZonedDateTime criado;

    private Double valorParcial;

    private PedidoDTO pedido;

    private FaturaMensalDTO fatura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getnParcela() {
        return nParcela;
    }

    public void setnParcela(Integer nParcela) {
        this.nParcela = nParcela;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Double getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(Double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public PedidoDTO getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }

    public FaturaMensalDTO getFatura() {
        return fatura;
    }

    public void setFatura(FaturaMensalDTO fatura) {
        this.fatura = fatura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MensalidadePedidoDTO)) {
            return false;
        }

        MensalidadePedidoDTO mensalidadePedidoDTO = (MensalidadePedidoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mensalidadePedidoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MensalidadePedidoDTO{" +
            "id=" + getId() +
            ", nParcela=" + getnParcela() +
            ", valor=" + getValor() +
            ", criado='" + getCriado() + "'" +
            ", valorParcial=" + getValorParcial() +
            ", pedido=" + getPedido() +
            ", fatura=" + getFatura() +
            "}";
    }
}
