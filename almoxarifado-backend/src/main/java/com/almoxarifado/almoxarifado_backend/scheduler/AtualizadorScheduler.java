package com.almoxarifado.almoxarifado_backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;

import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.model.ProdutoCache;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoCacheRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AtualizadorScheduler {

    private final ProdutoRepository produtoRepository;
    private final ProdutoCacheRepository produtoCacheRepository;

    public AtualizadorScheduler(ProdutoRepository produtoRepository, ProdutoCacheRepository produtoCacheRepository) {
        this.produtoRepository = produtoRepository;
        this.produtoCacheRepository = produtoCacheRepository;
    }

    @Scheduled(cron = "0 */10 * * * *") // Executa a cada 10 minutos
    public void atualizarCacheProdutosMaisMovimentados() {
        List<Produto> maisMovimentados = produtoRepository.findMaisMovimentados(PageRequest.of(0, 10));

        List<ProdutoCache> cacheAtualizado = maisMovimentados.stream()
            .map(p -> {
                ProdutoCache pc = new ProdutoCache();
                pc.setId(p.getId());
                pc.setNome(p.getNome());
                pc.setQuantidade(p.getQuantidade());
                pc.setAtualizadoEm(LocalDateTime.now());
                return pc;
            }).collect(Collectors.toList());

        produtoCacheRepository.saveAll(cacheAtualizado);
    }
}