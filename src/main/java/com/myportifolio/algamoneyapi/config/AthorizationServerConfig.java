package com.myportifolio.algamoneyapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.UUID;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AthorizationServerConfig {
    @Autowired
    protected AuthenticationManager authenticationManager;

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("angular_api_consumer")
                .secret("!@ngul@r_4PI!")
                .scopes("read", "write")
                .authorizedGrantTypes("password")
                .accessTokenValiditySeconds(1800);
    }

    @Bean
    public SecurityFilterChain securityAsFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        return http.formLogin().and().build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        var clientScopes = new HashSet<String>();
        clientScopes.add("read");
        clientScopes.add("write");
        var registeredClientBuilder = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("angular_client")
                .clientSecret("!@ngul@ar!")
                .scope(OidcScopes.OPENID)
                .clientAuthenticationMethod(Cl);

    }
}
