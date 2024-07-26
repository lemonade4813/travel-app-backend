//package com.example.travelappbackend.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.Map;
//
//@Service
//public class JwtService {
//
//    private static String secretKey = "javaSpringBootJWTTokenIssueMethod";
//
//    public String create(Map<String, Object> claims, LocalDateTime expiredAt) {
//        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
//        var _expiredAt = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());
//
//        return Jwts.builder().signWith(key, SignatureAlgorithm.HS256)
//                .setClaims(claims)
//                .setExpiration(_expiredAt)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String extractUsername(String token) {
//        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
//        return claims.getSubject();
//    }
//}
