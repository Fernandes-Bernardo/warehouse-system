package com.almoxarifado.almoxarifado_backend.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity //Classe que ira para uma tabela no DB
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Increment
    private Long id; // PK
    private String nome;
    private Integer quantidade;
    private String descricao;
    private String categoria;
    private String prateleira;
    private Integer estoqueMinimo;

    // Relacionamentos

    // 1 : N
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<Entrada> entradas;
    
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<Retirada> retiradas;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPrateleira() { return prateleira; }
    public void setPrateleira(String prateleira) { this.prateleira = prateleira; }

    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
}