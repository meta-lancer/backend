package com.metalancer.backend.common.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
            .version("v1.0.0")
            .title("Metaovis API")
            .description("API Description")
            .contact(new Contact().name("메타오비스").email("metaovis@gmail.com"));

        return new OpenAPI()
            .info(info);
    }

    @Bean
    public GroupedOpenApi group1() {
        return GroupedOpenApi.builder()
            .group("유저")
            .pathsToMatch("/api/users/**")
            .build();
    }

    @Bean
    public GroupedOpenApi group2() {
        return GroupedOpenApi.builder()
            .group("게시물")
            .pathsToMatch("/api/posts/**")
            .build();
    }

    @Bean
    public GroupedOpenApi group3() {
        return GroupedOpenApi.builder()
            .group("상품")
            .pathsToMatch("/api/products/**")
            .build();
    }

    @Bean
    public GroupedOpenApi group4() {
        return GroupedOpenApi.builder()
            .group("주문")
            .pathsToMatch("/api/orders/**")
            .build();
    }


}