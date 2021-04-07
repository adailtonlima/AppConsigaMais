package br.com.consiga.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.consiga.domain.FaturaMensal} entity.
 */
public class FaturaMensalDTO implements Serializable {

    private Long id;

    private LocalDate mes;

    private ZonedDateTime criado;

    private String boletoUrl;

    private ZonedDateTime dataPago;

    private EmpresaDTO empresa;

    private FilialDTO filial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMes() {
        return mes;
    }

    public void setMes(LocalDate mes) {
        this.mes = mes;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public String getBoletoUrl() {
        return boletoUrl;
    }

    public void setBoletoUrl(String boletoUrl) {
        this.boletoUrl = boletoUrl;
    }

    public ZonedDateTime getDataPago() {
        return dataPago;
    }

    public void setDataPago(ZonedDateTime dataPago) {
        this.dataPago = dataPago;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public FilialDTO getFilial() {
        return filial;
    }

    public void setFilial(FilialDTO filial) {
        this.filial = filial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FaturaMensalDTO)) {
            return false;
        }

        FaturaMensalDTO faturaMensalDTO = (FaturaMensalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, faturaMensalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FaturaMensalDTO{" +
            "id=" + getId() +
            ", mes='" + getMes() + "'" +
            ", criado='" + getCriado() + "'" +
            ", boletoUrl='" + getBoletoUrl() + "'" +
            ", dataPago='" + getDataPago() + "'" +
            ", empresa=" + getEmpresa() +
            ", filial=" + getFilial() +
            "}";
    }
}
