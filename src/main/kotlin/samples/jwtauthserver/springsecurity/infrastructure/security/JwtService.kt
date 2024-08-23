package samples.jwtauthserver.springsecurity.infrastructure.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${security.jwt.secret-key}")
    private val secretKey: String,

    @Value("\${security.jwt.expiration-time}")
    private val expirationTime: Long
) {
    fun extractUsername(token: String) = extractClaim(token, Claims::getSubject)

    fun <T> extractClaim(token: String, resolver: Claims.() -> T): T {
        val claims = extractAllClaims(token)
        return resolver.invoke(claims)
    }

    fun generateToken(userDetails: UserDetails) = generateToken(mapOf(), userDetails)

    fun generateToken(extraClaims: Map<String, Any?>, userDetails: UserDetails): String {
        return buildToken(extraClaims, userDetails, expirationTime)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    fun getExpirationTime() = expirationTime

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())

    private fun extractExpiration(token: String) = extractClaim(token, Claims::getExpiration)

    private fun buildToken(extraClaims: Map<String, Any?>, userDetails: UserDetails, expirationTime: Long): String {
        val now = System.currentTimeMillis()

        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date(now))
            .expiration(Date(now + expirationTime))
            .signWith(getSignInKey(), Jwts.SIG.HS512)
            .compact()
    }

    private fun extractAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(getSignInKey())
            .build()

        return parser.parseSignedClaims(token).payload
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes = Base64.getDecoder().decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
