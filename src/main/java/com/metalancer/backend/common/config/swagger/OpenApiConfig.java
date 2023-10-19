package com.metalancer.backend.common.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
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
            .description(
                "1. 대부분의 api는 @AuthenticationPrincipal PrincipalDetails user(세션)로 유저를 식별합니다. \n\n "
                    + " 2. 그리고 페이징 사용 시, 페이지를 1부터 시작하게끔 설정했습니다. \n\n "
                    + " 3. 현재 에셋과 상품을 혼용해서 사용하고 있습니다. \n\n "
                    + " 4. 이메일 로그인 -> 홈 조회 -> 에세 상세 조회 -> 구매하기/장바구니 담기 -> 결제하기(주문서 만들기, 포트원 토큰 발행 -> 모듈 결제처리 완료 \n\n"
            )
            .contact(new Contact().name("메타오비스").email("metaovis@gmail.com"));

        return new OpenAPI()
            .info(info).addServersItem(new Server().url("/"))
            .components(new Components().addSecuritySchemes("basicScheme",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));
    }

    @Bean
    public GroupedOpenApi group1() {
        return GroupedOpenApi.builder()
            .group("유저")
            .pathsToMatch("/api/users/**", "/api/auth/**", "/api/cart/**")
            .build();
    }

//    @Bean
//    public GroupedOpenApi group2() {
//        return GroupedOpenApi.builder()
//            .group("게시물")
//            .pathsToMatch("/api/posts/**")
//            .build();
//    }

    @Bean
    public GroupedOpenApi group3() {
        return GroupedOpenApi.builder()
            .group("상품(에셋)")
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

    @Bean
    public GroupedOpenApi group5() {
        return GroupedOpenApi.builder()
            .group("기타")
            .pathsToMatch("/api/interests/**")
            .build();
    }

    @Bean
    public GroupedOpenApi group6() {
        return GroupedOpenApi.builder()
            .group("크리에이터 전용")
            // creators -> creator 중
            .pathsToMatch("/api/creator/**")
            .build();
    }

    @Bean
    public GroupedOpenApi group7() {
        return GroupedOpenApi.builder()
            .group("카테고리")
            .pathsToMatch("/api/category/**")
            .build();
    }

    @Bean
    public GroupedOpenApi group8() {
        return GroupedOpenApi.builder()
            .group("제작요청")
            .pathsToMatch("/api/request/**")
            .build();
    }
}