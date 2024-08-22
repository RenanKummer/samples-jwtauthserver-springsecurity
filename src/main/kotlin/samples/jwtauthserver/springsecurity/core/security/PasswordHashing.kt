package samples.jwtauthserver.springsecurity.core.security

/**
 * Represents a password encoder service.
 */
interface PasswordHashing {
    val algorithm: String

    /**
     * Applies this encoder's hashing algorithm to [password].
     *
     * @param password The plain-text password.
     * @return The hashed password.
     */
    fun hash(password: String): String

    /**
     * Checks if [password] matches the [hashedPassword] with this encoder's algorithm.
     *
     * @param password The plain-text password.
     * @param hashedPassword The hashed password.
     * @return `true` if passwords match, `false` otherwise.
     */
    fun matches(password: String, hashedPassword: String): Boolean
}
