package com.almoxarifado.almoxarifado_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.almoxarifado.almoxarifado_backend.model.Retirada;

public interface RetiradaRepository extends JpaRepository<Retirada, Long> {
    // histórico de retiradas por produto
    List<Retirada> findByProdutoId(Long produtoId);
}