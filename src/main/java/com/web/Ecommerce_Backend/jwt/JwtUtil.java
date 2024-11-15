package com.web.Ecommerce_Backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {


    public static final String SECURITY= "czkuqw2U1GHO2HF2OXZgIMmS6115pEeVrC60wEJTwtM7kIe8gHKW2hpOw2WCxMY27cBmQzuV36sspi8ykau0eEr5wXEd27qf";

    public String createToken(String userName)
    {
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims,userName);
    }

    private String buildToken(Map<String,Object>claims,String userName)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey()
    {
        byte[] bytesKey = Decoders.BASE64.decode(SECURITY);
        return Keys.hmacShaKeyFor(bytesKey);
    }

    public String getUsernameFromToken(String token)
    {
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims,T>resolver)
    {
        final Claims claims = getAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims getAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token)
    {
        return getClaim(token,Claims::getExpiration);
    }

    private boolean isValidateToken(String token, UserDetails userDetails)
    {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
}
