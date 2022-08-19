package com.backend.connectable.global.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.url}")
    private String baseUrl;

    @Bean
    public Docket parseApi() {
        return new Docket(DocumentationType.OAS_30)
            .ignoredParameterTypes(AuthenticationPrincipal.class)
            .servers(serverInfo())
            .apiInfo(apiInfo())
            .globalRequestParameters(parameters())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.backend.connectable"))
            .paths(PathSelectors.ant("/**"))
            .paths(Predicate.not(PathSelectors.regex("/admin.*")))
            .build();
    }

    private List<RequestParameter> parameters() {
        return singletonList(
            new RequestParameterBuilder()
                .name("Authorization")
                .description("Required for user-based requests")
                .in(ParameterType.HEADER)
                .required(false)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .build()
        );
    }

    private Server serverInfo() {
        return new Server("",
            baseUrl,
            "",
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Connectable API Documentation")
            .description("API documentation of Connectable project by Team UACC")
            .version("1.0.0")
            .build();
    }
}
