package com.desafio.tecnico.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Processo.
 */
@Entity
@Table(name = "processo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Processo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "npu")
    private String npu;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "data_visualizacao")
    private LocalDate dataVisualizacao;

    @Column(name = "municipio")
    private String municipio;

    @Column(name = "uf")
    private String uf;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String documento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Processo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNpu() {
        return this.npu;
    }

    public Processo npu(String npu) {
        this.setNpu(npu);
        return this;
    }

    public void setNpu(String npu) {
        this.npu = npu;
    }

    public LocalDate getDataCadastro() {
        return this.dataCadastro;
    }

    public Processo dataCadastro(LocalDate dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataVisualizacao() {
        return this.dataVisualizacao;
    }

    public Processo dataVisualizacao(LocalDate dataVisualizacao) {
        this.setDataVisualizacao(dataVisualizacao);
        return this;
    }

    public void setDataVisualizacao(LocalDate dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public Processo municipio(String municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }


    public String getUf() {
        return this.uf;
    }

    public Processo uf(String uf) {
        this.setUf(uf);
        return this;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getDocumento() {
        return this.documento;
    }

    public Processo documento(String documento) {
        this.setDocumento(documento);
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Processo)) {
            return false;
        }
        return getId() != null && getId().equals(((Processo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Processo{" +
            "id=" + getId() +
            ", npu='" + getNpu() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", dataVisualizacao='" + getDataVisualizacao() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", uf='" + getUf() + "'" +
            ", documento='" + getDocumento() + "'" +
            "}";
    }
}
