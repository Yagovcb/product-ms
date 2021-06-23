package br.com.yagovcb.productms.config;

import br.com.yagovcb.productms.repository.ProdutoRepository;
import br.com.yagovcb.productms.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ProdutoServiceTestConfiguration {

    @Autowired
    private ProdutoRepository repository;

    @Bean
    public ProdutoService produtoService(){
        return new ProdutoService(repository);
    }
}
