package br.com.yagovcb.productms.mock;

import br.com.yagovcb.productms.model.Produto;
import br.com.yagovcb.productms.service.dto.ProdutoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.ArrayList;
import java.util.List;

public class ProdutoMock {

    public static Page<Produto> createProdutos(){
        List<Produto> produtoList = new ArrayList<>();
        Produto p = new Produto();
        p.setId("1");
        p.setName("produto 1");
        p.setDescription("Descrição produto 1");
        p.setPrice(19.99);
        Produto p2 = new Produto();
        p2.setId("2");
        p2.setName("produto 2");
        p2.setDescription("Descrição produto 2");
        p2.setPrice(29.99);
        Produto p3 = new Produto();
        p3.setId("3");
        p3.setName("produto 3");
        p3.setDescription("Descrição produto 3");
        p3.setPrice(39.99);

        produtoList.add(p);
        produtoList.add(p2);
        produtoList.add(p3);
        Page<Produto> pagedResponse = new PageImpl(produtoList);
        return pagedResponse;
    }

    public static ProdutoDTO getProdutoDTO() {
        return getProdutoDTO("1", "Name Teste", "Premio Teste", 99.99);
    }

    public static ProdutoDTO getProdutoDTO(String id, String name, String descricao, double preco) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(descricao);
        dto.setPrice(preco);
        return dto;
    }
}
