package com.almoxarifado.almoxarifado_backend.dto;

import java.time.LocalDateTime;

public class EntradaDTO {

    private Long id;
    private LocalDateTime dataHora;
    private Integer quantidadeAdicionada;
    private Long produtoId;
    private String produtoNome;
    private String produtoCategoria;
    private String produtoPrateleira;
    private String produtoOrigem;

    public EntradaDTO(Long id, LocalDateTime dataHora, Integer quantidadeAdicionada,
                      Long produtoId, String produtoNome, String produtoCategoria,
                      String produtoPrateleira, String produtoOrigem) {
        this.id = id;
        this.dataHora = dataHora;
        this.quantidadeAdicionada = quantidadeAdicionada;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.produtoCategoria = produtoCategoria;
        this.produtoPrateleira = produtoPrateleira;
        this.produtoOrigem = produtoOrigem;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Integer getQuantidadeAdicionada() { return quantidadeAdicionada; }
    public Long getProdutoId() { return produtoId; }
    public String getProdutoNome() { return produtoNome; }
    public String getProdutoCategoria() { return produtoCategoria; }
    public String getProdutoPrateleira() { return produtoPrateleira; }
    public String getProdutoOrigem() { return produtoOrigem; }
}