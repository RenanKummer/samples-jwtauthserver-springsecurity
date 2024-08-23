package samples.jwtauthserver.springsecurity.infrastructure.helpers

import java.security.SecureRandom

fun main() {
    val secureRandom = SecureRandom()
    val keyBytes = ByteArray(64)
    secureRandom.nextBytes(keyBytes)

    val secretKey = keyBytes.joinToString("") { String.format("%02x", it) }
    println(secretKey)
}
