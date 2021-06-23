package br.com.yagovcb.productms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *  Classe de Configuração do Swagger
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.yagovcb.productms.controller"))
                .paths(PathSelectors.ant("/**"))
                .build();
    }
}
