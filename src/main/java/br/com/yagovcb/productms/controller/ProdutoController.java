package br.com.yagovcb.productms.controller;

import br.com.yagovcb.productms.model.Produto;
import br.com.yagovcb.productms.service.ProdutoService;
import br.com.yagovcb.productms.service.dto.DetalheRespostaDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import br.com.yagovcb.productms.service.dto.ProdutoDTO;
import br.com.yagovcb.productms.util.ProdutoUri;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *  REST controller para gerenciar {@link Produto}.
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Slf4j
@RepositoryRestController
@RequestMapping()
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    /**
     * {@code POST /products} : Rest Endpoint de Criação do {@link Produto}
     * @param produtoDTO passado no corpo da requisição
     * @return the {@link ResponseEntity} com o status {@code 201 (CREATED)} e a entidade {@link Produto} criada
     * */
    @PostMapping(path = ProdutoUri.CRIAR_PRODUTO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> createProduto(@RequestBody ProdutoDTO produtoDTO) {
        log.info( "ProdutosController: Criando novo produto" + produtoDTO );
        return new ResponseEntity<>(service.salvarProduto( produtoDTO ), HttpStatus.CREATED);
    }


    /**
     * {@code PUT /products/{id}} : Rest Endpoint de Atualização do {@link Produto}
     * @param produtoDTO passado no corpo da requisição
     * @param produtoId passado no URL da requisição
     * @return the {@link ResponseEntity} com o status {@code 200 (OK)} e a entidade {@link Produto} criada
     * */
    @PutMapping(path = ProdutoUri.ATUALIZAR_PRODUTO + "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> atualizaProdutoPorId(@PathVariable("id") String produtoId, @RequestBody ProdutoDTO produtoDTO){
        log.info( "ProdutosController: Atualizando o produto de id - " + produtoId );
        return new ResponseEntity<>(service.atualizaProduto(produtoDTO, produtoId), HttpStatus.OK);
    }

    /**
     * {@code GET /products/{id}} : Rest Endpoint de busca de um {@link Produto} pelo id passado
     * @param produtoId passado no URL da requisição
     * @return the {@link ResponseEntity} com o status {@code 200 (OK)} e a entidade {@link Produto} criada
     * */
    @GetMapping(path = ProdutoUri.BUSCA_POR_ID + "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> getProdutoPorID(@PathVariable("id") String produtoId) {
        log.info("ProdutosController: Buscando produto do ID - " + produtoId + "...");
        return new ResponseEntity<>(service.getProdutoPorId(produtoId), HttpStatus.OK);
    }

    /**
     * {@code GET /products} : Rest Endpoint de busca de uma {@link List<Produto>} já criada
     * @param pageable paginação informada
     * @return the {@link ResponseEntity} com o status {@code 200 (OK)}} e a entidade {@link List<Produto>} criada
     * */
    @GetMapping(path = ProdutoUri.LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Produto>> getProdutos(@ApiParam Pageable pageable) {
        log.info("Retornando todos os Produtos Cadastrados");
        return new ResponseEntity<>(service.getProdutos(pageable).getContent(), HttpStatus.OK);
    }

    /**
     * {@code GET /products/search} : Rest Endpoint de busca personalizada de um {@link Produto}
     *
     * @param q passado no URL, não obrigatorio, representa o atributo Name da entidade {@link Produto}
     * @param maxPrice passado no URL, não obrigatorio, representa o maior valor do atributo preco da entidade {@link Produto}
     * @param minPrice passado no URL, não obrigatorio, representa o menor valor do atributo preco da entidade {@link Produto}
     *
     * @return the {@link ResponseEntity} com o status {@code 200 (OK)}} e a entidade {@link List<Produto>} criada
     * */
    @Transactional
    @GetMapping(path = ProdutoUri.LISTAR_POR_FILTRO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Produto>> getProdutoPorFiltro(@RequestParam(name = "q", required = false) String q,
                                                         @RequestParam(name = "min_price", required = false) Double minPrice,
                                                         @RequestParam(name = "max_price", required = false) Double maxPrice) {
        log.info("ProdutosController: Buscando produts conforme parametros...");
        return new ResponseEntity<>(service.buscaProdutosPorFiltro(q, minPrice, maxPrice), HttpStatus.OK);
    }

    /**
     * {@code DELETE /products/{id}} : Rest Endpoint de deleção de um {@link Produto} dado seu ID
     * @param produtoId passado no URL da requisição
     * @return the {@link ResponseEntity} com o status {@code 201 (OK)} e a entidade {@link DetalheRespostaDTO} criada
     * */
    @DeleteMapping(path = ProdutoUri.DELETA_PRODUTO + "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheRespostaDTO> deleteProduto(@PathVariable("id") String produtoId){
        log.info("ProdutosController: Buscando produto do ID - " + produtoId + "...");
        return new ResponseEntity<>(service.deletarProduto(produtoId), HttpStatus.OK);
    }

}
