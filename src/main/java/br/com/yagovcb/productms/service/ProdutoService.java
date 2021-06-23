package br.com.yagovcb.productms.service;

import br.com.yagovcb.productms.model.Produto;
import br.com.yagovcb.productms.repository.ProdutoRepository;
import br.com.yagovcb.productms.service.dto.DetalheRespostaDTO;
import br.com.yagovcb.productms.service.dto.ProdutoDTO;
import br.com.yagovcb.productms.service.exception.MethodNotAllowedException;
import br.com.yagovcb.productms.service.exception.NoSuchElementException;
import br.com.yagovcb.productms.util.ValidadorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *  {@link Service} controller para gerenciar as  ações do controller {@link br.com.yagovcb.productms.controller.ProdutoController}.
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProdutoService {

    private static final String RESPOSTA_LOG_PADRAO = "ProdutosController: Produto do ID - ";
    private static final String PRODUTO_NAO_ENCONTRADO = "Nenhum produto encontrado";

    @Autowired
    private final ProdutoRepository repository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Método responsavel por retornar todos os produtos
     * @param pageable paginação informada
     * @return um {@link Page<Produto>} com todos os registros da entidade {@link Produto} criados
     * */
    @Transactional(readOnly = true)
    public Page<Produto> getProdutos(Pageable pageable){
        return repository.findAll(pageable);
    }

    /**
     * Método responsavel por retornar um {@link Produto} especifico, dado seu id
     *
     * @param id do {@link Produto} que está se buscando
     *
     * @throws NoSuchElementException caso não encontre o produto
     * @return um {@link Produto} especifico
     * */
    @Transactional(readOnly = true)
    public Produto getProdutoPorId(String id) throws NoSuchElementException{
        try {
            Optional<Produto> produto = repository.findById(id);
            return produto.isPresent() ? produto.get() : null;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(PRODUTO_NAO_ENCONTRADO);
        }finally {
            log.info(RESPOSTA_LOG_PADRAO + id + " foi encontrado");
        }
    }

    /**
     * Método responsavel por salvar um {@link Produto} especifico, dado seu {@link ProdutoDTO}
     *
     * @param dto entidade {@link ProdutoDTO} que será persistida
     *
     * @throws MethodNotAllowedException Caso não seja possivel salvar o produto
     * @return um {@link Produto} já persistido no banco
     * */
    @Transactional
    public Produto salvarProduto(ProdutoDTO dto) throws MethodNotAllowedException{
        Produto p = modelMapper.map(dto, Produto.class);
        try{
            return (ValidadorUtil.validaProduto(p) && !verificaExistencia(p)) ? repository.save(p) : null;
        } catch (MethodNotAllowedException e) {
            throw new MethodNotAllowedException("Não foi possivel salvar o produto, erro: " + e);
        } finally {
            log.info( "ProdutosController: Produto criado" + p );
        }
    }

    /**
     * Método responsavel por atualizar um {@link Produto} dado seu ID, recebendo as informações que serão atualizadas
     *  mediante a entidade {@link ProdutoDTO}
     *
     * @param produtoDTO entidade {@link ProdutoDTO} que será atualizada
     * @param produtoId do produto antigo para fazer a pesquisa
     *
     * @throws NoSuchElementException caso não encontre o produto
     * @return o {@link Produto} atualizado
     * */
    @Transactional
    public Produto atualizaProduto(ProdutoDTO produtoDTO, String produtoId) throws NoSuchElementException{
        var pAntigo = getProdutoPorId(produtoId);
        try {
            Produto pNovo = modelMapper.map(produtoDTO, Produto.class);
            if (ValidadorUtil.validaProduto(pNovo)) {
                pAntigo.setName(pNovo.getName());
                pAntigo.setDescription(pNovo.getDescription());
                pAntigo.setPrice(pNovo.getPrice());
                repository.save(pAntigo);
            }
            return pAntigo;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(PRODUTO_NAO_ENCONTRADO);
        }finally {
            log.info(RESPOSTA_LOG_PADRAO + pAntigo.getId() + " foi atualizado");
        }
    }

    /**
     * Método responsavel por deletar um {@link Produto} dado seu id
     *
     * @param produtoId do {@link Produto} que será deletado
     *
     * @throws NoSuchElementException caso não encontre o produto
     * @return o {@link DetalheRespostaDTO} informando que o produto foi deletado
     * */
    @Transactional
    public DetalheRespostaDTO deletarProduto(String produtoId) throws NoSuchElementException{
        var p = getProdutoPorId(produtoId);
        try {
            if (Objects.nonNull(p)) {
                log.info("ProdutosController: Deletando produto do ID - " + produtoId + "...");
                repository.delete(p);
            }
            return new DetalheRespostaDTO("Produto deletado com sucesso", 200L);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(PRODUTO_NAO_ENCONTRADO);
        }finally {
            log.info(RESPOSTA_LOG_PADRAO + produtoId + " foi deletado com sucesso");
        }
    }

    /**
     * Método responsavel por Retornar uma {@link List<Produto>} com base no parametros que serão utilizados no filtro
     *
     * @param q  representa o atributo Name da entidade {@link Produto}, podendo ser null
     * @param maxPrice representa o maior valor do atributo preco da entidade {@link Produto}, podendo ser null
     * @param minPrice  representa o menor valor do atributo preco da entidade {@link Produto}, podendo ser null
     *
     * @throws NoSuchElementException caso não encontre o produto
     * @return uma {@link List<Produto>} todos os produtos que se encaixem no filtro
     * */
    @Transactional
    public List<Produto> buscaProdutosPorFiltro(String q, Double minPrice, Double maxPrice) throws NoSuchElementException{
        List<Produto> produtoList = null;
        try {
            if (!verificaRetornoConsultaVazio(q, minPrice, maxPrice)){
                produtoList = repository.listarProdutosPorFiltro(q, minPrice, maxPrice);
                return produtoList;
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(PRODUTO_NAO_ENCONTRADO);
        } finally {
            log.info("ProdutosController: Produtos encontrados { " + produtoList + " }");
        }
        return null;
    }

    /**
     * Método responsavel por verificar se um {@link Produto} existe
     * @param p entidade {@link Produto} que será verificada
     * @throws MethodNotAllowedException caso o {@link Produto} ja esteja cadastrado
     * @return false se não existir na base
     * */
    private boolean verificaExistencia(Produto p) throws MethodNotAllowedException{
        if (Objects.nonNull(repository.findByNameOrDescription(p.getName(), p.getDescription()))) {
            throw new MethodNotAllowedException("Produto Ja cadastrado na base");
        } else {
            return false;
        }
    }

    /**
     * Método responsavel por verificar se a consulta retorna alguma coisa
     * @param q  representa o atributo Name da entidade {@link Produto}, podendo ser null
     * @param maxPrice representa o maior valor do atributo preco da entidade {@link Produto}, podendo ser null
     * @param minPrice  representa o menor valor do atributo preco da entidade {@link Produto}, podendo ser null
     * @throws NoSuchElementException caso não haja retorno da consulta
     * @return false se retornar algo
     * */
    private boolean verificaRetornoConsultaVazio(String q, Double minPrice, Double maxPrice) throws MethodNotAllowedException{
        if (repository.listarProdutosPorFiltro(q, minPrice, maxPrice).isEmpty()) {
            throw new NoSuchElementException(PRODUTO_NAO_ENCONTRADO);
        } else {
            return false;
        }
    }
}
