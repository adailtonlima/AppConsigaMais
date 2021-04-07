package br.com.consiga.service.dto;

import br.com.consiga.domain.enumeration.StatusArquivo;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.consiga.domain.ArquivoImportacao} entity.
 */
public class ArquivoImportacaoDTO implements Serializable {

    private Long id;

    private String urlArquivo;

    private String urlCriticas;

    private ZonedDateTime criado;

    private StatusArquivo estado;

    private ZonedDateTime processado;

    private AdministradorDTO criador;

    private EmpresaDTO empresa;

    private FilialDTO filial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlArquivo() {
        return urlArquivo;
    }

    public void setUrlArquivo(String urlArquivo) {
        this.urlArquivo = urlArquivo;
    }

    public String getUrlCriticas() {
        return urlCriticas;
    }

    public void setUrlCriticas(String urlCriticas) {
        this.urlCriticas = urlCriticas;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public StatusArquivo getEstado() {
        return estado;
    }

    public void setEstado(StatusArquivo estado) {
        this.estado = estado;
    }

    public ZonedDateTime getProcessado() {
        return processado;
    }

    public void setProcessado(ZonedDateTime processado) {
        this.processado = processado;
    }

    public AdministradorDTO getCriador() {
        return criador;
    }

    public void setCriador(AdministradorDTO criador) {
        this.criador = criador;
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
        if (!(o instanceof ArquivoImportacaoDTO)) {
            return false;
        }

        ArquivoImportacaoDTO arquivoImportacaoDTO = (ArquivoImportacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, arquivoImportacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoImportacaoDTO{" +
            "id=" + getId() +
            ", urlArquivo='" + getUrlArquivo() + "'" +
            ", urlCriticas='" + getUrlCriticas() + "'" +
            ", criado='" + getCriado() + "'" +
            ", estado='" + getEstado() + "'" +
            ", processado='" + getProcessado() + "'" +
            ", criador=" + getCriador() +
            ", empresa=" + getEmpresa() +
            ", filial=" + getFilial() +
            "}";
    }
}
