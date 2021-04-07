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
 * Criteria class for the {@link br.com.consiga.domain.Administrador} entity. This class is used
 * in {@link br.com.consiga.web.rest.AdministradorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /administradors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AdministradorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private ZonedDateTimeFilter criado;

    private ZonedDateTimeFilter atualizado;

    private ZonedDateTimeFilter ultimoLogin;

    private LongFilter empresasId;

    private LongFilter filiaisId;

    public AdministradorCriteria() {}

    public AdministradorCriteria(AdministradorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.atualizado = other.atualizado == null ? null : other.atualizado.copy();
        this.ultimoLogin = other.ultimoLogin == null ? null : other.ultimoLogin.copy();
        this.empresasId = other.empresasId == null ? null : other.empresasId.copy();
        this.filiaisId = other.filiaisId == null ? null : other.filiaisId.copy();
    }

    @Override
    public AdministradorCriteria copy() {
        return new AdministradorCriteria(this);
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

    public LongFilter getEmpresasId() {
        return empresasId;
    }

    public LongFilter empresasId() {
        if (empresasId == null) {
            empresasId = new LongFilter();
        }
        return empresasId;
    }

    public void setEmpresasId(LongFilter empresasId) {
        this.empresasId = empresasId;
    }

    public LongFilter getFiliaisId() {
        return filiaisId;
    }

    public LongFilter filiaisId() {
        if (filiaisId == null) {
            filiaisId = new LongFilter();
        }
        return filiaisId;
    }

    public void setFiliaisId(LongFilter filiaisId) {
        this.filiaisId = filiaisId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AdministradorCriteria that = (AdministradorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(atualizado, that.atualizado) &&
            Objects.equals(ultimoLogin, that.ultimoLogin) &&
            Objects.equals(empresasId, that.empresasId) &&
            Objects.equals(filiaisId, that.filiaisId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, criado, atualizado, ultimoLogin, empresasId, filiaisId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministradorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (atualizado != null ? "atualizado=" + atualizado + ", " : "") +
            (ultimoLogin != null ? "ultimoLogin=" + ultimoLogin + ", " : "") +
            (empresasId != null ? "empresasId=" + empresasId + ", " : "") +
            (filiaisId != null ? "filiaisId=" + filiaisId + ", " : "") +
            "}";
    }
}
