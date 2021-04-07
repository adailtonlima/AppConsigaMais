package br.com.consiga.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Funcionario.
 */
@Entity
@Table(name = "funcionario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @Column(name = "atualizado")
    private ZonedDateTime atualizado;

    @Column(name = "ultimo_login")
    private ZonedDateTime ultimoLogin;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Funcionario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Funcionario cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Funcionario criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public ZonedDateTime getAtualizado() {
        return this.atualizado;
    }

    public Funcionario atualizado(ZonedDateTime atualizado) {
        this.atualizado = atualizado;
        return this;
    }

    public void setAtualizado(ZonedDateTime atualizado) {
        this.atualizado = atualizado;
    }

    public ZonedDateTime getUltimoLogin() {
        return this.ultimoLogin;
    }

    public Funcionario ultimoLogin(ZonedDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
        return this;
    }

    public void setUltimoLogin(ZonedDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Funcionario)) {
            return false;
        }
        return id != null && id.equals(((Funcionario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Funcionario{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", criado='" + getCriado() + "'" +
            ", atualizado='" + getAtualizado() + "'" +
            ", ultimoLogin='" + getUltimoLogin() + "'" +
            "}";
    }
}
