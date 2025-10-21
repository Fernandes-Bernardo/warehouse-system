package com.almoxarifado.almoxarifado_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.almoxarifado.almoxarifado_backend.model.Produto;

// JpaRepository<Entidade, TipoDaChavePrimaria>
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // Buscar por nome
    List<Produto> findByNomeContainingIgnoreCase(String nome);

     // Buscar por categoria (ignora maiúsculas/minúsculas)
    List<Produto> findByCategoriaIgnoreCase(String categoria);

    // Buscar por prateleira
    List<Produto> findByPrateleiraIgnoreCase(String prateleira);

    // Buscar produtos com estoque baixo
    List<Produto> findByQuantidadeLessThanEqual(Integer quantidade);
}