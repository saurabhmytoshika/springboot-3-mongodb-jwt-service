package com.mongodb.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {

    private final val secretKey = "5468576D5A7134743777217A25432A462D4A404E635266556A586E3272357538"

    fun extractUserName(token: String): String? {
        return extractClaims(token, Claims::getSubject)
    }

    fun <T> extractClaims(token: String, claimsResolver: java.util.function.Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()
        claims["ROLE"] = userDetails.authorities
        return generateToken(claims, userDetails)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() +1000*60*24))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isValidToken(token: String, userDetails: UserDetails): Boolean {
        val userName = extractUserName(token)
        return (userName == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaims(token, Claims::getExpiration)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}