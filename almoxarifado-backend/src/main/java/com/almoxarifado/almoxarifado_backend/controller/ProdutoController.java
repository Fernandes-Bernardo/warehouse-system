package com.almoxarifado.almoxarifado_backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.almoxarifado.almoxarifado_backend.model.Produto;
import com.almoxarifado.almoxarifado_backend.repository.ProdutoRepository;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/produtos") // URL base: http://localhost:8080/api/v1/produtos
public class ProdutoController {
    private final ProdutoRepository repository;

    // Injeção de dependência
    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    // Deletar todos os produtos
    @DeleteMapping("/limpar")
    @PreAuthorize("hasRole('ADMIN')")
    public void limparTudo() {
        repository.deleteAll();
    }

    // Listar todos os produtos
    @GetMapping
    public List<Produto> listar() {
        return repository.findAll();
    }

    // Buscar produto por id
    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // Buscar por nome
    @GetMapping("/nome/{nome}")
    public List<Produto> buscarPorNome(@PathVariable String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar por prateleira
    @GetMapping("/prateleira/{prateleira}")
    public List<Produto> buscarPorPrateleira(@PathVariable String prateleira) {
        return repository.findByPrateleiraIgnoreCase(prateleira);
    }

    // Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    public List<Produto> buscarPorCategoria(@PathVariable String categoria) {
        return repository.findByCategoriaIgnoreCase(categoria);
    }

    // Listar produtos com estoque baixo
    @GetMapping("/estoque-baixo")
    public List<Produto> listarEstoqueBaixo() {
        return repository.findAll()
                .stream()
                .filter(p -> p.getQuantidade() != null
                        && p.getEstoqueMinimo() != null
                        && p.getQuantidade() <= p.getEstoqueMinimo())
                .toList();
    }

    // Buscar produtos com filtros opcionais
    @GetMapping("/filtro")
    public List<Produto> filtrarProdutos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String nome) {

        // Começa com todos os produtos
        List<Produto> produtos = repository.findAll();

        // Filtro por categoria
        if (categoria != null && !categoria.isBlank()) {
            produtos = produtos.stream()
                    .filter(p -> p.getCategoria() != null &&
                            p.getCategoria().equalsIgnoreCase(categoria))
                    .toList();
        }

        // Filtro por origem
        if (origem != null && !origem.isBlank()) {
            produtos = produtos.stream()
                    .filter(p -> p.getOrigem() != null &&
                            p.getOrigem().equalsIgnoreCase(origem))
                    .toList();
        }

        // Filtro por nome (contém)
        if (nome != null && !nome.isBlank()) {
            produtos = produtos.stream()
                    .filter(p -> p.getNome() != null &&
                            p.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        return produtos;
    }

    // Funções de admin
    // Registrar novo produto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Produto registrar(@RequestBody Produto produto) {
        return repository.save(produto);
    }

    // Atualizar produto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        return repository.save(produto);
    }

    // Deletar produto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}