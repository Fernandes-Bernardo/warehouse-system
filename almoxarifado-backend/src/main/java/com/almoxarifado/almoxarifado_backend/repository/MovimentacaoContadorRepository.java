package com.almoxarifado.almoxarifado_backend.repository;

import com.almoxarifado.almoxarifado_backend.model.MovimentacaoContador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovimentacaoContadorRepository extends JpaRepository<MovimentacaoContador, Long> {

    Optional<MovimentacaoContador> findByProduto_Id(Long produtoId);
}