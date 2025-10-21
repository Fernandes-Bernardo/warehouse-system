package com.almoxarifado.almoxarifado_backend.dto;

import java.time.LocalDateTime;

public class EmprestimoDTO {

    private Long id;
    private LocalDateTime dataHora;
    private String responsavel;
    private String destino;
    private Integer quantidadeEmprestada;

    private Long produtoId;
    private String produtoNome;
    private String produtoCategoria;
    private String produtoPrateleira;
    private String produtoOrigem;

    // Construtor padrão (necessário para serialização/deserialização)
    public EmprestimoDTO() {
    }

    // Construtor completo
    public EmprestimoDTO(Long id, LocalDateTime dataHora, String responsavel, String destino,
                         Integer quantidadeEmprestada, Long produtoId, String produtoNome,
                         String produtoCategoria, String produtoPrateleira, String produtoOrigem) {
        this.id = id;
        this.dataHora = dataHora;
        this.responsavel = responsavel;
        this.destino = destino;
        this.quantidadeEmprestada = quantidadeEmprestada;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.produtoCategoria = produtoCategoria;
        this.produtoPrateleira = produtoPrateleira;
        this.produtoOrigem = produtoOrigem;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Integer getQuantidadeEmprestada() { return quantidadeEmprestada; }
    public void setQuantidadeEmprestada(Integer quantidadeEmprestada) { this.quantidadeEmprestada = quantidadeEmprestada; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public String getProdutoCategoria() { return produtoCategoria; }
    public void setProdutoCategoria(String produtoCategoria) { this.produtoCategoria = produtoCategoria; }

    public String getProdutoPrateleira() { return produtoPrateleira; }
    public void setProdutoPrateleira(String produtoPrateleira) { this.produtoPrateleira = produtoPrateleira; }

    public String getProdutoOrigem() { return produtoOrigem; }
    public void setProdutoOrigem(String produtoOrigem) { this.produtoOrigem = produtoOrigem; }
}