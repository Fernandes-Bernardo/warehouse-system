package com.almoxarifado.almoxarifado_backend.dto;

import java.time.LocalDateTime;

public class MovimentacaoDTO {
    private LocalDateTime dataHora;
    private String tipo;
    private String responsavel;
    private Integer quantidade;
    private String destino;

    // Construtor
    public MovimentacaoDTO(LocalDateTime dataHora, String tipo, String responsavel, Integer quantidade,
            String destino) {
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.responsavel = responsavel;
        this.quantidade = quantidade;
        this.destino = destino;
    }

    // Getters
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getDestino() {
        return destino;
    }
}