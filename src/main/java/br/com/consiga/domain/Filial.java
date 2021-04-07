package br.com.consiga.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Filial.
 */
@Entity
@Table(name = "filial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Filial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "cnpj")
    private String cnpj;

    @ManyToOne
    @JsonIgnoreProperties(value = { "administradores" }, allowSetters = true)
    private Empresa empresa;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_filial__administradores",
        joinColumns = @JoinColumn(name = "filial_id"),
        inverseJoinColumns = @JoinColumn(name = "administradores_id")
    )
    @JsonIgnoreProperties(value = { "empresas", "filiais" }, allowSetters = true)
    private Set<Administrador> administradores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Filial id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Filial nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Filial codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Filial cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public Filial empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Set<Administrador> getAdministradores() {
        return this.administradores;
    }

    public Filial administradores(Set<Administrador> administradors) {
        this.setAdministradores(administradors);
        return this;
    }

    public Filial addAdministradores(Administrador administrador) {
        this.administradores.add(administrador);
        administrador.getFiliais().add(this);
        return this;
    }

    public Filial removeAdministradores(Administrador administrador) {
        this.administradores.remove(administrador);
        administrador.getFiliais().remove(this);
        return this;
    }

    public void setAdministradores(Set<Administrador> administradors) {
        this.administradores = administradors;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Filial)) {
            return false;
        }
        return id != null && id.equals(((Filial) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Filial{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            "}";
    }
}
