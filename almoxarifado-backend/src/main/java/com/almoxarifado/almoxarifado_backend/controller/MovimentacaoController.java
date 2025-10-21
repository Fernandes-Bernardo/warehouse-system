package com.almoxarifado.almoxarifado_backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.almoxarifado.almoxarifado_backend.dto.MovimentacaoDTO;
import com.almoxarifado.almoxarifado_backend.dto.ResumoProdutoDTO;
import com.almoxarifado.almoxarifado_backend.model.Entrada;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.model.Retirada;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;

@RestController
@RequestMapping("/api/v1/movimentacoes")
public class MovimentacaoController {

    private final ProdutoRepository produtoRepository;

    public MovimentacaoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Histórico geral de movimentações (entradas + retiradas) com paginação
    @GetMapping
    public Page<MovimentacaoDTO> listarMovimentacoes(
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) String destino,
            Pageable pageable) {

        List<MovimentacaoDTO> movimentacoes = new ArrayList<>();

        // Percorre todos os produtos e junta entradas + retiradas
        for (Produto produto : produtoRepository.findAll()) {

            for (Entrada entrada : produto.getEntradas()) {
                movimentacoes.add(new MovimentacaoDTO(
                        entrada.getDataHora(),
                        "ENTRADA",
                        entrada.getResponsavel(),
                        entrada.getQuantidadeAdicionada(),
                        null,
                        produto.getNome(),
                        produto.getId(),
                        produto.getCategoria(),
                        produto.getPrateleira(),
                        produto.getOrigem(),
                        produto.getQuantidade(),
                        produto.getEstoqueMinimo()
                ));
            }

            for (Retirada retirada : produto.getRetiradas()) {
                movimentacoes.add(new MovimentacaoDTO(
                        retirada.getDataHora(),
                        "RETIRADA",
                        retirada.getResponsavel(),
                        retirada.getQuantidadeRetirada(),
                        retirada.getDestino(),
                        produto.getNome(),
                        produto.getId(),
                        produto.getCategoria(),
                        produto.getPrateleira(),
                        produto.getOrigem(),
                        produto.getQuantidade(),
                        produto.getEstoqueMinimo()
                ));
            }
        }

        // Filtros
        if (tipo != null && !tipo.isBlank()) {
            movimentacoes.removeIf(m -> !m.getTipo().equalsIgnoreCase(tipo));
        }

        if (responsavel != null && !responsavel.isBlank()) {
            movimentacoes.removeIf(m -> m.getResponsavel() == null ||
                    !m.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()));
        }

        if (destino != null && !destino.isBlank()) {
            movimentacoes.removeIf(m -> m.getDestino() == null ||
                    !m.getDestino().toLowerCase().contains(destino.toLowerCase()));
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
                movimentacoes.removeIf(m -> m.getDataHora().isBefore(limite));
            }
        }

        if (dataInicio != null && dataFim != null) {
            LocalDateTime inicio = LocalDateTime.parse(dataInicio + "T00:00:00");
            LocalDateTime fim = LocalDateTime.parse(dataFim + "T23:59:59");
            movimentacoes.removeIf(m -> m.getDataHora().isBefore(inicio) || m.getDataHora().isAfter(fim));
        }

        // Ordenação
        if ("desc".equalsIgnoreCase(ordenacao)) {
            movimentacoes.sort(Comparator.comparing(MovimentacaoDTO::getDataHora).reversed());
        } else {
            movimentacoes.sort(Comparator.comparing(MovimentacaoDTO::getDataHora));
        }

        // Paginação manual
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), movimentacoes.size());
        List<MovimentacaoDTO> pageContent = movimentacoes.subList(start, end);

        return new PageImpl<>(pageContent, pageable, movimentacoes.size());
    }

    // Histórico apenas de movimentações críticas (estoque abaixo do mínimo)
    @GetMapping("/criticas")
    public Page<MovimentacaoDTO> listarMovimentacoesCriticas(
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) String destino,
            Pageable pageable) {

        Page<MovimentacaoDTO> todas = listarMovimentacoes(
                ordenacao, tipo, responsavel, periodo, dataInicio, dataFim, destino, pageable);

        List<MovimentacaoDTO> criticas = todas.getContent().stream()
                .filter(MovimentacaoDTO::isEstoqueBaixo)
                .toList();

        return new PageImpl<>(criticas, pageable, criticas.size());
    }

    // Relatório resumido de todos os produtos
    @GetMapping("/resumo")
    public Page<ResumoProdutoDTO> listarResumoProdutos(Pageable pageable) {
        List<ResumoProdutoDTO> resumo = new ArrayList<>();

        for (Produto produto : produtoRepository.findAll()) {
            resumo.add(new ResumoProdutoDTO(
                    produto.getId(),
                    produto.getNome(),
                    produto.getCategoria(),
                    produto.getPrateleira(),
                    produto.getOrigem(),
                    produto.getQuantidade(),
                    produto.getEstoqueMinimo()
            ));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resumo.size());
        List<ResumoProdutoDTO> pageContent = resumo.subList(start, end);

        return new PageImpl<>(pageContent, pageable, resumo.size());
    }

    // Relatório resumido apenas com produtos críticos
    @GetMapping("/resumo/criticos")
    public Page<ResumoProdutoDTO> listarResumoProdutosCriticos(Pageable pageable) {
        List<ResumoProdutoDTO> resumo = new ArrayList<>();

        for (Produto produto : produtoRepository.findAll()) {
            ResumoProdutoDTO dto = new ResumoProdutoDTO(
                    produto.getId(),
                    produto.getNome(),
                    produto.getCategoria(),
                    produto.getPrateleira(),
                    produto.getOrigem(),
                    produto.getQuantidade(),
                    produto.getEstoqueMinimo()
            );

            if (dto.isEstoqueBaixo()) {
                resumo.add(dto);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resumo.size());
        List<ResumoProdutoDTO> pageContent = resumo.subList(start, end);

        return new PageImpl<>(pageContent, pageable, resumo.size());
    }
}