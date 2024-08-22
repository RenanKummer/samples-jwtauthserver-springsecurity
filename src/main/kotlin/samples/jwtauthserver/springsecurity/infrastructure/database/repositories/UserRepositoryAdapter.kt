package samples.jwtauthserver.springsecurity.infrastructure.database.repositories

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ISqlExpressionBuilder
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import samples.jwtauthserver.springsecurity.core.extensions.logError
import samples.jwtauthserver.springsecurity.core.extensions.logInfo
import samples.jwtauthserver.springsecurity.core.extensions.logWarn
import samples.jwtauthserver.springsecurity.core.models.MutableUser
import samples.jwtauthserver.springsecurity.core.models.User
import samples.jwtauthserver.springsecurity.core.repositories.UserFilterProperties
import samples.jwtauthserver.springsecurity.core.repositories.UserRepository
import samples.jwtauthserver.springsecurity.core.security.PasswordHashing
import samples.jwtauthserver.springsecurity.infrastructure.database.entities.Users
import java.time.LocalDate

@Repository
@Transactional
class UserRepositoryAdapter(private val passwordHashing: PasswordHashing) : UserRepository {

    @Value("\${security.password-expiration-days}")
    private val passwordExpirationDays: Long = 1

    override fun create(user: User): User? {
        logger.logInfo("Creating a new user...", "user" to user)

        try {
            Users.insert { it mapUser user withPassword user.password }
            logger.logInfo("Successfully created new user", "user" to user)
            return user
        } catch (ex: ExposedSQLException) {
            logger.logError("Cannot create user", ex, "user" to user)
        }

        return null
    }

    override fun update(value: String, property: UserFilterProperties, user: MutableUser): Boolean {
        logger.logInfo("Updating user...", "value" to value, "property" to property.name, "data" to user)

        try {
            val updatedCount = Users.update(where = { equalsIgnoreCase(value, property) }) {
                it mapUser user withPassword user.password
            }

            if (updatedCount == 0) {
                logger.logWarn("Cannot update user: not found", "value" to value, "property" to property.name)
                return false
            }
        } catch (ex: ExposedSQLException) {
            logger.logError("Cannot update user", ex, "value" to value, "property" to property.name)
            return false
        }

        logger.logInfo("Successfully updated user object", "value" to value, "property" to property.name)
        return true
    }

    override fun delete(value: String, property: UserFilterProperties): Boolean {
        logger.logInfo("Deleting user...", "value" to value, "property" to property.name)

        try {
            val deletedCount = Users.deleteWhere { it.equalsIgnoreCase(value, property) }

            if (deletedCount == 0) {
                logger.logWarn("Cannot delete user: not found", "value" to value, "property" to property.name)
                return false
            }
        } catch (ex: ExposedSQLException) {
            logger.logError("Cannot delete user", ex, "value" to value, "property" to property.name)
        }


        logger.logInfo("User deleted successfully", "value" to value, "property" to property.name)
        return true
    }

    override fun exists(value: String, property: UserFilterProperties): Boolean {
        return Users.selectAll().where { equalsIgnoreCase(value, property) }.count() > 0
    }

    override fun findOne(value: String, property: UserFilterProperties): User? {
        logger.logInfo("Fetching single user...", "value" to value, "property" to property.name)

        val user = Users.selectAll().where { equalsIgnoreCase(value, property) }.firstOrNull()?.toUser()

        if (user == null) {
            logger.logWarn("User not found", "value" to value, "property" to property.name)
            return null
        }

        logger.logInfo("Successfully found user", "user" to user)
        return user
    }

    override fun findAll(): List<User> {
        logger.logInfo("Fetching all users...")
        val users = Users.selectAll().map { it.toUser() }
        logger.logInfo("Successfully fetched all users.", "users" to users)

        return users
    }

    // TODO: Hashing should not be part of the transaction
    private infix fun UpdateBuilder<*>.withPassword(password: String?) {
        if (password != null) {
            this[Users.password] = passwordHashing.hash(password)
            this[Users.passwordHashAlgorithm] = passwordHashing.algorithm
            this[Users.passwordExpirationDate] = LocalDate.now().plusDays(passwordExpirationDays)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserRepositoryAdapter::class.java)
    }
}

private fun ISqlExpressionBuilder.equalsIgnoreCase(value: String, property: UserFilterProperties) = when (property) {
    UserFilterProperties.USERNAME -> Users.username.lowerCase() eq value.lowercase()
    UserFilterProperties.EMAIL -> Users.email.lowerCase() eq value.lowercase()
}

private infix fun InsertStatement<*>.mapUser(user: User): InsertStatement<*> {
    this[Users.username] = user.username.lowercase()
    this[Users.email] = user.email.lowercase()
    this[Users.firstName] = user.firstName
    this[Users.lastName] = user.lastName
    this[Users.phoneNumber] = user.phoneNumber
    this[Users.companyName] = user.companyName
    this[Users.createdBy] = user.createdBy.lowercase()
    this[Users.createdDateTime] = user.createdDateTime

    return this
}

private infix fun UpdateStatement.mapUser(user: MutableUser): UpdateStatement {
    this.setIfNotNull(Users.id, user.id)
    this.setIfNotNull(Users.username, user.username?.lowercase())
    this.setIfNotNull(Users.email, user.email?.lowercase())
    this.setIfNotNull(Users.firstName, user.firstName)
    this.setIfNotNull(Users.lastName, user.lastName)
    this.setIfNotNull(Users.phoneNumber, user.phoneNumber)
    this.setIfNotNull(Users.companyName, user.companyName)
    this.setIfNotNull(Users.createdBy, user.createdBy?.lowercase())
    this.setIfNotNull(Users.createdDateTime, user.createdDateTime)
    this.setIfNotNull(Users.updatedBy, user.updatedBy?.lowercase())
    this.setIfNotNull(Users.updatedDateTime, user.updatedDateTime)

    return this
}

private fun <T> UpdateStatement.setIfNotNull(column: Column<T>, value: T?) {
    if (value != null) {
        this[column] = value
    }
}

private fun <T : Comparable<T>> UpdateStatement.setIfNotNull(column: Column<EntityID<T>>, value: T?) {
    if (value != null) {
        this[column] = value
    }
}

private fun ResultRow.toUser() = User(
    id = this.getOrNull(Users.id)?.value,
    username = this[Users.username],
    password = this[Users.password],
    passwordHashAlgorithm = this[Users.passwordHashAlgorithm],
    passwordExpirationDate = this[Users.passwordExpirationDate],
    email = this[Users.email],
    firstName = this[Users.firstName],
    lastName = this[Users.lastName],
    phoneNumber = this[Users.phoneNumber],
    companyName = this[Users.companyName],
    createdBy = this[Users.createdBy],
    createdDateTime = this[Users.createdDateTime],
    updatedBy = this.getOrNull(Users.updatedBy),
    updatedDateTime = this.getOrNull(Users.updatedDateTime)
)
