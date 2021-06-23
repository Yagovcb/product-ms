package br.com.yagovcb.productms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

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
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Gerenciamento de produtos API",
                "API para gerenciamento de produtos",
                "API Versão 1",
                "Terms of service",
                new Contact("Yago Castelo Branco", "https://github.com/Yagovcb/product-ms", "yago.vcb@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
