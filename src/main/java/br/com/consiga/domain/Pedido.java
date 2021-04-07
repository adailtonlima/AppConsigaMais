package br.com.consiga.domain;

import br.com.consiga.domain.enumeration.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private StatusPedido estado;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @Column(name = "data_aprovacao")
    private ZonedDateTime dataAprovacao;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @Column(name = "renda")
    private Double renda;

    @Column(name = "valor_solicitado")
    private Double valorSolicitado;

    @Column(name = "qt_parcelas_solicitado")
    private Integer qtParcelasSolicitado;

    @Column(name = "valor_aprovado")
    private Double valorAprovado;

    @Column(name = "valor_parcela_aprovado")
    private Double valorParcelaAprovado;

    @Column(name = "qt_parcelas_aprovado")
    private Integer qtParcelasAprovado;

    @Column(name = "data_primeiro_vencimento")
    private LocalDate dataPrimeiroVencimento;

    @Column(name = "data_ultimo_vencimento")
    private LocalDate dataUltimoVencimento;

    @ManyToOne
    private Funcionario funcionario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "administradores" }, allowSetters = true)
    private Empresa empresa;

    @ManyToOne
    @JsonIgnoreProperties(value = { "empresa", "administradores" }, allowSetters = true)
    private Filial filia;

    @ManyToOne
    @JsonIgnoreProperties(value = { "empresas", "filiais" }, allowSetters = true)
    private Administrador quemAprovou;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido id(Long id) {
        this.id = id;
        return this;
    }

    public StatusPedido getEstado() {
        return this.estado;
    }

    public Pedido estado(StatusPedido estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(StatusPedido estado) {
        this.estado = estado;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Pedido criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public ZonedDateTime getDataAprovacao() {
        return this.dataAprovacao;
    }

    public Pedido dataAprovacao(ZonedDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
        return this;
    }

    public void setDataAprovacao(ZonedDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public LocalDate getDataExpiracao() {
        return this.dataExpiracao;
    }

    public Pedido dataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
        return this;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Double getRenda() {
        return this.renda;
    }

    public Pedido renda(Double renda) {
        this.renda = renda;
        return this;
    }

    public void setRenda(Double renda) {
        this.renda = renda;
    }

    public Double getValorSolicitado() {
        return this.valorSolicitado;
    }

    public Pedido valorSolicitado(Double valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
        return this;
    }

    public void setValorSolicitado(Double valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public Integer getQtParcelasSolicitado() {
        return this.qtParcelasSolicitado;
    }

    public Pedido qtParcelasSolicitado(Integer qtParcelasSolicitado) {
        this.qtParcelasSolicitado = qtParcelasSolicitado;
        return this;
    }

    public void setQtParcelasSolicitado(Integer qtParcelasSolicitado) {
        this.qtParcelasSolicitado = qtParcelasSolicitado;
    }

    public Double getValorAprovado() {
        return this.valorAprovado;
    }

    public Pedido valorAprovado(Double valorAprovado) {
        this.valorAprovado = valorAprovado;
        return this;
    }

    public void setValorAprovado(Double valorAprovado) {
        this.valorAprovado = valorAprovado;
    }

    public Double getValorParcelaAprovado() {
        return this.valorParcelaAprovado;
    }

    public Pedido valorParcelaAprovado(Double valorParcelaAprovado) {
        this.valorParcelaAprovado = valorParcelaAprovado;
        return this;
    }

    public void setValorParcelaAprovado(Double valorParcelaAprovado) {
        this.valorParcelaAprovado = valorParcelaAprovado;
    }

    public Integer getQtParcelasAprovado() {
        return this.qtParcelasAprovado;
    }

    public Pedido qtParcelasAprovado(Integer qtParcelasAprovado) {
        this.qtParcelasAprovado = qtParcelasAprovado;
        return this;
    }

    public void setQtParcelasAprovado(Integer qtParcelasAprovado) {
        this.qtParcelasAprovado = qtParcelasAprovado;
    }

    public LocalDate getDataPrimeiroVencimento() {
        return this.dataPrimeiroVencimento;
    }

    public Pedido dataPrimeiroVencimento(LocalDate dataPrimeiroVencimento) {
        this.dataPrimeiroVencimento = dataPrimeiroVencimento;
        return this;
    }

    public void setDataPrimeiroVencimento(LocalDate dataPrimeiroVencimento) {
        this.dataPrimeiroVencimento = dataPrimeiroVencimento;
    }

    public LocalDate getDataUltimoVencimento() {
        return this.dataUltimoVencimento;
    }

    public Pedido dataUltimoVencimento(LocalDate dataUltimoVencimento) {
        this.dataUltimoVencimento = dataUltimoVencimento;
        return this;
    }

    public void setDataUltimoVencimento(LocalDate dataUltimoVencimento) {
        this.dataUltimoVencimento = dataUltimoVencimento;
    }

    public Funcionario getFuncionario() {
        return this.funcionario;
    }

    public Pedido funcionario(Funcionario funcionario) {
        this.setFuncionario(funcionario);
        return this;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public Pedido empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Filial getFilia() {
        return this.filia;
    }

    public Pedido filia(Filial filial) {
        this.setFilia(filial);
        return this;
    }

    public void setFilia(Filial filial) {
        this.filia = filial;
    }

    public Administrador getQuemAprovou() {
        return this.quemAprovou;
    }

    public Pedido quemAprovou(Administrador administrador) {
        this.setQuemAprovou(administrador);
        return this;
    }

    public void setQuemAprovou(Administrador administrador) {
        this.quemAprovou = administrador;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido)) {
            return false;
        }
        return id != null && id.equals(((Pedido) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", criado='" + getCriado() + "'" +
            ", dataAprovacao='" + getDataAprovacao() + "'" +
            ", dataExpiracao='" + getDataExpiracao() + "'" +
            ", renda=" + getRenda() +
            ", valorSolicitado=" + getValorSolicitado() +
            ", qtParcelasSolicitado=" + getQtParcelasSolicitado() +
            ", valorAprovado=" + getValorAprovado() +
            ", valorParcelaAprovado=" + getValorParcelaAprovado() +
            ", qtParcelasAprovado=" + getQtParcelasAprovado() +
            ", dataPrimeiroVencimento='" + getDataPrimeiroVencimento() + "'" +
            ", dataUltimoVencimento='" + getDataUltimoVencimento() + "'" +
            "}";
    }
}
