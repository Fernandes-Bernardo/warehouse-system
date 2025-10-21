package com.almoxarifado.almoxarifado_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.almoxarifado.almoxarifado_backend.model.Entrada;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    // Busca todas as entradas de um produto específico
    List<Entrada> findByProdutoId(Long produtoId);
}