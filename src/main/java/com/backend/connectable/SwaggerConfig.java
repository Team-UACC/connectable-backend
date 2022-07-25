package com.backend.connectable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
            .ignoredParameterTypes(AuthenticationPrincipal.class)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.backend.connectable"))
            .paths(PathSelectors.ant("/**"))
            .build();
    }
}
