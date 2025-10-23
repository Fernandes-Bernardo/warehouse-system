package com.almoxarifado.almoxarifado_backend.controller;

import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.model.ProdutoCache;
import com.almoxarifado.almoxarifado_backend.model.MovimentacaoContador;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoCacheRepository;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;
import com.almoxarifado.almoxarifado_backend.service.MovimentacaoContadorService;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoCacheController {

    private final ProdutoCacheRepository produtoCacheRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoContadorService contadorService;

    public ProdutoCacheController(
        ProdutoCacheRepository produtoCacheRepository,
        ProdutoRepository produtoRepository,
        MovimentacaoContadorService contadorService
    ) {
        this.produtoCacheRepository = produtoCacheRepository;
        this.produtoRepository = produtoRepository;
        this.contadorService = contadorService;
    }

    @GetMapping("/cache")
    public List<ProdutoCache> listarCache() {
        return produtoCacheRepository.findAll();
    }

    @GetMapping("/cache/atualizado")
    public List<ProdutoCache> listarCacheOrdenado() {
        return produtoCacheRepository.findAll(Sort.by(Sort.Direction.DESC, "atualizadoEm"));
    }

    @GetMapping("/movimentacoes/top")
    public List<MovimentacaoContador> maisMovimentados(@RequestParam(defaultValue = "10") int limite) {
        return contadorService.buscarMaisMovimentados(limite);
    }

    @PostMapping("/movimentacoes/registrar")
    public ResponseEntity<Void> registrarMovimentacao(@RequestBody Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        contadorService.registrarMovimentacao(produto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> limparCache() {
        produtoCacheRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}