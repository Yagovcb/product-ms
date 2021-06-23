package br.com.yagovcb.productms.service.exception;

/**
 *  Classe que faz o apontamento para a {@link Exception} personalizada {@link NoSuchElementException}
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
public class NoSuchElementException extends RuntimeException{


    /**
     * Método que chama o construtor padrão de RuntimeException
     * @param mensagem a ser exibida
     * */
    public NoSuchElementException(String mensagem) {
        super(mensagem);
    }
}
