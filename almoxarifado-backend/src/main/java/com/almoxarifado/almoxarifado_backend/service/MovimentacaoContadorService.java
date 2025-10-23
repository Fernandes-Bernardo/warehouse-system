package com.almoxarifado.almoxarifado_backend.service;

import com.almoxarifado.almoxarifado_backend.model.MovimentacaoContador;
import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.repository.MovimentacaoContadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoContadorService {

    private final MovimentacaoContadorRepository contadorRepository;

    public MovimentacaoContadorService(MovimentacaoContadorRepository contadorRepository) {
        this.contadorRepository = contadorRepository;
    }

    // Atualiza o contador de movimentações para um produto
    public void registrarMovimentacao(Produto produto) {
        MovimentacaoContador contador = contadorRepository.findByProduto_Id(produto.getId()).orElse(null);

        if (contador == null) {
            contador = new MovimentacaoContador(produto);
        } else {
            contador.incrementar();
        }

        contadorRepository.save(contador);
    }

    // Retorna os produtos mais movimentados
    public List<MovimentacaoContador> buscarMaisMovimentados(int limite) {
        return contadorRepository.findAll().stream()
                .sorted((a, b) -> b.getTotalMovimentacoes().compareTo(a.getTotalMovimentacoes()))
                .limit(limite)
                .toList();
    }
}