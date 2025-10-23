package com.almoxarifado.almoxarifado_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movimentacao_contador")
public class MovimentacaoContador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "id")
    private Produto produto;

    private Integer totalMovimentacoes;

    public MovimentacaoContador() {
    }

    public MovimentacaoContador(Produto produto) {
        this.produto = produto;
        this.totalMovimentacoes = 1;
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getTotalMovimentacoes() {
        return totalMovimentacoes;
    }

    public void setTotalMovimentacoes(Integer totalMovimentacoes) {
        this.totalMovimentacoes = totalMovimentacoes;
    }

    public void incrementar() {
        this.totalMovimentacoes++;
    }
}