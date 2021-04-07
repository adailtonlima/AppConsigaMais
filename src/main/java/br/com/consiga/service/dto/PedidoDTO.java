package br.com.consiga.service.dto;

import br.com.consiga.domain.enumeration.StatusPedido;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.consiga.domain.Pedido} entity.
 */
public class PedidoDTO implements Serializable {

    private Long id;

    private StatusPedido estado;

    private ZonedDateTime criado;

    private ZonedDateTime dataAprovacao;

    private LocalDate dataExpiracao;

    private Double renda;

    private Double valorSolicitado;

    private Integer qtParcelasSolicitado;

    private Double valorAprovado;

    private Double valorParcelaAprovado;

    private Integer qtParcelasAprovado;

    private LocalDate dataPrimeiroVencimento;

    private LocalDate dataUltimoVencimento;

    private FuncionarioDTO funcionario;

    private EmpresaDTO empresa;

    private FilialDTO filia;

    private AdministradorDTO quemAprovou;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusPedido getEstado() {
        return estado;
    }

    public void setEstado(StatusPedido estado) {
        this.estado = estado;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public ZonedDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(ZonedDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Double getRenda() {
        return renda;
    }

    public void setRenda(Double renda) {
        this.renda = renda;
    }

    public Double getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(Double valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public Integer getQtParcelasSolicitado() {
        return qtParcelasSolicitado;
    }

    public void setQtParcelasSolicitado(Integer qtParcelasSolicitado) {
        this.qtParcelasSolicitado = qtParcelasSolicitado;
    }

    public Double getValorAprovado() {
        return valorAprovado;
    }

    public void setValorAprovado(Double valorAprovado) {
        this.valorAprovado = valorAprovado;
    }

    public Double getValorParcelaAprovado() {
        return valorParcelaAprovado;
    }

    public void setValorParcelaAprovado(Double valorParcelaAprovado) {
        this.valorParcelaAprovado = valorParcelaAprovado;
    }

    public Integer getQtParcelasAprovado() {
        return qtParcelasAprovado;
    }

    public void setQtParcelasAprovado(Integer qtParcelasAprovado) {
        this.qtParcelasAprovado = qtParcelasAprovado;
    }

    public LocalDate getDataPrimeiroVencimento() {
        return dataPrimeiroVencimento;
    }

    public void setDataPrimeiroVencimento(LocalDate dataPrimeiroVencimento) {
        this.dataPrimeiroVencimento = dataPrimeiroVencimento;
    }

    public LocalDate getDataUltimoVencimento() {
        return dataUltimoVencimento;
    }

    public void setDataUltimoVencimento(LocalDate dataUltimoVencimento) {
        this.dataUltimoVencimento = dataUltimoVencimento;
    }

    public FuncionarioDTO getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioDTO funcionario) {
        this.funcionario = funcionario;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public FilialDTO getFilia() {
        return filia;
    }

    public void setFilia(FilialDTO filia) {
        this.filia = filia;
    }

    public AdministradorDTO getQuemAprovou() {
        return quemAprovou;
    }

    public void setQuemAprovou(AdministradorDTO quemAprovou) {
        this.quemAprovou = quemAprovou;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PedidoDTO)) {
            return false;
        }

        PedidoDTO pedidoDTO = (PedidoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pedidoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoDTO{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", criado='" + getCriado() + "'" +
            ", dataAprovacao='" + getDataAprovacao() + "'" +
            ", dataExpiracao='" + getDataExpiracao() + "'" +
            ", renda=" + getRenda() +
            ", valorSolicitado=" + getValorSolicitado() +
            ", qtParcelasSolicitado=" + getQtParcelasSolicitado() +
            ", valorAprovado=" + getValorAprovado() +
            ", valorParcelaAprovado=" + getValorParcelaAprovado() +
            ", qtParcelasAprovado=" + getQtParcelasAprovado() +
            ", dataPrimeiroVencimento='" + getDataPrimeiroVencimento() + "'" +
            ", dataUltimoVencimento='" + getDataUltimoVencimento() + "'" +
            ", funcionario=" + getFuncionario() +
            ", empresa=" + getEmpresa() +
            ", filia=" + getFilia() +
            ", quemAprovou=" + getQuemAprovou() +
            "}";
    }
}
