package br.com.consiga.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link br.com.consiga.domain.Filial} entity.
 */
public class FilialDTO implements Serializable {

    private Long id;

    private String nome;

    private String codigo;

    private String cnpj;

    private EmpresaDTO empresa;

    private Set<AdministradorDTO> administradores = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public Set<AdministradorDTO> getAdministradores() {
        return administradores;
    }

    public void setAdministradores(Set<AdministradorDTO> administradores) {
        this.administradores = administradores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilialDTO)) {
            return false;
        }

        FilialDTO filialDTO = (FilialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilialDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", empresa=" + getEmpresa() +
            ", administradores=" + getAdministradores() +
            "}";
    }
}
