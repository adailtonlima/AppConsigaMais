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
 * Criteria class for the {@link br.com.consiga.domain.Funcionario} entity. This class is used
 * in {@link br.com.consiga.web.rest.FuncionarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /funcionarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FuncionarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cpf;

    private ZonedDateTimeFilter criado;

    private ZonedDateTimeFilter atualizado;

    private ZonedDateTimeFilter ultimoLogin;

    public FuncionarioCriteria() {}

    public FuncionarioCriteria(FuncionarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.atualizado = other.atualizado == null ? null : other.atualizado.copy();
        this.ultimoLogin = other.ultimoLogin == null ? null : other.ultimoLogin.copy();
    }

    @Override
    public FuncionarioCriteria copy() {
        return new FuncionarioCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
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

    public ZonedDateTimeFilter getAtualizado() {
        return atualizado;
    }

    public ZonedDateTimeFilter atualizado() {
        if (atualizado == null) {
            atualizado = new ZonedDateTimeFilter();
        }
        return atualizado;
    }

    public void setAtualizado(ZonedDateTimeFilter atualizado) {
        this.atualizado = atualizado;
    }

    public ZonedDateTimeFilter getUltimoLogin() {
        return ultimoLogin;
    }

    public ZonedDateTimeFilter ultimoLogin() {
        if (ultimoLogin == null) {
            ultimoLogin = new ZonedDateTimeFilter();
        }
        return ultimoLogin;
    }

    public void setUltimoLogin(ZonedDateTimeFilter ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FuncionarioCriteria that = (FuncionarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(atualizado, that.atualizado) &&
            Objects.equals(ultimoLogin, that.ultimoLogin)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cpf, criado, atualizado, ultimoLogin);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncionarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (atualizado != null ? "atualizado=" + atualizado + ", " : "") +
            (ultimoLogin != null ? "ultimoLogin=" + ultimoLogin + ", " : "") +
            "}";
    }
}
