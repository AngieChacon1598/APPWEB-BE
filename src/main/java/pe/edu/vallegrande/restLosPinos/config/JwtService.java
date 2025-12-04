package pe.edu.vallegrande.restLosPinos.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, expirationTime);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, expirationTime);
    }

    public String buildToken(Map<String, Object> claims, UserDetails userDetails, Long expirationTime) {
        // Agregar las authorities (roles) al token
        claims.put("authorities", userDetails.getAuthorities());
        
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean isvalidatetoken(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpirationtoken(token);
    }

    public boolean isExpirationtoken(String token) {
        return extraerDateExpiration(token).before(new Date());
    }

    public String extraerUsername(String token) {
        return getExtratedClaim(token, Claims::getSubject);
    }

    public Date extraerDateExpiration(String token) {
        return getExtratedClaim(token, Claims::getExpiration);
    }

    public Object extraerAuthorities(String token) {
        return getExtratedClaim(token, claims -> claims.get("authorities"));
    }

    @SuppressWarnings("unchecked")
    public Collection<? extends GrantedAuthority> extraerAuthoritiesAsGrantedAuthorities(String token) {
        Object authoritiesObj = extraerAuthorities(token);
        
        if (authoritiesObj == null) {
            return Collections.emptyList();
        }
        
        // Si es una lista de mapas (formato de Spring Security)
        if (authoritiesObj instanceof List) {
            List<?> authoritiesList = (List<?>) authoritiesObj;
            return authoritiesList.stream()
                    .map(auth -> {
                        if (auth instanceof Map) {
                            Map<String, Object> authMap = (Map<String, Object>) auth;
                            String authority = (String) authMap.get("authority");
                            return new SimpleGrantedAuthority(authority);
                        } else if (auth instanceof String) {
                            return new SimpleGrantedAuthority((String) auth);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        
        return Collections.emptyList();
    }

    public <T> T getExtratedClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getExtratedClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims getExtratedClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
