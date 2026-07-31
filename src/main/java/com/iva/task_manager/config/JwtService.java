package com.iva.task_manager.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import  javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    /*tajni kljuc kojim se svaki token potpisuje. samo ga server zna zato niko spolja ne moze
    * da falsifikuje token, cak iako zna kako JWT izgleda*/
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() *1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }
    /*pravi token koji unutra nosi: subject (korisnicko ime), issusedAt (kad je napravljen),
    * expiration (kad istice), i potpisan je kljucem*/
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    /*kasnije kad neko posalje token nazad, ova metoda ga "otvori" i procita
    * cije je korisnicko ime unutra - ali radi to samo ako je potpis validan*/
}
