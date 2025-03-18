package com.desafio.tecnico.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.desafio.tecnico.domain.Processo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessoDTO implements Serializable {

    private Long id;

    private String npu;

    private LocalDate dataCadastro;

    private LocalDate dataVisualizacao;

    private String municipio;

    private String uf;

    @Lob
    private String documento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNpu() {
        return npu;
    }

    public void setNpu(String npu) {
        this.npu = npu;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataVisualizacao() {
        return dataVisualizacao;
    }

    public void setDataVisualizacao(LocalDate dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }


    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessoDTO)) {
            return false;
        }

        ProcessoDTO processoDTO = (ProcessoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessoDTO{" +
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
