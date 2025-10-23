package com.almoxarifado.almoxarifado_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.almoxarifado.almoxarifado_backend.model.ProdutoCache;

@Repository
public interface ProdutoCacheRepository extends JpaRepository<ProdutoCache, Long> {
}