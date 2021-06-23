package br.com.yagovcb.productms.service;

import br.com.yagovcb.productms.mock.ProdutoMock;
import br.com.yagovcb.productms.model.Produto;
import br.com.yagovcb.productms.repository.ProdutoRepository;
import br.com.yagovcb.productms.service.dto.DetalheRespostaDTO;
import br.com.yagovcb.productms.service.dto.ProdutoDTO;
import br.com.yagovcb.productms.service.exception.MethodNotAllowedException;
import br.com.yagovcb.productms.service.exception.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ProdutoServiceTest {

    @Autowired
    private ProdutoService service;

    @MockBean
    private ProdutoRepository repository;


    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    public void setup() {
        this.service = new ProdutoService(repository);
        BDDMockito.given(this.repository.save(Mockito.any(Produto.class))).willReturn(new Produto());
        BDDMockito.given(this.repository.findById(Mockito.any())).willReturn(Optional.of(new Produto()));


        Pageable pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")
        ));
        when(repository.findAll(pageable)).thenReturn(ProdutoMock.createProdutos());

        when(repository.listarProdutosPorFiltro("produtos", 10.0, 100.0)).thenReturn(ProdutoMock.createProdutos().getContent());
    }

    @Test
    @Transactional
    @DisplayName("Teste de salvar um Produto")
    void salvarProdutoTest() {
        ProdutoDTO produtoDTO = ProdutoMock.getProdutoDTO();
        Produto p = this.service.salvarProduto(produtoDTO);

        assertNotNull(produtoDTO);
        assertNotNull(p);

        when(this.service.salvarProduto(produtoDTO)).thenThrow(MethodNotAllowedException.class);

        assertThrows(MethodNotAllowedException.class, () -> this.service.salvarProduto(produtoDTO));
    }

    @Test
    @DisplayName("Teste de retorno de todos os produtos")
    void getTodosProdutosTest() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")
        ));
        Page<Produto> produtos = service.getProdutos(pageable);
        assertEquals(produtos.getTotalPages(), 1);
        assertEquals(produtos.getNumberOfElements(), 3);
        assertNotNull(produtos);
    }

    @Test
    @Transactional
    @DisplayName("Teste retorna produto por ID especifico")
    void getProdutoPorIdTest() {
        salvarProdutoTest();
        Produto p = this.service.getProdutoPorId("1");
        assertNotNull(p);
        when(this.service.getProdutoPorId("5")).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> this.service.getProdutoPorId("5"));
    }


    @Test
    @Transactional
    @DisplayName("Teste atualiza produto")
    void atualizaProdutoTest(){
        salvarProdutoTest();
        Produto p = this.service.getProdutoPorId("1");
        assertNotNull(p);
        ProdutoDTO dto = ProdutoMock.getProdutoDTO("1", "produto 2", "descricao 2", 77.7);
        Produto pAtualizado = this.service.atualizaProduto(dto, p.getId());
        assertEquals(p.getId(), pAtualizado.getId());
        assertEquals(dto.getDescription(), pAtualizado.getDescription());

        when(this.service.atualizaProduto(dto, "5")).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> this.service.atualizaProduto(dto, "5"));

    }

    @Test
    @Transactional
    @DisplayName("Teste deletar produto")
    void deletarProdutoTest(){
        salvarProdutoTest();
        Produto p = this.service.getProdutoPorId("1");
        assertNotNull(p);

        DetalheRespostaDTO resposta = this.service.deletarProduto(p.getId());
        assertEquals(200L, resposta.getStatus());

        when(this.service.deletarProduto("50")).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> this.service.deletarProduto("50"));

    }

    @Test
    @Transactional
    @DisplayName("Teste buscar produtos pelo filtro tendo retorno vazio")
    void buscarProdutoPorFiltroRetornoVazioTest(){
        List<Produto> produtoList = service.getProdutos(PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")
        ))).getContent();

        assertNotNull(produtoList);
        when(this.repository.listarProdutosPorFiltro(null, 29.99, null)).thenReturn(produtoList);

        assertThrows(NoSuchElementException.class, () -> this.service.buscaProdutosPorFiltro(any(), anyDouble(), anyDouble()));
    }

}
