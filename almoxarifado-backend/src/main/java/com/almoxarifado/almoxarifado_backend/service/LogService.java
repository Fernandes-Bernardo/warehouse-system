package com.almoxarifado.almoxarifado_backend.service;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.almoxarifado.almoxarifado_backend.model.LogOperacao;
import com.almoxarifado.almoxarifado_backend.repository.LogOperacaoRepository;

@Service
public class LogService {
    private final LogOperacaoRepository logOperacaoRepository;

    public LogService(LogOperacaoRepository logOperacaoRepository) {
        this.logOperacaoRepository = logOperacaoRepository;
    }

    public void registrar(String acao, String entidade, Long entidadeId, String descricao) {
        LogOperacao log = new LogOperacao();
        log.setDataHora(LocalDateTime.now());
        log.setUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
        log.setAcao(acao);
        log.setEntidade(entidade);
        log.setEntidadeId(entidadeId);
        log.setDescricao(descricao);

        logOperacaoRepository.save(log);
    }

}
