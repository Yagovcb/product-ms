package br.com.yagovcb.productms.integration;

import br.com.yagovcb.productms.ProductMsApplication;
import br.com.yagovcb.productms.mock.ProdutoMock;
import br.com.yagovcb.productms.service.dto.ProdutoDTO;
import br.com.yagovcb.productms.util.ProdutoUri;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = ProductMsApplication.class)
@DisplayName("ProdutosController Teste de Integração")
public class ProdutosControllerIT {

    @Autowired
    private MockMvc restProdutoMockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @DisplayName("Teste de API persistindo produto")
    public void postProdutoTest() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(
                post(ProdutoUri.CRIAR_PRODUTO).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
                .andExpect(jsonPath("$.price").value(dto.getPrice()));
    }

    @Test
    @Transactional
    @DisplayName("Teste de API persistindo produto que ja foi persistido")
    public void postProdutoJaPersistidoTest() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        ProdutoDTO dto2 = ProdutoMock.getProdutoDTO(null, "Name Teste", "Premio Teste", 99.99);
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    @Transactional
    @DisplayName("Teste de API recupera produto por id")
    public void getProdutoPorIdTest() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        restProdutoMockMvc.perform(get(ProdutoUri.BUSCA_POR_ID + "{id}", dto.getId()))
                .andExpect(status().isOk());

    }

    //Teste com erro
    @Test
    @DisplayName("Teste de API tenta recuperar um produto que não existe")
    public void getProdutoPorIdTest_NaoExiste404() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO("2", "name teste 2", "descricao teste 2", 7.09);
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        restProdutoMockMvc.perform(get(ProdutoUri.BUSCA_POR_ID + "{id}", dto.getId()))
                .andExpect(status().isOk());

        restProdutoMockMvc.perform(get(ProdutoUri.BUSCA_POR_ID + "{id}", "12")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());
    }


    //Teste com erro
    @Test
    @Transactional
    @DisplayName("Teste de API tenta atualizar um produto pelo ID")
    public void atualizaProdutoPorID() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        restProdutoMockMvc.perform(get(ProdutoUri.BUSCA_POR_ID + "{id}", dto.getId()))
                .andExpect(status().isOk()).andReturn().getResponse();


        ProdutoDTO dto2 = ProdutoMock.getProdutoDTO(null, "Name 2", "descricao 2", 11.98);
        restProdutoMockMvc.perform(put(ProdutoUri.ATUALIZAR_PRODUTO + "{id}", dto.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @Transactional
    @DisplayName("Teste de API tenta recuperar todos os produtos")
    public void getProdutosTest() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        restProdutoMockMvc.perform(get(ProdutoUri.LISTAR_TODOS))
                .andExpect(status().isOk());
    }
    @Test
    @Transactional
    @DisplayName("Teste de API tenta recuperar todos os produtos por Filtro determinado")
    public void getProdutosPorFiltroTest() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        restProdutoMockMvc.perform(get(ProdutoUri.LISTAR_POR_FILTRO))
                .andExpect(status().isOk());
    }

    //Teste com erro
    @Test
    @Transactional
    @DisplayName("Teste de API tenta recuperar todos os produtos por Filtro determinado erro not Found")
    public void getProdutosPorFiltroTest_ErroNotFound() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        restProdutoMockMvc.perform(get(ProdutoUri.LISTAR_POR_FILTRO).queryParam("min_price", "100.0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("Teste de API tenta deletar produto")
    public void deletarProdutoTest() throws Exception {
        ProdutoDTO dto = ProdutoMock.getProdutoDTO();
        restProdutoMockMvc.perform(post(ProdutoUri.CRIAR_PRODUTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        restProdutoMockMvc.perform(delete(ProdutoUri.DELETA_PRODUTO + "{id}", dto.getId()))
                .andExpect(status().isOk());
    }
}
