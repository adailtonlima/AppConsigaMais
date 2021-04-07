package br.com.consiga.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.consiga.domain.Funcionario} entity.
 */
public class FuncionarioDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String cpf;

    private ZonedDateTime criado;

    private ZonedDateTime atualizado;

    private ZonedDateTime ultimoLogin;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public ZonedDateTime getAtualizado() {
        return atualizado;
    }

    public void setAtualizado(ZonedDateTime atualizado) {
        this.atualizado = atualizado;
    }

    public ZonedDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(ZonedDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FuncionarioDTO)) {
            return false;
        }

        FuncionarioDTO funcionarioDTO = (FuncionarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, funcionarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncionarioDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", criado='" + getCriado() + "'" +
            ", atualizado='" + getAtualizado() + "'" +
            ", ultimoLogin='" + getUltimoLogin() + "'" +
            "}";
    }
}
