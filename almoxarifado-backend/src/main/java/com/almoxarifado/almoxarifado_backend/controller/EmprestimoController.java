package com.almoxarifado.almoxarifado_backend.controller;

import com.almoxarifado.almoxarifado_backend.dto.EmprestimoDTO;
import com.almoxarifado.almoxarifado_backend.model.Emprestimo;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.repository.EmprestimoRepository;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;
import com.almoxarifado.almoxarifado_backend.service.LogService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/emprestimos")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;
    private final ProdutoRepository produtoRepository;
    private final LogService logService;

    public EmprestimoController(EmprestimoRepository emprestimoRepository,
                                ProdutoRepository produtoRepository,
                                LogService logService) {
        this.emprestimoRepository = emprestimoRepository;
        this.produtoRepository = produtoRepository;
        this.logService = logService;
    }

    @GetMapping
    public Page<EmprestimoDTO> listarEmprestimos(
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            Pageable pageable) {

        List<EmprestimoDTO> emprestimos = emprestimoRepository.findAll().stream()
                .map(e -> {
                    Produto p = e.getProduto();
                    return new EmprestimoDTO(
                            e.getId(),
                            e.getDataHora(),
                            e.getResponsavel(),
                            e.getDestino(),
                            e.getQuantidadeEmprestada(),
                            p.getId(),
                            p.getNome(),
                            p.getCategoria(),
                            p.getPrateleira(),
                            p.getOrigem()
                    );
                })
                .collect(Collectors.toList());

        if (responsavel != null && !responsavel.isBlank()) {
            emprestimos.removeIf(e -> e.getResponsavel() == null ||
                    !e.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()));
        }

        if (destino != null && !destino.isBlank()) {
            emprestimos.removeIf(e -> e.getDestino() == null ||
                    !e.getDestino().toLowerCase().contains(destino.toLowerCase()));
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
                emprestimos.removeIf(e -> e.getDataHora().isBefore(limite));
            }
        }

        if (dataInicio != null && dataFim != null) {
            LocalDateTime inicio = LocalDateTime.parse(dataInicio + "T00:00:00");
            LocalDateTime fim = LocalDateTime.parse(dataFim + "T23:59:59");
            emprestimos.removeIf(e -> e.getDataHora().isBefore(inicio) || e.getDataHora().isAfter(fim));
        }

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Comparator<EmprestimoDTO> comparator = switch (order.getProperty()) {
                    case "dataHora" -> Comparator.comparing(EmprestimoDTO::getDataHora);
                    case "responsavel" -> Comparator.comparing(EmprestimoDTO::getResponsavel, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "destino" -> Comparator.comparing(EmprestimoDTO::getDestino, Comparator.nullsLast(String::compareToIgnoreCase));
                    default -> null;
                };
                if (comparator != null) {
                    emprestimos.sort(order.isAscending() ? comparator : comparator.reversed());
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), emprestimos.size());
        List<EmprestimoDTO> pageContent = emprestimos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, emprestimos.size());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Emprestimo registrarEmprestimo(@RequestBody Emprestimo emprestimo) {
        Long produtoId = emprestimo.getProduto().getId();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: ID " + produtoId));

        emprestimo.setProduto(produto);
        emprestimo.setDataHora(LocalDateTime.now());

        Emprestimo salvo = emprestimoRepository.save(emprestimo);

        logService.registrar(
                "EMPRESTIMO",
                "Emprestimo",
                salvo.getId(),
                "Empréstimo de " + salvo.getQuantidadeEmprestada() +
                        " unidades do produto '" + produto.getNome() +
                        "' para '" + salvo.getDestino() +
                        "' (responsável: " + salvo.getResponsavel() + ")"
        );

        return salvo;
    }
}