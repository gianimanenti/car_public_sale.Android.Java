package com.moondu.leilao.model.ws;

import java.io.Serializable;

public class Cep implements Serializable {

    private static final long serialVersionUID = 8456324243613278293L;

    private Long cep;
    private String logradouro;
    private String bairro;
    private String complemento;
    private String localidade;
    private String uf;
    private String numero;
    private Long nrCodIbge;

    public Cep() {
    }

    public Long getCep() {
        return this.cep;
    }

    public void setCep(Long cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return this.bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getLocalidade() {
        return this.localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return this.uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getNrCodIbge() {
        return this.nrCodIbge;
    }

    public void setNrCodIbge(Long nrCodIbge) {
        this.nrCodIbge = nrCodIbge;
    }

    public boolean isEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cep cep1 = (Cep) o;

        if (cep != null ? !cep.equals(cep1.cep) : cep1.cep != null) return false;
        if (logradouro != null ? !logradouro.equals(cep1.logradouro) : cep1.logradouro != null) return false;
        if (bairro != null ? !bairro.equals(cep1.bairro) : cep1.bairro != null) return false;
        if (complemento != null ? !complemento.equals(cep1.complemento) : cep1.complemento != null) return false;
        if (localidade != null ? !localidade.equals(cep1.localidade) : cep1.localidade != null) return false;
        if (uf != null ? !uf.equals(cep1.uf) : cep1.uf != null) return false;
        return numero != null ? numero.equals(cep1.numero) : cep1.numero == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cep cep1 = (Cep) o;

        return cep != null ? cep.equals(cep1.cep) : cep1.cep == null;
    }

    @Override
    public int hashCode() {
        return cep != null ? cep.hashCode() : 0;
    }
}
