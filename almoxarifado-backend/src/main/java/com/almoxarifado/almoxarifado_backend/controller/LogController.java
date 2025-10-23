package com.almoxarifado.almoxarifado_backend.controller;

import com.almoxarifado.almoxarifado_backend.model.LogOperacao;
import com.almoxarifado.almoxarifado_backend.repository.LogOperacaoRepository;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

    private final LogOperacaoRepository logRepository;

    public LogController(LogOperacaoRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping
    public Page<LogOperacao> listarLogs(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String acao,
            @RequestParam(required = false) String entidade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            Pageable pageable) {

        List<LogOperacao> logs = logRepository.findAll();

        if (usuario != null && !usuario.isBlank()) {
            logs.removeIf(l -> l.getUsuario() == null || !l.getUsuario().toLowerCase().contains(usuario.toLowerCase()));
        }

        if (acao != null && !acao.isBlank()) {
            logs.removeIf(l -> l.getAcao() == null || !l.getAcao().equalsIgnoreCase(acao));
        }

        if (entidade != null && !entidade.isBlank()) {
            logs.removeIf(l -> l.getEntidade() == null || !l.getEntidade().equalsIgnoreCase(entidade));
        }

        if (dataInicio != null && dataFim != null) {
            logs.removeIf(l -> l.getDataHora().isBefore(dataInicio) || l.getDataHora().isAfter(dataFim));
        }

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Comparator<LogOperacao> comparator = switch (order.getProperty()) {
                    case "dataHora" -> Comparator.comparing(LogOperacao::getDataHora);
                    case "usuario" -> Comparator.comparing(LogOperacao::getUsuario, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "acao" -> Comparator.comparing(LogOperacao::getAcao, Comparator.nullsLast(String::compareToIgnoreCase));
                    case "entidade" -> Comparator.comparing(LogOperacao::getEntidade, Comparator.nullsLast(String::compareToIgnoreCase));
                    default -> null;
                };
                if (comparator != null) {
                    logs.sort(order.isAscending() ? comparator : comparator.reversed());
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), logs.size());
        List<LogOperacao> pageContent = logs.subList(start, end);

        return new PageImpl<>(pageContent, pageable, logs.size());
    }
}