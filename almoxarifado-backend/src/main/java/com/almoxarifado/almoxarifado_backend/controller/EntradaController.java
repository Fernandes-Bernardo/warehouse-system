package com.almoxarifado.almoxarifado_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.almoxarifado.almoxarifado_backend.model.Entrada;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.repository.EntradaRepository;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/entradas") // URL base: http://localhost:8080/api/v1/entradas
public class EntradaController {

    private final EntradaRepository entradaRepository;
    private final ProdutoRepository produtoRepository;

    // Injeção de dependências
    public EntradaController(EntradaRepository entradaRepository, ProdutoRepository produtoRepository) {
        this.entradaRepository = entradaRepository;
        this.produtoRepository = produtoRepository;
    }

    // Limpar todas as entradas
    @DeleteMapping("/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    public void limparTodasEntradas() {
        entradaRepository.deleteAll();
    }

    // Listar todas as entradas
    @GetMapping
    public List<Entrada> listarTodas() {
        return entradaRepository.findAll();
    }

    // Buscar entrada por ID
    @GetMapping("/{id}")
    public Entrada buscarPorId(@PathVariable Long id) {
        return entradaRepository.findById(id).orElse(null);
    }

    // Listar entradas por produto
    @GetMapping("/produto/{produtoId}")
    public List<Entrada> listarPorProduto(@PathVariable Long produtoId) {
        return entradaRepository.findByProdutoId(produtoId);
    }

    // Registrar uma entrada (somente ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Entrada registrar(@RequestBody Entrada entrada) {
        // valida produto
        Long produtoId = entrada.getProduto().getId();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: id=" + produtoId));

        // valida quantidade
        if (entrada.getQuantidadeAdicionada() == null || entrada.getQuantidadeAdicionada() <= 0) {
            throw new IllegalArgumentException("Quantidade de entrada deve ser maior que zero.");
        }

        // atualiza estoque
        int estoqueAtual = produto.getQuantidade() != null ? produto.getQuantidade() : 0;
        produto.setQuantidade(estoqueAtual + entrada.getQuantidadeAdicionada());
        produtoRepository.save(produto);

        // persiste a entrada
        return entradaRepository.save(entrada);
    }
}