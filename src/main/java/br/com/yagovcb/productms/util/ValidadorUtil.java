package br.com.yagovcb.productms.util;

import br.com.yagovcb.productms.model.Produto;
import br.com.yagovcb.productms.service.exception.BadRequestException;
import java.util.Locale;
import java.util.Objects;

/**
 *  Classe de Métodos auxiliares
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
public class ValidadorUtil {

    /**
     * Construtor padrão vazio
     *
     * */
    private ValidadorUtil(){}

    /**
     * Método auxiliar responsavel por concentrar as requisições para validação por parte do service
     *
     * @param p entidade produto que será verificada
     * @throws BadRequestException caso algum atributo esteja com o tipo incorreto
     * @throws NullPointerException caso algum atributo seja nulo
     * @return true se a entidade {@link Produto} estiver de acordo com as regras propostas
     * */
    public static boolean validaProduto(Produto p) throws NullPointerException, BadRequestException {
        verificaNulo(p);
        verificaTipoCorretoParametro(p);
        return true;
    }

    /**
     * Método auxiliar responsavel por verificar se algum atributo da entidade {@link Produto} está com o tipo incorreto
     * do aceitavel
     * @param p entidade produto que será verificada
     * @throws BadRequestException caso algum atributo esteja com o tipo incorreto
     * */
    private static void verificaTipoCorretoParametro(Produto p) {
        if ( !(p.getDescription().toUpperCase(Locale.ROOT).substring(0, 3).matches("[A-Z]*")) ){
            throw new BadRequestException("Você passou um caracter para o atributo 'description' diferente do permitido, são aceitos somente letras de A à Z");
        }
        if (!(p.getName().toUpperCase(Locale.ROOT).substring(0, 3).matches("[A-Z]*"))){
            throw new BadRequestException("Você passou um caracter para o atributo 'name' diferente do permitido, são aceitos somente letras de A à Z");
        }

        if (p.getPrice() < 0) {
            throw new BadRequestException("Você passou um valor menor que zero para o atributo 'price', só é permitido valores positivos");
        }
    }

    /**
     * Método auxiliar responsavel por verificar se algum atributo da entidade {@link Produto} está nulo
     * @param p entidade produto que será verificada
     * @throws NullPointerException caso algum atributo seja nulo
     * */
    private static void verificaNulo(Produto p) {
        if (Objects.isNull(p.getDescription()) ){
            throw new NullPointerException("O Atributo 'description' precisa ser preenchido!");
        }
        if (Objects.isNull((p.getName())) ){
            throw new NullPointerException("O Atributo 'name' precisa ser preenchido!");
        }
        if (Objects.isNull(p.getPrice())){
            throw new NullPointerException("O Atributo 'price' precisa ser preenchido!");
        }
    }

}
