package com.myportifolio.algamoneyapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@EnableWebSecurity
@Configuration
public class ResourceServerSecurityConfig {

    @Bean
    protected InMemoryUserDetailsManager userDetailsService() {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var user = User.withUsername("admin")
                .password(encoder.encode("admin"))
                .roles("ROLE")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    protected SecurityFilterChain filterChain(AuthenticationManagerBuilder authenticationManagerBuilder,HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                 .authorizeHttpRequests()
                    .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/categoria").permitAll()
                                .anyRequest().authenticated()

                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
