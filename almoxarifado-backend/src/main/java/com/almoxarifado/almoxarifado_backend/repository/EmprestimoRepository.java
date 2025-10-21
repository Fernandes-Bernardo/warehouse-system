package com.almoxarifado.almoxarifado_backend.repository;

import com.almoxarifado.almoxarifado_backend.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
}