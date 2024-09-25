package com.example.travelappbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {



    private static String secretKey = "javaSpringBootJWTTokenIssueMethod";

    public String create(Map<String, Object> claims, LocalDateTime expiredAt, String userId) {
        try {

            var key = Keys.hmacShaKeyFor(secretKey.getBytes());
            var _expiredAt = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

            return Jwts.builder().signWith(key, SignatureAlgorithm.HS256)
                    .setClaims(claims)
                    .setSubject(userId)
                    .setExpiration(_expiredAt)
                    .compact();
        }
            catch(Exception e){
                e.printStackTrace();
                throw new RuntimeException("Failed to create JWT", e);
            }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {

            e.printStackTrace();
            System.out.println("JWT expired: " + e.getMessage());
            throw e;  // 만료된 토큰 예외를 다시 던져 필터에서 처리
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String extractSubject(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
