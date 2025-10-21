package com.almoxarifado.almoxarifado_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.almoxarifado.almoxarifado_backend.model.Retirada;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.repository.RetiradaRepository;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/retiradas")
public class RetiradaController {

    private final RetiradaRepository retiradaRepository;
    private final ProdutoRepository produtoRepository;

    // Injeção de dependências
    public RetiradaController(RetiradaRepository retiradaRepository, ProdutoRepository produtoRepository) {
        this.retiradaRepository = retiradaRepository;
        this.produtoRepository = produtoRepository;
    }

    // Limpar todas as retiradas
    @DeleteMapping("/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    public void limparTodasRetiradas() {
        retiradaRepository.deleteAll();
    }

    // Listar todas as retiradas
    @GetMapping
    public List<Retirada> listarTodas() {
        return retiradaRepository.findAll();
    }

    // Buscar retirada por ID
    @GetMapping("/{id}")
    public Retirada buscarPorId(@PathVariable Long id) {
        return retiradaRepository.findById(id).orElse(null);
    }

    // Listar retiradas por produto
    @GetMapping("/produto/{produtoId}")
    public List<Retirada> listarPorProduto(@PathVariable Long produtoId) {
        return retiradaRepository.findByProdutoId(produtoId);
    }

    // Registrar uma retirada (somente ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Retirada registrar(@RequestBody Retirada retirada) {
        // valida produto
        Long produtoId = retirada.getProduto().getId();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: id=" + produtoId));

        // valida quantidade
        if (retirada.getQuantidadeRetirada() == null || retirada.getQuantidadeRetirada() <= 0) {
            throw new IllegalArgumentException("Quantidade de retirada deve ser maior que zero.");
        }

        // checa estoque suficiente
        int estoqueAtual = produto.getQuantidade() != null ? produto.getQuantidade() : 0;
        if (retirada.getQuantidadeRetirada() > estoqueAtual) {
            throw new IllegalArgumentException("Estoque insuficiente. Disponível: " + estoqueAtual);
        }

        // atualiza estoque
        produto.setQuantidade(estoqueAtual - retirada.getQuantidadeRetirada());
        produtoRepository.save(produto);

        // registra retirada
        return retiradaRepository.save(retirada);
    }
}