package com.eray.jwtExample.service;

import com.eray.jwtExample.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;
    public String findEmail(String token) {
        // gelen tokenin export bolununden Claims diyerek gelen nesneyi cekerek emaili bulduk
        return exportToken(token, Claims::getSubject);
    }

    // Jwt den gelen tum degerleri yakalamamizi saglayacak
    private <T> T exportToken(String token, Function<Claims,T> claimsTFunction) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody(); // claims nesnemizi parse ettik
        return claimsTFunction.apply(claims); // gelen fonksiyonun degerini dondurduk
    }

    private Key getKey(){
        byte[] key = Decoders.BASE64.decode(SECRET_KEY); // benzersiz bir anahtar vermesi icin base64 ile decode ettik
        return Keys.hmacShaKeyFor(key);
    }

    // jwt nin gecerli olup olmadigini kontrol eden method
    public boolean tokenControl(String jwt, UserDetails userDetails) {
        final String email = findEmail(jwt);
        // token in hem username bilgisini hem de Claims::getExpiration diyerek gecerli bir token mi degil mi ona baktık (suandan once mi diye baktik)
        return (email.equals(userDetails.getUsername()) && !exportToken(jwt,Claims::getExpiration).before(new Date()));
    }

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // olusturulma zamanini verdik
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // gecerlilik suresini verdik 1 gun verdik
                .signWith(getKey(), SignatureAlgorithm.HS256)  // sifrelenmesini sagladik
                .compact(); // token generate ettik
    }


}

