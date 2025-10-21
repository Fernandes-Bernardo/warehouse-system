package com.almoxarifado.almoxarifado_backend.dto;

public class ResumoProdutoDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String prateleira;
    private String origem;
    private Integer estoqueAtual;
    private Integer estoqueMinimo;
    private boolean estoqueBaixo;

    public ResumoProdutoDTO(Long id, String nome, String categoria, String prateleira,
                            String origem, Integer estoqueAtual, Integer estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.prateleira = prateleira;
        this.origem = origem;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
        this.estoqueBaixo = (estoqueAtual != null && estoqueMinimo != null && estoqueAtual <= estoqueMinimo);
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public String getPrateleira() { return prateleira; }
    public String getOrigem() { return origem; }
    public Integer getEstoqueAtual() { return estoqueAtual; }
    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public boolean isEstoqueBaixo() { return estoqueBaixo; }
}