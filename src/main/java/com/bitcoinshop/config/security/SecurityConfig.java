
package com.bitcoinshop.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/", "/api/v1/**", "/test/**").permitAll()
                .anyRequest().authenticated();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        web.ignoring().antMatchers("/swagger-ui.html",
                "/swagger-ui/**",
                "/api-docs",
                "/api-docs/**",
                "/h2-console/**",
                "/docs/index.html");
    }
}
