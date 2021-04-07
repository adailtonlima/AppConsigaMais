package br.com.consiga.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.consiga.domain.FaturaMensal} entity. This class is used
 * in {@link br.com.consiga.web.rest.FaturaMensalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fatura-mensals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FaturaMensalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter mes;

    private ZonedDateTimeFilter criado;

    private StringFilter boletoUrl;

    private ZonedDateTimeFilter dataPago;

    private LongFilter empresaId;

    private LongFilter filialId;

    public FaturaMensalCriteria() {}

    public FaturaMensalCriteria(FaturaMensalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.mes = other.mes == null ? null : other.mes.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.boletoUrl = other.boletoUrl == null ? null : other.boletoUrl.copy();
        this.dataPago = other.dataPago == null ? null : other.dataPago.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.filialId = other.filialId == null ? null : other.filialId.copy();
    }

    @Override
    public FaturaMensalCriteria copy() {
        return new FaturaMensalCriteria(this);
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

    public LocalDateFilter getMes() {
        return mes;
    }

    public LocalDateFilter mes() {
        if (mes == null) {
            mes = new LocalDateFilter();
        }
        return mes;
    }

    public void setMes(LocalDateFilter mes) {
        this.mes = mes;
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

    public StringFilter getBoletoUrl() {
        return boletoUrl;
    }

    public StringFilter boletoUrl() {
        if (boletoUrl == null) {
            boletoUrl = new StringFilter();
        }
        return boletoUrl;
    }

    public void setBoletoUrl(StringFilter boletoUrl) {
        this.boletoUrl = boletoUrl;
    }

    public ZonedDateTimeFilter getDataPago() {
        return dataPago;
    }

    public ZonedDateTimeFilter dataPago() {
        if (dataPago == null) {
            dataPago = new ZonedDateTimeFilter();
        }
        return dataPago;
    }

    public void setDataPago(ZonedDateTimeFilter dataPago) {
        this.dataPago = dataPago;
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
        final FaturaMensalCriteria that = (FaturaMensalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mes, that.mes) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(boletoUrl, that.boletoUrl) &&
            Objects.equals(dataPago, that.dataPago) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(filialId, that.filialId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mes, criado, boletoUrl, dataPago, empresaId, filialId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FaturaMensalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (mes != null ? "mes=" + mes + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (boletoUrl != null ? "boletoUrl=" + boletoUrl + ", " : "") +
            (dataPago != null ? "dataPago=" + dataPago + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (filialId != null ? "filialId=" + filialId + ", " : "") +
            "}";
    }
}
