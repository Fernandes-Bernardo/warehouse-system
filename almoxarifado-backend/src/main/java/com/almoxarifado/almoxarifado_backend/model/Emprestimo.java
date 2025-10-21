package com.almoxarifado.almoxarifado_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String responsavel;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private Integer quantidadeEmprestada;

    private LocalDateTime dataHora = LocalDateTime.now();

    // N : 1
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonBackReference
    private Produto produto;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getQuantidadeEmprestada() {
        return quantidadeEmprestada;
    }

    public void setQuantidadeEmprestada(Integer quantidadeEmprestada) {
        this.quantidadeEmprestada = quantidadeEmprestada;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

}