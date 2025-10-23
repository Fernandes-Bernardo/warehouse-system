package com.almoxarifado.almoxarifado_backend.controller;

import com.almoxarifado.almoxarifado_backend.dto.EntradaDTO;
import com.almoxarifado.almoxarifado_backend.model.Entrada;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.repository.EntradaRepository;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;
import com.almoxarifado.almoxarifado_backend.service.LogService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/entradas")
public class EntradaController {

    private final EntradaRepository entradaRepository;
    private final ProdutoRepository produtoRepository;
    private final LogService logService;

    public EntradaController(EntradaRepository entradaRepository,
                             ProdutoRepository produtoRepository,
                             LogService logService) {
        this.entradaRepository = entradaRepository;
        this.produtoRepository = produtoRepository;
        this.logService = logService;
    }

    @DeleteMapping("/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    public void limparTodasEntradas() {
        entradaRepository.deleteAll();
    }

    @GetMapping
    public Page<EntradaDTO> listarEntradas(
            @RequestParam(required = false) Long produtoId,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            Pageable pageable) {

        List<EntradaDTO> entradas = entradaRepository.findAll().stream()
                .map(e -> {
                    Produto p = e.getProduto();
                    return new EntradaDTO(
                            e.getId(),
                            e.getDataHora(),
                            e.getQuantidadeAdicionada(),
                            p.getId(),
                            p.getNome(),
                            p.getCategoria(),
                            p.getPrateleira(),
                            p.getOrigem()
                    );
                })
                .collect(Collectors.toList());

        if (produtoId != null) {
            entradas.removeIf(e -> !e.getProdutoId().equals(produtoId));
        }

        if (periodo != null) {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime limite = switch (periodo.toLowerCase()) {
                case "semana" -> agora.minusWeeks(1);
                case "mes" -> agora.minusMonths(1);
                case "ano" -> agora.minusYears(1);
                default -> null;
            };
            if (limite != null) {
                entradas.removeIf(e -> e.getDataHora().isBefore(limite));
            }
        }

        if (dataInicio != null && dataFim != null) {
            LocalDateTime inicio = LocalDateTime.parse(dataInicio + "T00:00:00");
            LocalDateTime fim = LocalDateTime.parse(dataFim + "T23:59:59");
            entradas.removeIf(e -> e.getDataHora().isBefore(inicio) || e.getDataHora().isAfter(fim));
        }

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Comparator<EntradaDTO> comparator = switch (order.getProperty()) {
                    case "dataHora" -> Comparator.comparing(EntradaDTO::getDataHora);
                    case "produtoNome" -> Comparator.comparing(EntradaDTO::getProdutoNome, Comparator.nullsLast(String::compareToIgnoreCase));
                    default -> null;
                };
                if (comparator != null) {
                    entradas.sort(order.isAscending() ? comparator : comparator.reversed());
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), entradas.size());
        List<EntradaDTO> pageContent = entradas.subList(start, end);

        return new PageImpl<>(pageContent, pageable, entradas.size());
    }

    @GetMapping("/{id}")
    public Entrada buscarPorId(@PathVariable Long id) {
        return entradaRepository.findById(id).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Entrada registrar(@RequestBody Entrada entrada) {
        Long produtoId = entrada.getProduto().getId();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: id=" + produtoId));

        if (entrada.getQuantidadeAdicionada() == null || entrada.getQuantidadeAdicionada() <= 0) {
            throw new IllegalArgumentException("Quantidade de entrada deve ser maior que zero.");
        }

        int estoqueAtual = produto.getQuantidade() != null ? produto.getQuantidade() : 0;
        produto.setQuantidade(estoqueAtual + entrada.getQuantidadeAdicionada());
        produtoRepository.save(produto);

        entrada.setDataHora(LocalDateTime.now());
        entrada.setProduto(produto);

        Entrada salva = entradaRepository.save(entrada);

        logService.registrar(
                "ENTRADA_ESTOQUE",
                "Entrada",
                salva.getId(),
                "Entrada de " + salva.getQuantidadeAdicionada() +
                        " unidades no produto '" + produto.getNome() + "'"
        );

        return salva;
    }
}