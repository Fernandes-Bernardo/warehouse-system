package com.almoxarifado.almoxarifado_backend.dto;

import java.time.LocalDateTime;

public class RetiradaDTO {
    private Long id;
    private LocalDateTime dataHora;
    private String responsavel;
    private String destino;
    private Integer quantidadeRetirada;
    private Long produtoId;
    private String produtoNome;
    private String produtoCategoria;
    private String produtoPrateleira;
    private String produtoOrigem;

    public RetiradaDTO(Long id, LocalDateTime dataHora, String responsavel, String destino,
                       Integer quantidadeRetirada, Long produtoId, String produtoNome,
                       String produtoCategoria, String produtoPrateleira, String produtoOrigem) {
        this.id = id;
        this.dataHora = dataHora;
        this.responsavel = responsavel;
        this.destino = destino;
        this.quantidadeRetirada = quantidadeRetirada;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.produtoCategoria = produtoCategoria;
        this.produtoPrateleira = produtoPrateleira;
        this.produtoOrigem = produtoOrigem;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getResponsavel() { return responsavel; }
    public String getDestino() { return destino; }
    public Integer getQuantidadeRetirada() { return quantidadeRetirada; }
    public Long getProdutoId() { return produtoId; }
    public String getProdutoNome() { return produtoNome; }
    public String getProdutoCategoria() { return produtoCategoria; }
    public String getProdutoPrateleira() { return produtoPrateleira; }
    public String getProdutoOrigem() { return produtoOrigem; }
}