package com.almoxarifado.almoxarifado_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private LocalDateTime dataHora = LocalDateTime.now();

    @Column(nullable = false)
    private String responsavel;

    @Column(nullable = false)
    private Integer quantidadeAdicionada;

    // N:1 
    @ManyToOne(optional = false) 
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonBackReference
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