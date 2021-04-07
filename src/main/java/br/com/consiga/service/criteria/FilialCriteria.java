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
 * Criteria class for the {@link br.com.consiga.domain.Filial} entity. This class is used
 * in {@link br.com.consiga.web.rest.FilialResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /filials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FilialCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter codigo;

    private StringFilter cnpj;

    private LongFilter empresaId;

    private LongFilter administradoresId;

    public FilialCriteria() {}

    public FilialCriteria(FilialCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.administradoresId = other.administradoresId == null ? null : other.administradoresId.copy();
    }

    @Override
    public FilialCriteria copy() {
        return new FilialCriteria(this);
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

    public StringFilter getCodigo() {
        return codigo;
    }

    public StringFilter codigo() {
        if (codigo == null) {
            codigo = new StringFilter();
        }
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
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

    public LongFilter getEmpresaId() {
        return empresaId;
    }

    public LongFilter empresaId() {
        if (empresaId == null) {
            empresaId = new LongFilter();
        }
        return empresaId;
    }

    public void setEmpresaId(LongFilter empresaId) {
        this.empresaId = empresaId;
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
        final FilialCriteria that = (FilialCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(administradoresId, that.administradoresId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, codigo, cnpj, empresaId, administradoresId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilialCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (codigo != null ? "codigo=" + codigo + ", " : "") +
            (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (administradoresId != null ? "administradoresId=" + administradoresId + ", " : "") +
            "}";
    }
}
