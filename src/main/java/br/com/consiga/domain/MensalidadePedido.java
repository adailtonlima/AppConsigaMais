package br.com.consiga.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MensalidadePedido.
 */
@Entity
@Table(name = "mensalidade_pedido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MensalidadePedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "n_parcela")
    private Integer nParcela;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @Column(name = "valor_parcial")
    private Double valorParcial;

    @ManyToOne
    @JsonIgnoreProperties(value = { "funcionario", "empresa", "filia", "quemAprovou" }, allowSetters = true)
    private Pedido pedido;

    @ManyToOne
    @JsonIgnoreProperties(value = { "empresa", "filial" }, allowSetters = true)
    private FaturaMensal fatura;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MensalidadePedido id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getnParcela() {
        return this.nParcela;
    }

    public MensalidadePedido nParcela(Integer nParcela) {
        this.nParcela = nParcela;
        return this;
    }

    public void setnParcela(Integer nParcela) {
        this.nParcela = nParcela;
    }

    public Double getValor() {
        return this.valor;
    }

    public MensalidadePedido valor(Double valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public MensalidadePedido criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Double getValorParcial() {
        return this.valorParcial;
    }

    public MensalidadePedido valorParcial(Double valorParcial) {
        this.valorParcial = valorParcial;
        return this;
    }

    public void setValorParcial(Double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public MensalidadePedido pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public FaturaMensal getFatura() {
        return this.fatura;
    }

    public MensalidadePedido fatura(FaturaMensal faturaMensal) {
        this.setFatura(faturaMensal);
        return this;
    }

    public void setFatura(FaturaMensal faturaMensal) {
        this.fatura = faturaMensal;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MensalidadePedido)) {
            return false;
        }
        return id != null && id.equals(((MensalidadePedido) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MensalidadePedido{" +
            "id=" + getId() +
            ", nParcela=" + getnParcela() +
            ", valor=" + getValor() +
            ", criado='" + getCriado() + "'" +
            ", valorParcial=" + getValorParcial() +
            "}";
    }
}
