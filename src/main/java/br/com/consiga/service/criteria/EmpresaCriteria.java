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

/**
 * Criteria class for the {@link br.com.consiga.domain.Empresa} entity. This class is used
 * in {@link br.com.consiga.web.rest.EmpresaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /empresas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmpresaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cnpj;

    private StringFilter razaoSocial;

    private LongFilter administradoresId;

    public EmpresaCriteria() {}

    public EmpresaCriteria(EmpresaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.razaoSocial = other.razaoSocial == null ? null : other.razaoSocial.copy();
        this.administradoresId = other.administradoresId == null ? null : other.administradoresId.copy();
    }

    @Override
    public EmpresaCriteria copy() {
        return new EmpresaCriteria(this);
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

    public StringFilter getCnpj() {
        return cnpj;
    }

    public StringFilter cnpj() {
        if (cnpj == null) {
            cnpj = new StringFilter();
        }
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getRazaoSocial() {
        return razaoSocial;
    }

    public StringFilter razaoSocial() {
        if (razaoSocial == null) {
            razaoSocial = new StringFilter();
        }
        return razaoSocial;
    }

    public void setRazaoSocial(StringFilter razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public LongFilter getAdministradoresId() {
        return administradoresId;
    }

    public LongFilter administradoresId() {
        if (administradoresId == null) {
            administradoresId = new LongFilter();
        }
        return administradoresId;
    }

    public void setAdministradoresId(LongFilter administradoresId) {
        this.administradoresId = administradoresId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmpresaCriteria that = (EmpresaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(razaoSocial, that.razaoSocial) &&
            Objects.equals(administradoresId, that.administradoresId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cnpj, razaoSocial, administradoresId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpresaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
            (razaoSocial != null ? "razaoSocial=" + razaoSocial + ", " : "") +
            (administradoresId != null ? "administradoresId=" + administradoresId + ", " : "") +
            "}";
    }
}
