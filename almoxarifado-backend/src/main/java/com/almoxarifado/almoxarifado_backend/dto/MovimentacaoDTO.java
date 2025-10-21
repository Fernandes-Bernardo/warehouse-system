package com.almoxarifado.almoxarifado_backend.dto;

import java.time.LocalDateTime;

public class MovimentacaoDTO {
    private LocalDateTime dataHora;
    private String tipo;
    private String responsavel;
    private Integer quantidade;
    private String destino;
    private String produtoNome;
    private Long produtoId;
    private String produtoCategoria;
    private String produtoPrateleira;
    private String produtoOrigem;
    private Integer produtoEstoqueAtual;
    private Integer produtoEstoqueMinimo;

    // Construtor
    public MovimentacaoDTO(LocalDateTime dataHora, String tipo, String responsavel, Integer quantidade,
                           String destino, String produtoNome, Long produtoId,
                           String produtoCategoria, String produtoPrateleira,
                           String produtoOrigem, Integer produtoEstoqueAtual,
                           Integer produtoEstoqueMinimo) {
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.responsavel = responsavel;
        this.quantidade = quantidade;
        this.destino = destino;
        this.produtoNome = produtoNome;
        this.produtoId = produtoId;
        this.produtoCategoria = produtoCategoria;
        this.produtoPrateleira = produtoPrateleira;
        this.produtoOrigem = produtoOrigem;
        this.produtoEstoqueAtual = produtoEstoqueAtual;
        this.produtoEstoqueMinimo = produtoEstoqueMinimo;
    }

    public LocalDateTime getDataHora() { return dataHora; }
    public String getTipo() { return tipo; }
    public String getResponsavel() { return responsavel; }
    public Integer getQuantidade() { return quantidade; }
    public String getDestino() { return destino; }
    public String getProdutoNome() { return produtoNome; }
    public Long getProdutoId() { return produtoId; }
    public String getProdutoCategoria() { return produtoCategoria; }
    public String getProdutoPrateleira() { return produtoPrateleira; }
    public String getProdutoOrigem() { return produtoOrigem; }
    public Integer getProdutoEstoqueAtual() { return produtoEstoqueAtual; }
    public Integer getProdutoEstoqueMinimo() { return produtoEstoqueMinimo; }

    // Validando se o estoque esta abaixo do minimo
    public boolean isEstoqueBaixo() {
        return produtoEstoqueAtual != null 
            && produtoEstoqueMinimo != null 
            && produtoEstoqueAtual <= produtoEstoqueMinimo;
    }
}