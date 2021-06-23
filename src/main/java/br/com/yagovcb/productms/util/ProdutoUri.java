package br.com.yagovcb.productms.util;

import org.springframework.stereotype.Component;

/**
 *  Classe de mapeamento das rotas das requisições REST
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Component
public class ProdutoUri {

    public static final String CRIAR_PRODUTO = "/products";
    public static final String ATUALIZAR_PRODUTO = "/products/";
    public static final String BUSCA_POR_ID = "/products/";
    public static final String LISTAR_TODOS= "/products";
    public static final String LISTAR_POR_FILTRO= "/products/search";
    public static final String DELETA_PRODUTO = "/products/";

    /**
     * Construtor padrão vazio
     *
     * */
    private ProdutoUri(){}

}
