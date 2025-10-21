package com.almoxarifado.almoxarifado_backend.controller;

import com.almoxarifado.almoxarifado_backend.dto.RetiradaDTO;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.model.Retirada;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;
import com.almoxarifado.almoxarifado_backend.repository.RetiradaRepository;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/retiradas")
public class RetiradaController {

    private final RetiradaRepository retiradaRepository;
    private final ProdutoRepository produtoRepository;

    public RetiradaController(RetiradaRepository retiradaRepository, ProdutoRepository produtoRepository) {
        this.retiradaRepository = retiradaRepository;
        this.produtoRepository = produtoRepository;
    }

    @DeleteMapping("/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    public void limparTodasRetiradas() {
        retiradaRepository.deleteAll();
    }

    @GetMapping
    public Page<RetiradaDTO> listarRetiradas(
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            Pageable pageable) {

        List<RetiradaDTO> retiradas = retiradaRepository.findAll().stream()
                .map(r -> {
                    Produto p = r.getProduto();
                    return new RetiradaDTO(
                            r.getId(),
                            r.getDataHora(),
                            r.getResponsavel(),
                            r.getDestino(),
                            r.getQuantidadeRetirada(),
                            p.getId(),
                            p.getNome(),
                            p.getCategoria(),
                            p.getPrateleira(),
                            p.getOrigem()
                    );
                })
                .collect(Collectors.toList());

        if (responsavel != null && !responsavel.isBlank()) {
            retiradas.removeIf(r -> r.getResponsavel() == null ||
                    !r.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()));
        }

        if (destino != null && !destino.isBlank()) {
            retiradas.removeIf(r -> r.getDestino() == null ||
                    !r.getDestino().toLowerCase().contains(destino.toLowerCase()));
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
                retiradas.removeIf(r -> r.getDataHora().isBefore(limite));
            }
        }

        if (dataInicio != null && dataFim != null) {
            LocalDateTime inicio = LocalDateTime.parse(dataInicio + "T00:00:00");
            LocalDateTime fim = LocalDateTime.parse(dataFim + "T23:59:59");
            retiradas.removeIf(r -> r.getDataHora().isBefore(inicio) || r.getDataHora().isAfter(fim));
        }

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Comparator<RetiradaDTO> comparator = switch (order.getProperty()) {
                    case "dataHora" -> Comparator.comparing(RetiradaDTO::getDataHora);
                    case "responsavel" -> Comparator.comparing(RetiradaDTO::getResponsavel, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "destino" -> Comparator.comparing(RetiradaDTO::getDestino, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "produtoNome" -> Comparator.comparing(RetiradaDTO::getProdutoNome, Comparator.nullsLast(String::compareToIgnoreCase));
                    default -> null;
                };
                if (comparator != null) {
                    retiradas.sort(order.isAscending() ? comparator : comparator.reversed());
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), retiradas.size());
        List<RetiradaDTO> pageContent = retiradas.subList(start, end);

        return new PageImpl<>(pageContent, pageable, retiradas.size());
    }

    @GetMapping("/{id}")
    public Retirada buscarPorId(@PathVariable Long id) {
        return retiradaRepository.findById(id).orElse(null);
    }

    @GetMapping("/produto/{produtoId}")
    public List<Retirada> listarPorProduto(@PathVariable Long produtoId) {
        return retiradaRepository.findByProdutoId(produtoId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Retirada registrar(@RequestBody Retirada retirada) {
        Long produtoId = retirada.getProduto().getId();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: id=" + produtoId));

        if (retirada.getQuantidadeRetirada() == null || retirada.getQuantidadeRetirada() <= 0) {
            throw new IllegalArgumentException("Quantidade de retirada deve ser maior que zero.");
        }

        int estoqueAtual = produto.getQuantidade() != null ? produto.getQuantidade() : 0;
        if (retirada.getQuantidadeRetirada() > estoqueAtual) {
            throw new IllegalArgumentException("Estoque insuficiente. Disponível: " + estoqueAtual);
        }

        produto.setQuantidade(estoqueAtual - retirada.getQuantidadeRetirada());
        produtoRepository.save(produto);

        retirada.setProduto(produto);
        retirada.setDataHora(LocalDateTime.now());

        return retiradaRepository.save(retirada);
    }
}