package br.com.consiga.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FaturaMensal.
 */
@Entity
@Table(name = "fatura_mensal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FaturaMensal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "mes")
    private LocalDate mes;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @Column(name = "boleto_url")
    private String boletoUrl;

    @Column(name = "data_pago")
    private ZonedDateTime dataPago;

    @ManyToOne
    @JsonIgnoreProperties(value = { "administradores" }, allowSetters = true)
    private Empresa empresa;

    @ManyToOne
    @JsonIgnoreProperties(value = { "empresa", "administradores" }, allowSetters = true)
    private Filial filial;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FaturaMensal id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getMes() {
        return this.mes;
    }

    public FaturaMensal mes(LocalDate mes) {
        this.mes = mes;
        return this;
    }

    public void setMes(LocalDate mes) {
        this.mes = mes;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public FaturaMensal criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public String getBoletoUrl() {
        return this.boletoUrl;
    }

    public FaturaMensal boletoUrl(String boletoUrl) {
        this.boletoUrl = boletoUrl;
        return this;
    }

    public void setBoletoUrl(String boletoUrl) {
        this.boletoUrl = boletoUrl;
    }

    public ZonedDateTime getDataPago() {
        return this.dataPago;
    }

    public FaturaMensal dataPago(ZonedDateTime dataPago) {
        this.dataPago = dataPago;
        return this;
    }

    public void setDataPago(ZonedDateTime dataPago) {
        this.dataPago = dataPago;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public FaturaMensal empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public FaturaMensal filial(Filial filial) {
        this.setFilial(filial);
        return this;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FaturaMensal)) {
            return false;
        }
        return id != null && id.equals(((FaturaMensal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FaturaMensal{" +
            "id=" + getId() +
            ", mes='" + getMes() + "'" +
            ", criado='" + getCriado() + "'" +
            ", boletoUrl='" + getBoletoUrl() + "'" +
            ", dataPago='" + getDataPago() + "'" +
            "}";
    }
}
