package br.com.consiga.domain;

import br.com.consiga.domain.enumeration.StatusArquivo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArquivoImportacao.
 */
@Entity
@Table(name = "arquivo_importacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArquivoImportacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "url_arquivo")
    private String urlArquivo;

    @Column(name = "url_criticas")
    private String urlCriticas;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private StatusArquivo estado;

    @Column(name = "processado")
    private ZonedDateTime processado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "empresas", "filiais" }, allowSetters = true)
    private Administrador criador;

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

    public ArquivoImportacao id(Long id) {
        this.id = id;
        return this;
    }

    public String getUrlArquivo() {
        return this.urlArquivo;
    }

    public ArquivoImportacao urlArquivo(String urlArquivo) {
        this.urlArquivo = urlArquivo;
        return this;
    }

    public void setUrlArquivo(String urlArquivo) {
        this.urlArquivo = urlArquivo;
    }

    public String getUrlCriticas() {
        return this.urlCriticas;
    }

    public ArquivoImportacao urlCriticas(String urlCriticas) {
        this.urlCriticas = urlCriticas;
        return this;
    }

    public void setUrlCriticas(String urlCriticas) {
        this.urlCriticas = urlCriticas;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public ArquivoImportacao criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public StatusArquivo getEstado() {
        return this.estado;
    }

    public ArquivoImportacao estado(StatusArquivo estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(StatusArquivo estado) {
        this.estado = estado;
    }

    public ZonedDateTime getProcessado() {
        return this.processado;
    }

    public ArquivoImportacao processado(ZonedDateTime processado) {
        this.processado = processado;
        return this;
    }

    public void setProcessado(ZonedDateTime processado) {
        this.processado = processado;
    }

    public Administrador getCriador() {
        return this.criador;
    }

    public ArquivoImportacao criador(Administrador administrador) {
        this.setCriador(administrador);
        return this;
    }

    public void setCriador(Administrador administrador) {
        this.criador = administrador;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public ArquivoImportacao empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public ArquivoImportacao filial(Filial filial) {
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
        if (!(o instanceof ArquivoImportacao)) {
            return false;
        }
        return id != null && id.equals(((ArquivoImportacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoImportacao{" +
            "id=" + getId() +
            ", urlArquivo='" + getUrlArquivo() + "'" +
            ", urlCriticas='" + getUrlCriticas() + "'" +
            ", criado='" + getCriado() + "'" +
            ", estado='" + getEstado() + "'" +
            ", processado='" + getProcessado() + "'" +
            "}";
    }
}
