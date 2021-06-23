package br.com.yagovcb.productms.handler;

import br.com.yagovcb.productms.service.dto.DetalheRespostaDTO;
import br.com.yagovcb.productms.service.exception.BadRequestException;
import br.com.yagovcb.productms.service.exception.MethodNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

/**
 *  Classe de padrõnização das {@link Exception} dado seu tipo
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@ControllerAdvice
public class ProdutoExceptionHandler {

    /**
     * Método responsavel por tratar a excessão quando ocorrer
     *
     * @param bad tipo da excessão
     * @param request requisição feita
     *
     * @return retorna a excessão em formato ResponseEntity
     * */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DetalheRespostaDTO> handleBadRequestException(BadRequestException bad, HttpServletRequest request) {
        var erro = new DetalheRespostaDTO();
        erro.setStatus(400L);
        erro.setMensagem(bad.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    /**
     * Método responsavel por tratar a excessão quando ocorrer
     *
     * @param notfound tipo da excessão
     * @param request requisição feita
     *
     * @return retorna a excessão em formato ResponseEntity
     * */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<DetalheRespostaDTO> handleNoSuchElementException(NoSuchElementException notfound, HttpServletRequest request) {
        var erro = new DetalheRespostaDTO();
        erro.setStatus(404L);
        erro.setMensagem(notfound.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    /**
     * Método responsavel por tratar a excessão quando ocorrer
     *
     * @param notfound tipo da excessão
     * @param request requisição feita
     *
     * @return retorna a excessão em formato ResponseEntity
     * */
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<DetalheRespostaDTO> handleMethodNotAllowedException(MethodNotAllowedException notfound, HttpServletRequest request) {
        var erro = new DetalheRespostaDTO();
        erro.setStatus(420L);
        erro.setMensagem(notfound.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(erro);
    }
}
