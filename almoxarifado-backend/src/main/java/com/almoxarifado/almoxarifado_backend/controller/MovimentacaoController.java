package com.almoxarifado.almoxarifado_backend.controller;

import com.almoxarifado.almoxarifado_backend.dto.MovimentacaoDTO;
import com.almoxarifado.almoxarifado_backend.dto.NovaEntradaDTO;
import com.almoxarifado.almoxarifado_backend.dto.NovaRetiradaDTO;
import com.almoxarifado.almoxarifado_backend.dto.ResumoProdutoDTO;
import com.almoxarifado.almoxarifado_backend.model.Entrada;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.model.Retirada;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;
import com.almoxarifado.almoxarifado_backend.service.MovimentacaoContadorService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/movimentacoes")
public class MovimentacaoController {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoContadorService contadorService;

    public MovimentacaoController(ProdutoRepository produtoRepository,
                                  MovimentacaoContadorService contadorService) {
        this.produtoRepository = produtoRepository;
        this.contadorService = contadorService;
    }

    // ---------- NOVOS ENDPOINTS ADICIONADOS ----------
    @PostMapping("/entrada")
    public ResponseEntity<Void> registrarEntrada(@RequestBody NovaEntradaDTO entradaDTO) {
        Produto produto = produtoRepository.findById(entradaDTO.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Entrada entrada = new Entrada();
        entrada.setDataHora(LocalDateTime.now());
        entrada.setResponsavel(entradaDTO.getResponsavel());
        entrada.setQuantidadeAdicionada(entradaDTO.getQuantidade());
        produto.getEntradas().add(entrada);

        produto.setQuantidade(produto.getQuantidade() + entradaDTO.getQuantidade());
        produtoRepository.save(produto);

        contadorService.registrarMovimentacao(produto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/retirada")
    public ResponseEntity<Void> registrarRetirada(@RequestBody NovaRetiradaDTO retiradaDTO) {
        Produto produto = produtoRepository.findById(retiradaDTO.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Retirada retirada = new Retirada();
        retirada.setDataHora(LocalDateTime.now());
        retirada.setResponsavel(retiradaDTO.getResponsavel());
        retirada.setDestino(retiradaDTO.getDestino());
        retirada.setQuantidadeRetirada(retiradaDTO.getQuantidade());
        produto.getRetiradas().add(retirada);

        produto.setQuantidade(produto.getQuantidade() - retiradaDTO.getQuantidade());
        produtoRepository.save(produto);

        contadorService.registrarMovimentacao(produto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // -------------------------------------------------

    @GetMapping
    public Page<MovimentacaoDTO> listarMovimentacoes(
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) Long produtoId,
            Pageable pageable) {

        List<MovimentacaoDTO> movimentacoes = new ArrayList<>();

        for (Produto produto : produtoRepository.findAll()) {
            if (produtoId != null && !produto.getId().equals(produtoId)) continue;

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
                        produto.getEstoqueMinimo()));
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
                        produto.getEstoqueMinimo()));
            }
        }

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

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Comparator<MovimentacaoDTO> comparator = switch (order.getProperty()) {
                    case "dataHora" -> Comparator.comparing(MovimentacaoDTO::getDataHora);
                    case "tipo" -> Comparator.comparing(MovimentacaoDTO::getTipo, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "responsavel" -> Comparator.comparing(MovimentacaoDTO::getResponsavel, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "produtoNome" -> Comparator.comparing(MovimentacaoDTO::getProdutoNome, Comparator.nullsLast(String::compareToIgnoreCase));
                    default -> null;
                };
                if (comparator != null) {
                    movimentacoes.sort(order.isAscending() ? comparator : comparator.reversed());
                }
            }
        } else if ("desc".equalsIgnoreCase(ordenacao)) {
            movimentacoes.sort(Comparator.comparing(MovimentacaoDTO::getDataHora).reversed());
        } else {
            movimentacoes.sort(Comparator.comparing(MovimentacaoDTO::getDataHora));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), movimentacoes.size());
        List<MovimentacaoDTO> pageContent = movimentacoes.subList(start, end);

        return new PageImpl<>(pageContent, pageable, movimentacoes.size());
    }

    @GetMapping("/criticas")
    public Page<MovimentacaoDTO> listarMovimentacoesCriticas(
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) Long produtoId,
            Pageable pageable) {

        Page<MovimentacaoDTO> todas = listarMovimentacoes(
                ordenacao, tipo, responsavel, periodo, dataInicio, dataFim, destino, produtoId, pageable);

        List<MovimentacaoDTO> criticas = todas.getContent().stream()
                .filter(MovimentacaoDTO::isEstoqueBaixo)
                .toList();

        return new PageImpl<>(criticas, pageable, criticas.size());
    }

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
                    produto.getEstoqueMinimo()));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), resumo.size());
        List<ResumoProdutoDTO> pageContent = resumo.subList(start, end);

        return new PageImpl<>(pageContent, pageable, resumo.size());
    }

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
                    produto.getEstoqueMinimo());

            if (dto.isEstoqueBaixo()) {
                resumo.add(dto);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), resumo.size());
        List<ResumoProdutoDTO> pageContent = resumo.subList(start, end);

        return new PageImpl<>(pageContent, pageable, resumo.size());
    }
}
