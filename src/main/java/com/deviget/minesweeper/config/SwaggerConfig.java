package com.deviget.minesweeper.config;

import java.util.Arrays;
import java.util.List;

import com.deviget.minesweeper.dto.ApiError;
import com.fasterxml.classmate.TypeResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api(TypeResolver typeResolver) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.deviget.minesweeper")).paths(PathSelectors.any()).build()
                .apiInfo(getApiInfo());
        
        docket.useDefaultResponseMessages(false);

        docket.additionalModels(typeResolver.resolve(ApiError.class));

        ModelRef errorModel = new ModelRef("ApiError");

        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(500).message("Error").responseModel(errorModel).build());

        docket.globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages);
        return docket;

    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("MineSweeper API").description("MineSweeper API").version("0.1")
                .contact(new Contact("Axel Freiria", "https://github.com/axfrei/minesweeper-API", "")).build();
    }
}
