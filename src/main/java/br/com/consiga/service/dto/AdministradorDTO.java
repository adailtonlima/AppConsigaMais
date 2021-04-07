package br.com.consiga.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.consiga.domain.Administrador} entity.
 */
public class AdministradorDTO implements Serializable {

    private Long id;

    private String nome;

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
        if (!(o instanceof AdministradorDTO)) {
            return false;
        }

        AdministradorDTO administradorDTO = (AdministradorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, administradorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministradorDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criado='" + getCriado() + "'" +
            ", atualizado='" + getAtualizado() + "'" +
            ", ultimoLogin='" + getUltimoLogin() + "'" +
            "}";
    }
}
