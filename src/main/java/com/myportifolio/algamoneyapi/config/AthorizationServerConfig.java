package com.myportifolio.algamoneyapi.config;

import com.myportifolio.algamoneyapi.config.keys.JWKsKeys;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.HashSet;
import java.util.UUID;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AthorizationServerConfig {
//    @Autowired
//    protected AuthenticationManager authenticationManager;

//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("angular_api_consumer")
//                .secret("!@ngul@r_4PI!")
//                .scopes("read", "write")
//                .authorizedGrantTypes("password")
//                .accessTokenValiditySeconds(1800);
//    }

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
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                //.redirectUri("client_uri")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(20))
                        .refreshTokenTimeToLive(Duration.ofMinutes(20))
                        .build()
                )
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build()
                )
                .build();

        return new InMemoryRegisteredClientRepository(registeredClientBuilder);

    }
    @Bean
    public ProviderSettings getProviderSettings() {
        return ProviderSettings.builder().build();
    }

    @Bean
    public JWKSource<SecurityContext> getJWKJwkSource() {
        var rsaKey = JWKsKeys.generate();
         var set = new JWKSet(rsaKey);

         return (j, sc) -> j.select(set);
    }
}
