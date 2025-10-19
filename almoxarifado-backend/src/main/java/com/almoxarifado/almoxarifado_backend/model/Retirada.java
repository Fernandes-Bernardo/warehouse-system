package com.almoxarifado.almoxarifado_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Retirada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private LocalDateTime dataHora;
    private String responsavel;   
    private String destino;      
    private Integer quantidadeRetirada;

    // Relacionamentos

    // N : 1
    @ManyToOne
    @JoinColumn(name = "produto_id") // Relaciona FK no banco
    private Produto produto;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Integer getQuantidadeRetirada() { return quantidadeRetirada; }
    public void setQuantidadeRetirada(Integer quantidadeRetirada) { this.quantidadeRetirada = quantidadeRetirada; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

}
