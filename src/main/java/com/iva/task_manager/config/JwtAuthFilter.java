package com.iva.task_manager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try{
                String username = jwtService.extractUsername(token);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username,null, null);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception e){

            }
        }
        filterChain.doFilter(request, response);
    }

}
/*OncePerRequestFilter - spring klasa napravljena bas za ovo
* "izvrsi se tacno jednom, za svaki zahtev, pre nego sto stigne do kontrolera.
* token ne stize u JSON telu zahteva, nego u posebnom header-u (deo zahteva odvojen od tela,
* "kao koverta oko pisma). Standardna konvencija treba da izgleda ovako Authorizaton : Bearer <token>
authHeader.startsWith("Bearer ") proveri da li header psotoji i ima tacno ovaj format, ako korisnik nije poslao
* token, ovaj blok se preskoci,
* jwtService.extractUsername(token) - koristi metodu koju sam napravila ranije, koja i
* proverava potpis (ako je token falsifikovan/istekao, baciće grešku) i vraća username ako je sve u redu.
* SecurityContextHolder.getContext().setAuthentication(...) kaze spring secruity-ju ovaj zahtev je sad
* zvanicno prijavljen kao taj korisnik - od ovog trenutka ostatak aplikacije zna ko salje zahtev*/