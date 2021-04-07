package br.com.consiga.service.criteria;

import br.com.consiga.domain.enumeration.StatusArquivo;
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
 * Criteria class for the {@link br.com.consiga.domain.ArquivoImportacao} entity. This class is used
 * in {@link br.com.consiga.web.rest.ArquivoImportacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /arquivo-importacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArquivoImportacaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusArquivo
     */
    public static class StatusArquivoFilter extends Filter<StatusArquivo> {

        public StatusArquivoFilter() {}

        public StatusArquivoFilter(StatusArquivoFilter filter) {
            super(filter);
        }

        @Override
        public StatusArquivoFilter copy() {
            return new StatusArquivoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter urlArquivo;

    private StringFilter urlCriticas;

    private ZonedDateTimeFilter criado;

    private StatusArquivoFilter estado;

    private ZonedDateTimeFilter processado;

    private LongFilter criadorId;

    private LongFilter empresaId;

    private LongFilter filialId;

    public ArquivoImportacaoCriteria() {}

    public ArquivoImportacaoCriteria(ArquivoImportacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.urlArquivo = other.urlArquivo == null ? null : other.urlArquivo.copy();
        this.urlCriticas = other.urlCriticas == null ? null : other.urlCriticas.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.processado = other.processado == null ? null : other.processado.copy();
        this.criadorId = other.criadorId == null ? null : other.criadorId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.filialId = other.filialId == null ? null : other.filialId.copy();
    }

    @Override
    public ArquivoImportacaoCriteria copy() {
        return new ArquivoImportacaoCriteria(this);
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

    public StringFilter getUrlArquivo() {
        return urlArquivo;
    }

    public StringFilter urlArquivo() {
        if (urlArquivo == null) {
            urlArquivo = new StringFilter();
        }
        return urlArquivo;
    }

    public void setUrlArquivo(StringFilter urlArquivo) {
        this.urlArquivo = urlArquivo;
    }

    public StringFilter getUrlCriticas() {
        return urlCriticas;
    }

    public StringFilter urlCriticas() {
        if (urlCriticas == null) {
            urlCriticas = new StringFilter();
        }
        return urlCriticas;
    }

    public void setUrlCriticas(StringFilter urlCriticas) {
        this.urlCriticas = urlCriticas;
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

    public StatusArquivoFilter getEstado() {
        return estado;
    }

    public StatusArquivoFilter estado() {
        if (estado == null) {
            estado = new StatusArquivoFilter();
        }
        return estado;
    }

    public void setEstado(StatusArquivoFilter estado) {
        this.estado = estado;
    }

    public ZonedDateTimeFilter getProcessado() {
        return processado;
    }

    public ZonedDateTimeFilter processado() {
        if (processado == null) {
            processado = new ZonedDateTimeFilter();
        }
        return processado;
    }

    public void setProcessado(ZonedDateTimeFilter processado) {
        this.processado = processado;
    }

    public LongFilter getCriadorId() {
        return criadorId;
    }

    public LongFilter criadorId() {
        if (criadorId == null) {
            criadorId = new LongFilter();
        }
        return criadorId;
    }

    public void setCriadorId(LongFilter criadorId) {
        this.criadorId = criadorId;
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

    public LongFilter getFilialId() {
        return filialId;
    }

    public LongFilter filialId() {
        if (filialId == null) {
            filialId = new LongFilter();
        }
        return filialId;
    }

    public void setFilialId(LongFilter filialId) {
        this.filialId = filialId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArquivoImportacaoCriteria that = (ArquivoImportacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(urlArquivo, that.urlArquivo) &&
            Objects.equals(urlCriticas, that.urlCriticas) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(processado, that.processado) &&
            Objects.equals(criadorId, that.criadorId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(filialId, that.filialId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlArquivo, urlCriticas, criado, estado, processado, criadorId, empresaId, filialId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoImportacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (urlArquivo != null ? "urlArquivo=" + urlArquivo + ", " : "") +
            (urlCriticas != null ? "urlCriticas=" + urlCriticas + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (processado != null ? "processado=" + processado + ", " : "") +
            (criadorId != null ? "criadorId=" + criadorId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (filialId != null ? "filialId=" + filialId + ", " : "") +
            "}";
    }
}
