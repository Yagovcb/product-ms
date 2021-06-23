package br.com.yagovcb.productms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Classe DTO para padronização das Respostas das requisições REST quando houvesse {@link Exception}
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalheRespostaDTO {

    private String mensagem;
    private Long status;
}
