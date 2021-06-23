package br.com.yagovcb.productms.service.dto;

import lombok.Data;
import br.com.yagovcb.productms.model.Produto;

/**
 *  Classe DTO para padronização das requisições REST
 *  quando houvesse tivesse a necessidade de usar a entidade {@link Produto}
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Data
public class ProdutoDTO {

    private String id;

    private String name;

    private String description;

    private double price;
}
