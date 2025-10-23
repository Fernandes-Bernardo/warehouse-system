package com.almoxarifado.almoxarifado_backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.almoxarifado.almoxarifado_backend.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByCategoriaIgnoreCase(String categoria);
    List<Produto> findByPrateleiraIgnoreCase(String prateleira);
    List<Produto> findByQuantidadeLessThanEqual(Integer quantidade);

    @Query("SELECT p FROM Produto p JOIN MovimentacaoContador m ON p = m.produto ORDER BY m.totalMovimentacoes DESC")
    List<Produto> findMaisMovimentados(Pageable pageable);
}