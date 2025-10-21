package com.almoxarifado.almoxarifado_backend.dto;

public class ProdutoDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String prateleira;
    private String origem;
    private Integer quantidade;
    private Integer estoqueMinimo;

    public ProdutoDTO(Long id, String nome, String categoria, String prateleira,
                      String origem, Integer quantidade, Integer estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.prateleira = prateleira;
        this.origem = origem;
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public String getPrateleira() { return prateleira; }
    public String getOrigem() { return origem; }
    public Integer getQuantidade() { return quantidade; }
    public Integer getEstoqueMinimo() { return estoqueMinimo; }

    public boolean isEstoqueBaixo() {
        return quantidade != null && estoqueMinimo != null && quantidade <= estoqueMinimo;
    }
}