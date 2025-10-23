package com.almoxarifado.almoxarifado_backend.controller;

import com.almoxarifado.almoxarifado_backend.dto.ProdutoDTO;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;
import com.almoxarifado.almoxarifado_backend.service.LogService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;
    private final LogService logService;

    public ProdutoController(ProdutoRepository repository, LogService logService) {
        this.repository = repository;
        this.logService = logService;
    }

    @DeleteMapping("/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    public void limparTudo() {
        repository.deleteAll();
    }

    @GetMapping
    public Page<ProdutoDTO> listarProdutos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String nome,
            Pageable pageable) {

        List<ProdutoDTO> produtos = repository.findAll().stream()
                .map(p -> new ProdutoDTO(
                        p.getId(),
                        p.getNome(),
                        p.getCategoria(),
                        p.getPrateleira(),
                        p.getOrigem(),
                        p.getQuantidade(),
                        p.getEstoqueMinimo()))
                .collect(Collectors.toList());

        if (categoria != null && !categoria.isBlank()) {
            produtos.removeIf(p -> p.getCategoria() == null ||
                    !p.getCategoria().equalsIgnoreCase(categoria));
        }

        if (origem != null && !origem.isBlank()) {
            produtos.removeIf(p -> p.getOrigem() == null ||
                    !p.getOrigem().equalsIgnoreCase(origem));
        }

        if (nome != null && !nome.isBlank()) {
            produtos.removeIf(p -> p.getNome() == null ||
                    !p.getNome().toLowerCase().contains(nome.toLowerCase()));
        }

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Comparator<ProdutoDTO> comparator = switch (order.getProperty()) {
                    case "nome" -> Comparator.comparing(ProdutoDTO::getNome, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "categoria" -> Comparator.comparing(ProdutoDTO::getCategoria, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "quantidade" -> Comparator.comparing(ProdutoDTO::getQuantidade, Comparator.nullsLast(Integer::compareTo));
                    default -> null;
                };
                if (comparator != null) {
                    produtos.sort(order.isAscending() ? comparator : comparator.reversed());
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), produtos.size());
        List<ProdutoDTO> pageContent = produtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, produtos.size());
    }

    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/estoque-baixo")
    public Page<ProdutoDTO> listarEstoqueBaixo(Pageable pageable) {
        List<ProdutoDTO> produtos = repository.findAll().stream()
                .map(p -> new ProdutoDTO(
                        p.getId(),
                        p.getNome(),
                        p.getCategoria(),
                        p.getPrateleira(),
                        p.getOrigem(),
                        p.getQuantidade(),
                        p.getEstoqueMinimo()))
                .filter(ProdutoDTO::isEstoqueBaixo)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), produtos.size());
        List<ProdutoDTO> pageContent = produtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, produtos.size());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Produto registrar(@RequestBody Produto produto) {
        Produto salvo = repository.save(produto);

        logService.registrar(
                "CADASTRO_PRODUTO",
                "Produto",
                salvo.getId(),
                "Produto '" + salvo.getNome() + "' cadastrado com quantidade " + salvo.getQuantidade()
        );

        return salvo;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        Produto atualizado = repository.save(produto);

        logService.registrar(
                "ATUALIZACAO_PRODUTO",
                "Produto",
                atualizado.getId(),
                "Produto '" + atualizado.getNome() + "' atualizado com nova quantidade " + atualizado.getQuantidade()
        );

        return atualizado;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        Produto produto = repository.findById(id).orElse(null);
        repository.deleteById(id);

        if (produto != null) {
            logService.registrar(
                    "EXCLUSAO_PRODUTO",
                    "Produto",
                    id,
                    "Produto '" + produto.getNome() + "' foi excluído do sistema"
            );
        }
    }
}