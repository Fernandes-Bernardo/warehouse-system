package com.almoxarifado.almoxarifado_backend.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.almoxarifado.almoxarifado_backend.dto.MovimentacaoDTO;
import com.almoxarifado.almoxarifado_backend.model.Entrada;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.model.Retirada;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/produtos") // URL base: http://localhost:8080/api/v1/produtos
public class ProdutoController {
    private final ProdutoRepository repository;

    // Injeção de dependência
    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    // Deletar todos os produtos (limpeza geral)
    @DeleteMapping("/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    public void limparTudo() {
        repository.deleteAll();
    }

    // Listar todos os produtos
    @GetMapping
    public List<Produto> listar() {
        return repository.findAll();
    }

    // Buscar produto por id
    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // Buscar por nome
    @GetMapping("/nome/{nome}")
    public List<Produto> buscarPorNome(@PathVariable String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar por prateleira
    @GetMapping("/prateleira/{prateleira}")
    public List<Produto> buscarPorPrateleira(@PathVariable String prateleira) {
        return repository.findByPrateleiraIgnoreCase(prateleira);
    }

    // Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    public List<Produto> buscarPorCategoria(@PathVariable String categoria) {
        return repository.findByCategoriaIgnoreCase(categoria);
    }

    // Listar produtos com estoque baixo
    @GetMapping("/estoque-baixo")
    public List<Produto> listarEstoqueBaixo() {
        return repository.findAll()
                .stream()
                .filter(p -> p.getQuantidade() != null
                        && p.getEstoqueMinimo() != null
                        && p.getQuantidade() <= p.getEstoqueMinimo())
                .toList();
    }

    // Funções de admin
    // Registrar novo produto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Produto registrar(@RequestBody Produto produto) {
        return repository.save(produto);
    }

    // Atualizar produto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        return repository.save(produto);
    }

    // Deletar produto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }

    // Histórico de movimentações de um produto
    @GetMapping("/{id}/movimentacoes")
    public List<MovimentacaoDTO> listarMovimentacoes(
            @PathVariable Long id,
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String periodo) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: id=" + id));

        List<MovimentacaoDTO> movimentacoes = new ArrayList<>();

        // Converte entradas em DTOs
        for (Entrada entrada : produto.getEntradas()) {
            MovimentacaoDTO dto = new MovimentacaoDTO(
                    entrada.getDataHora(),
                    "ENTRADA",
                    entrada.getResponsavel(),
                    entrada.getQuantidadeAdicionada(),
                    null);
            movimentacoes.add(dto);
        }

        // Converte retiradas em DTOs
        for (Retirada retirada : produto.getRetiradas()) {
            MovimentacaoDTO dto = new MovimentacaoDTO(
                    retirada.getDataHora(),
                    "RETIRADA",
                    retirada.getResponsavel(),
                    retirada.getQuantidadeRetirada(),
                    retirada.getDestino());
            movimentacoes.add(dto);
        }

        // Filtro por tipo
        if (tipo != null && !tipo.isBlank()) {
            movimentacoes.removeIf(m -> !m.getTipo().equalsIgnoreCase(tipo));
        }

        // Filtro por responsável
        if (responsavel != null && !responsavel.isBlank()) {
            movimentacoes.removeIf(m -> m.getResponsavel() == null ||
                    !m.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()));
        }

        // Filtro por período
        if (periodo != null) {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime limite = switch (periodo.toLowerCase()) {
                case "semana" -> agora.minusWeeks(1);
                case "mes" -> agora.minusMonths(1);
                case "ano" -> agora.minusYears(1);
                default -> null;
            };
            if (limite != null) {
                movimentacoes.removeIf(m -> m.getDataHora().isBefore(limite));
            }
        }

        // Ordenação
        if ("desc".equalsIgnoreCase(ordenacao)) {
            movimentacoes.sort(Comparator.comparing(MovimentacaoDTO::getDataHora).reversed());
        } else {
            movimentacoes.sort(Comparator.comparing(MovimentacaoDTO::getDataHora));
        }

        return movimentacoes;
    }
}