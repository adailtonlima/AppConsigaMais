package br.com.consiga.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Administrador.
 */
@Entity
@Table(name = "administrador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Administrador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @Column(name = "atualizado")
    private ZonedDateTime atualizado;

    @Column(name = "ultimo_login")
    private ZonedDateTime ultimoLogin;

    @ManyToMany(mappedBy = "administradores")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "administradores" }, allowSetters = true)
    private Set<Empresa> empresas = new HashSet<>();

    @ManyToMany(mappedBy = "administradores")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "empresa", "administradores" }, allowSetters = true)
    private Set<Filial> filiais = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Administrador id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Administrador nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Administrador criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public ZonedDateTime getAtualizado() {
        return this.atualizado;
    }

    public Administrador atualizado(ZonedDateTime atualizado) {
        this.atualizado = atualizado;
        return this;
    }

    public void setAtualizado(ZonedDateTime atualizado) {
        this.atualizado = atualizado;
    }

    public ZonedDateTime getUltimoLogin() {
        return this.ultimoLogin;
    }

    public Administrador ultimoLogin(ZonedDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
        return this;
    }

    public void setUltimoLogin(ZonedDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public Set<Empresa> getEmpresas() {
        return this.empresas;
    }

    public Administrador empresas(Set<Empresa> empresas) {
        this.setEmpresas(empresas);
        return this;
    }

    public Administrador addEmpresas(Empresa empresa) {
        this.empresas.add(empresa);
        empresa.getAdministradores().add(this);
        return this;
    }

    public Administrador removeEmpresas(Empresa empresa) {
        this.empresas.remove(empresa);
        empresa.getAdministradores().remove(this);
        return this;
    }

    public void setEmpresas(Set<Empresa> empresas) {
        if (this.empresas != null) {
            this.empresas.forEach(i -> i.removeAdministradores(this));
        }
        if (empresas != null) {
            empresas.forEach(i -> i.addAdministradores(this));
        }
        this.empresas = empresas;
    }

    public Set<Filial> getFiliais() {
        return this.filiais;
    }

    public Administrador filiais(Set<Filial> filials) {
        this.setFiliais(filials);
        return this;
    }

    public Administrador addFiliais(Filial filial) {
        this.filiais.add(filial);
        filial.getAdministradores().add(this);
        return this;
    }

    public Administrador removeFiliais(Filial filial) {
        this.filiais.remove(filial);
        filial.getAdministradores().remove(this);
        return this;
    }

    public void setFiliais(Set<Filial> filials) {
        if (this.filiais != null) {
            this.filiais.forEach(i -> i.removeAdministradores(this));
        }
        if (filials != null) {
            filials.forEach(i -> i.addAdministradores(this));
        }
        this.filiais = filials;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Administrador)) {
            return false;
        }
        return id != null && id.equals(((Administrador) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Administrador{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criado='" + getCriado() + "'" +
            ", atualizado='" + getAtualizado() + "'" +
            ", ultimoLogin='" + getUltimoLogin() + "'" +
            "}";
    }
}
