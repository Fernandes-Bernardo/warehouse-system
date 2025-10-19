package com.almoxarifado.almoxarifado_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Entrada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private LocalDateTime dataHora;
    private String responsavel;
    private Integer quantidadeAdicionada;

    // Relacionamentos

    // N : 1
    @ManyToOne
    @JoinColumn(name = "produto_id") // Relacionando FK no banco
    private Produto produto;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public Integer getQuantidadeAdicionada() { return quantidadeAdicionada; }
    public void setQuantidadeAdicionada(Integer quantidadeAdicionada) { this.quantidadeAdicionada = quantidadeAdicionada; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}