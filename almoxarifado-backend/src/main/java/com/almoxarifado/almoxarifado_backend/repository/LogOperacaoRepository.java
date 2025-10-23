package com.almoxarifado.almoxarifado_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.almoxarifado.almoxarifado_backend.model.LogOperacao;

public interface LogOperacaoRepository extends JpaRepository<LogOperacao, Long> {
}
