package com.brianvi.spring_auth.security.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// handles JWT operations: creation, signing, reading, validation
@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // overload helper for token without extra claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // generates token for the user with extra claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // helper that applies the resolver on the token to extract any
    // claim (subject, expiration, etc.) from the token
    public<T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    // verifies token using secret key. if token was tampered with
    // then return exception. otherwise, decodes full set of claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                // start builder chain
                .builder()
                // add custom data
                .setClaims(extraClaims)
                // sets the username
                .setSubject(userDetails.getUsername())
                // records when token was created
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // sets expiration
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // signs with secret key using HS256
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                // returns the encoded JWT string
                .compact();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // converts secret key into something usable for JJWT.
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // ensures key is the right length
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
