package samples.jwtauthserver.springsecurity.core.repositories

import samples.jwtauthserver.springsecurity.core.models.MutableUser
import samples.jwtauthserver.springsecurity.core.models.User

/**
 * Represents a repository of [User] entities.
 */
interface UserRepository {

    /**
     * Creates a user.
     *
     * @param user The user to create.
     * @return The created user if new, `null` if they already exist.
     */
    fun create(user: User): User?

    /**
     * Updates an existing user.
     *
     * @param value The value to filter for.
     * @param property The property to filter for.
     * @param data The user data to save.
     * @return `true` if successful, `false` otherwise.
     */
    fun update(value: String, property: UserFilterProperties, data: MutableUser): Boolean

    /**
     * Deletes an existing user with [value] matching user [property].
     *
     * @param value The value to filter user for.
     * @param property The property to filter for.
     * @return `true` if successful, `false` otherwise.
     */
    fun delete(value: String, property: UserFilterProperties): Boolean

    /**
     * Checks if a user exists with [value] matching user [property].
     *
     * @param value The value to filter for.
     * @param property The property to filter for.
     * @return `true` if found, `false` otherwise.
     */
    fun exists(value: String, property: UserFilterProperties = UserFilterProperties.USERNAME): Boolean

    /**
     * Fetches user by [value] matching user [property].
     *
     * @param value The value to filter for.
     * @param property The property to filter for.
     * @return The user if found, `null` otherwise.
     */
    fun findOne(value: String, property: UserFilterProperties): User?

    /**
     * Fetches all users.
     *
     * @return All users.
     */
    fun findAll(): List<User>
}

/**
 * Property names to filter users.
 */
enum class UserFilterProperties(val value: String) {
    USERNAME("username"),
    EMAIL("email");

    companion object {
        const val USERNAME_VALUE = "username"
        const val EMAIL_VALUE = "email"

        fun ofName(value: String) = when (value.lowercase()) {
            USERNAME_VALUE -> USERNAME
            EMAIL_VALUE -> EMAIL
            else -> null
        }
    }
}
