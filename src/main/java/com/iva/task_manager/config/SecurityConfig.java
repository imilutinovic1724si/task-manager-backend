package com.iva.task_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of(
                "http://localhost:5173",
                "https://task-manager-frontend-five-sable.vercel.app"
        ));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(java.util.List.of("*"));

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

        /*.cors(cors -> {}) govori Spring Security-ju "koristi CORS podešavanje",
         ali mu treba taj corsConfigurationSource bean da bi
          znao koje tačno podešavanje da primeni (koji origin je dozvoljen, koje metode).
          Bez tog bean-a, Spring je propuštao OPTIONS zahtev (jer smo to eksplicitno rekle),
          ali pravi GET odgovor nije nosio potrebne CORS header-e — otud "Access-Control-Allow-Origin" greška.*/
    }

}

/*@configuation kaze springu "ova klasa pravi gotove alate (bean-ove) koje
* drugi delovi aolikacije mogu da koriste
* @Bean kaze - metoda ispod pravi jedan gotov objekat koji spring cuva i deli
* svuda gde je potrebam isti princip kao @Autowired,
* ovo je "davalaac" a @Autowired je primalac
* BCryptPasswordEncoder je gotov industrijski standard algroitam za enkripciju lozinki. On uzme
* "test123"  i pretvroi u gomilu karaktwra. Nepovratan znaci da se ne moze dektiptovati nazad
*  u test124 moze samo da se proveri da li neka nova lozinka kad se enktiptuje daje isti rezultat
* SecurityFilterChain je centralno mesto gde definisem pravila ko sme sta da radi u aplikaciji
* .csrf(csrf -> csrf.disable()) -  CSRF zastita je bezbednosna mera napravljena za tradicionalne WEB sajtove sa formama i sesijama.
* Posto koristim rest api koji koristi JWT a ne sesije ova zastita mi ne treba
* .anyRequest. permit all za sada pusti bilo kij zahtev bez ikakve promene
* .requestMatchers(...) - samo rute koje pocinju sa /api/auth/ ostaju javne bez potrebe za tokenom
* sve ostalo (taskovi, projekti, korisnici) sad zahteva validan token
* addFilterBefore... kaze springu "pre nego sto uopsta razmislis o autentifikaciji prvo pusti moj filter da proveri token*/