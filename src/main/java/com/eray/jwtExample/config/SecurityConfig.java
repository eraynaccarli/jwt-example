package com.eray.jwtExample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()  // tum http istekleri icin bir yetkilendirme yaptik
                .requestMatchers("/login/**") // controller katmanindaki yolumuzun kararini veriyoruz login ile baslayacagiz
                .permitAll()
                .anyRequest().authenticated()       // login haric tum istekler icin yetkilendirme gerekli diyoruz
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // bir kullanicinin oturum actiktan sonra bir cookie ve cerez kullanmayacagimizi soyledik
                .and()
                .authenticationProvider(authenticationProvider)    // kimlik dogrulamayi hangi provider saglayacak onu verdik
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);    // kimlik dogrulama islemi icin hangi filtrelemeyi kullanacagiz

        return httpSecurity.build();

    }

}
