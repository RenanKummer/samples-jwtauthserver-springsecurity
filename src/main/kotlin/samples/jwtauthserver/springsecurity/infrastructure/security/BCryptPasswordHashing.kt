package samples.jwtauthserver.springsecurity.infrastructure.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import samples.jwtauthserver.springsecurity.core.security.PasswordHashing

@Service
class BCryptPasswordHashing(private val springPasswordEncoder: BCryptPasswordEncoder) : PasswordHashing {
    override val algorithm = "bcrypt"

    override fun hash(password: String) = springPasswordEncoder.encode(password)

    override fun matches(password: String, hashedPassword: String): Boolean {
        return springPasswordEncoder.matches(password, hashedPassword)
    }
}
