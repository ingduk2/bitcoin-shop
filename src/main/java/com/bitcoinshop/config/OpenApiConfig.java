package com.bitcoinshop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info().title("BitcoinShop").version(appVersion)
                .description("spring boot를 이용한 사이트입니다.")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact().name("ingduk2").url("https://github.com/ingduk2").email("ingduk2@gmail.com"))
                .license(new License().name("licence").url("url"));

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
